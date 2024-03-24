import { Component, OnInit } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { FooterComponent } from '../../../shared/components/footer/footer.component';
import { MessageService } from 'primeng/api';
import { CommonModule } from '@angular/common';
import { ActivityItemComponent } from '../activity-item/activity-item.component';
import { ActivityModalComponent } from '../activity-modal/activity-modal.component';
import { ActivityResponse } from '../../../interfaces/responses/activity-response';
import { ActivityService } from '../../../services/activity.service';
import { firstValueFrom } from 'rxjs';
import { ActivityAddRequest } from '../../../interfaces/requests/activity-add-request';
import { ActivityListSkeletonComponent } from '../activity-list-skeleton/activity-list-skeleton.component';
import { ChartDataResponse } from '../../../interfaces/responses/chart-data-response';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-activities',
  standalone: true,
  imports: [
    PrimeNgModule,
    FooterComponent,
    CommonModule,
    ActivityItemComponent,
    ActivityModalComponent,
    ActivityListSkeletonComponent,
  ],
  providers: [MessageService],
  templateUrl: './activities.component.html',
  styleUrl: './activities.component.css',
})
export class ActivitiesComponent implements OnInit {
  isModalOpen: boolean = false;
  isLoadingActivities: boolean = false;
  isLoadingCharts: boolean = false;
  hasErrors: boolean = false;

  selectedDate: Date = new Date();
  isCurrentDate: boolean = true;
  activities: ActivityResponse[] = [];
  chartData: ChartDataResponse[] = [];

  resultChartData: any;
  progressChartData: any;
  resultDataChartOptions: any;
  progressDataChartOptions: any;
  chartLabels: any;

  constructor(
    private activityService: ActivityService,
    private messageService: MessageService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.loadActivityHistory();
    this.loadChartData();
    this.initializeChartOptionsForResults();
    this.initializeChartOptionsForProgress();

    this.route.queryParams.subscribe((params) => {
      if (params['add']) {
        this.isModalOpen = true;
      }
    });
  }

  loadActivityHistory() {
    let month = this.selectedDate.getMonth();
    let year = this.selectedDate.getFullYear();
    this.isLoadingActivities = true;
    firstValueFrom(this.activityService.getAllUserActivities(month + 1, year))
      .then((response) => {
        this.activities = [...response];
      })
      .catch((err) => {
        setTimeout(() => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail:
              'Unable to load your activity history. Please, try again later',
          });
          this.hasErrors = true;
        }, 750);
      })
      .finally(() => {
        setTimeout(() => (this.isLoadingActivities = false), 1000);
      });
  }

  loadChartData() {
    let month = this.selectedDate.getMonth();
    let year = this.selectedDate.getFullYear();
    this.isLoadingCharts;
    firstValueFrom(this.activityService.getMonthlyActivityData(month + 1, year))
      .then((response) => {
        this.chartData = response;
        let numOfDays = this.getNumberOfDaysInMonth(this.selectedDate);
        this.chartLabels = [];
        for (let i = 0; i < numOfDays; i++) {
          this.chartLabels.push(i + 1);
        }
        this.initializeMonthlyResultChartData();
        this.initializeMonthlyProgressChartData();
      })
      .catch((err) => {
        setTimeout(() => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Unable to load chart data. Please, try again later',
          });
          this.hasErrors = true;
        }, 750);
      })
      .finally(() => {
        setTimeout(() => (this.isLoadingCharts = false), 1000);
      });
  }

  onActivityModalSubmit(request: ActivityAddRequest) {
    this.isLoadingActivities = true;
    this.isModalOpen = false;
    firstValueFrom(this.activityService.addNewActivity(request))
      .then((response) => {
        this.activities.unshift(response);
        this.loadChartData();
      })
      .catch(() => {
        setTimeout(() => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Unable to add user activity. Please, try again later',
          });
          this.hasErrors = true;
        }, 750);
      })
      .finally(() => {
        setTimeout(() => (this.isLoadingActivities = false), 1000);
      });
  }

  openActivityModal() {
    this.isModalOpen = true;
  }

  onIsModalOpenChanged(isOpen: boolean) {
    this.isModalOpen = isOpen;
  }

  onCalendarSelect() {
    let currDate = new Date();
    this.isCurrentDate =
      this.selectedDate.getMonth() === currDate.getMonth() &&
      this.selectedDate.getFullYear() === currDate.getFullYear();
    this.loadActivityHistory();
    this.loadChartData();
  }

  downloadUserActivities() {
    let month = this.selectedDate.getMonth();
    let year = this.selectedDate.getFullYear();
    firstValueFrom(
      this.activityService.downloadUserActivities(month + 1, year)
    ).then((response) => {
      const blob = new Blob([response], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      window.open(url);
    });
  }

  //#region  private methods
  private initializeMonthlyResultChartData() {
    const documentStyle = getComputedStyle(document.documentElement);
    this.resultChartData = {
      labels: this.chartLabels,
      datasets: [
        {
          label: 'Weight per day [Avg]',
          borderWidth: 3,
          fill: false,
          tension: 0.3,
          backgroundColor: documentStyle.getPropertyValue('--blue-700'),
          borderColor: documentStyle.getPropertyValue('--blue-700'),
          data: this.chartData.map((e) => {
            return { x: e.day, y: e.avgResult };
          }),
        },
      ],
    };
  }

  private initializeMonthlyProgressChartData() {
    const documentStyle = getComputedStyle(document.documentElement);
    this.progressChartData = {
      labels: this.chartLabels,
      datasets: [
        {
          label: 'Exercise completion [Avg in %]',
          borderWidth: 0,
          borderRadius: 6,
          fill: false,
          borderColor: documentStyle.getPropertyValue('--blue-900'),
          backgroundColor: documentStyle.getPropertyValue('--blue-900'),
          data: this.chartData.map((e) => {
            return { x: e.day, y: e.avgPercentageCompleted };
          }),
        },
        {
          label: 'Training duration [Avg in min]',
          borderWidth: 0,
          borderRadius: 6,
          fill: false,
          borderColor: documentStyle.getPropertyValue('--blue-700'),
          backgroundColor: documentStyle.getPropertyValue('--blue-700'),
          data: this.chartData.map((e) => {
            return { x: e.day, y: e.avgTrainingDuration };
          }),
        },
      ],
    };
  }

  private initializeChartOptionsForResults() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue(
      '--text-color-secondary'
    );
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    this.resultDataChartOptions = {
      maintainAspectRatio: false,
      aspectRatio: 0.7,
      plugins: {
        legend: {
          labels: {
            color: textColor,
          },
        },
        datalabels: {
          align: 'end',
          anchor: 'end',
          borderRadius: 4,
          backgroundColor: 'teal',
          color: 'white',
          font: {
            weight: 'bold',
          },
        },
      },
      scales: {
        x: {
          time: {
            unit: 'day',
          },
          stacked: false,
          ticks: {
            color: textColorSecondary,
            font: {
              weight: 500,
            },
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false,
          },
        },
        y: {
          title: {
            display: true,
            text: 'Weight [kg]',
          },
          stacked: false,
          ticks: {
            color: textColorSecondary,
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false,
          },
          suggestedMin: 50,
          suggestedMax: 100,
        },
      },
    };
  }

  private initializeChartOptionsForProgress() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue(
      '--text-color-secondary'
    );
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    this.progressDataChartOptions = {
      maintainAspectRatio: false,
      aspectRatio: 0.7,
      // parsing: {
      //   xAxisKey: 'day',
      //   yAxisKey: 'avgPercentageCompleted',
      // },
      plugins: {
        legend: {
          labels: {
            color: textColor,
          },
        },
        datalabels: {
          align: 'end',
          anchor: 'end',
          borderRadius: 4,
          backgroundColor: 'teal',
          color: 'white',
          font: {
            weight: 'bold',
          },
        },
      },
      scales: {
        x: {
          time: {
            unit: 'day',
          },
          stacked: false,
          ticks: {
            color: textColorSecondary,
            font: {
              weight: 500,
            },
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false,
          },
        },
        y: {
          title: {
            display: true,
            text: 'Activity complete [%]',
          },
          stacked: false,
          ticks: {
            color: textColorSecondary,
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false,
          },
          suggestedMin: 50,
          suggestedMax: 100,
        },
      },
    };
  }

  private getNumberOfDaysInMonth(date: string | Date): number {
    const tmp = new Date(date);
    tmp.setMonth(tmp.getMonth() + 1);
    tmp.setDate(0);
    return tmp.getDate();
  }

  //#endregion
}

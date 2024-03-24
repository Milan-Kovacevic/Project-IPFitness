import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { ReactiveFormsModule } from '@angular/forms';
import { FitnessProgramService } from '../../../services/fitness-program.service';
import { MenuItem, MessageService } from 'primeng/api';
import { CategoryResponse } from '../../../interfaces/responses/category-response';
import { BasicInformationStepComponent } from './basic-information-step/basic-information-step.component';
import { CategorySelectStepComponent } from './category-select-step/category-select-step.component';
import { ProgramPicturesStepComponent } from './program-pictures-step/program-pictures-step.component';
import { DemonstrationVideosStepComponent } from './demonstration-videos-step/demonstration-videos-step.component';
import { ProgramAddRequest } from '../../../interfaces/requests/program-add-request';
import { HttpErrorResponse } from '@angular/common/http';
import { UserContextService } from '../../../shared/context/user-context.service';
import { SingleFitnessProgramResponse } from '../../../interfaces/responses/single-fitness-program-response';
import { zip } from 'rxjs';
import { ProgramInfoUpdateRequest } from '../../../interfaces/requests/program-info-update-request';
import { EnumItem } from '../../../models/enum-item';

@Component({
  selector: 'app-fitness-program-submit-modal',
  standalone: true,
  imports: [
    PrimeNgModule,
    ReactiveFormsModule,
    BasicInformationStepComponent,
    CategorySelectStepComponent,
    ProgramPicturesStepComponent,
    DemonstrationVideosStepComponent,
  ],
  providers: [MessageService],
  templateUrl: './fitness-program-submit-modal.component.html',
  styleUrl: './fitness-program-submit-modal.component.css',
})
export class FitnessProgramSubmitModalComponent implements OnInit {
  @Input({ required: true }) programId: number | undefined;
  @Input() isOpen: boolean = false;
  @Input() isLoading: boolean = false;
  @Input({ required: true }) categories!: CategoryResponse[];
  @Input({ required: true }) locations!: EnumItem[];
  @Input({ required: true }) difficulties!: EnumItem[];
  @Output() onClose: EventEmitter<void> = new EventEmitter<void>();
  @Output() onSubmit: EventEmitter<boolean> = new EventEmitter<boolean>();

  public modalSteps: MenuItem[] | undefined;
  activeIndex: number = 0;
  isEdit: boolean = false;
  fitnessProgram: SingleFitnessProgramResponse | undefined;

  constructor(
    private fitnessProgramService: FitnessProgramService,
    private messageService: MessageService,
    private userContext: UserContextService
  ) {}

  onActiveIndexChange(event: number) {
    this.activeIndex = event;
  }

  ngOnInit(): void {
    this.modalSteps = [
      {
        label: 'Basic Information',
        styleClass: 'cursor-pointer',
      },
      {
        label: 'Category Select',
        styleClass: 'cursor-pointer',
      },
      {
        label: 'Program Pictures',
        styleClass: 'cursor-pointer',
      },
      {
        label: 'Demonstration Videos',
        styleClass: 'cursor-pointer',
      },
    ];
  }

  onShowModal() {
    this.isLoading = true;
    this.isEdit = this.programId != undefined;

    if (this.programId != undefined && this.programId <= 0) {
      this.isLoading = false;
      this.closeModal();
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Unable to display fitness program details. Please, try again',
      });
    }
    // Update fitness program
    else if (this.programId != undefined && this.programId > 0) {
      this.fitnessProgramService
        .getFitnessProgram(this.programId ?? 0)
        .subscribe({
          next: (response) => {
            // Fill up data for selected fitness program
            this.basicInfoStep.setInitialBasicInformations(response);
            this.categorySelectStep.setInitialCategoryInformations(response);
            this.pictureSelectStep.setInitialPictures(response.images);
            this.demonstrationVideosStep.setInitialDemonstrations(
              response.demonstrations
            );
          },
          error: (err) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Not Found',
              detail: 'Sorry, this fitness program is not available anymore',
            });
          },
        })
        .add(() => setTimeout(() => (this.isLoading = false), 1000));
    }
    // Add new fitness program
    else {
      this.clearModal();
      this.activeIndex = 0;
      setTimeout(() => (this.isLoading = false), 500);
    }
  }

  private clearModal() {
    this.basicInfoStep.clear();
    this.categorySelectStep.clear();
    this.pictureSelectStep.clear();
    this.demonstrationVideosStep.clear();
  }

  /* FOR BASIC INFORAMTION STEP */
  isLocationOnline: boolean = false;
  isBasicFormValid: boolean = false;
  onBasicInfoFormValidityChanges(isValid: boolean) {
    this.isBasicFormValid = isValid;
  }
  onLocationChanges(value: string) {
    this.isLocationOnline = value === 'ONLINE';
  }

  @ViewChild('basicInfoStep')
  basicInfoStep!: BasicInformationStepComponent;

  /* FOR CATEGORY SELECT STEP */
  @ViewChild('categorySelectStep')
  categorySelectStep!: CategorySelectStepComponent;

  /* FOR PICTURES SELECT STEP */
  @ViewChild('pictureSelectStep')
  pictureSelectStep!: ProgramPicturesStepComponent;

  /* FOR DEMONSTRATION VIDEOS */
  hasVideos: boolean = false;
  @ViewChild('demonstrationVideosStep')
  demonstrationVideosStep!: DemonstrationVideosStepComponent;
  onVideoCountChange(count: number) {
    this.hasVideos = count > 0;
  }

  submitFitnessProgram() {
    this.isLoading = true;

    if (this.isEdit) this.updateFitnessProgram();
    else this.addFitnessProgram();
  }

  private addFitnessProgram() {
    let basicInfo = this.basicInfoStep.basicInfoForm.value;
    let categoryInfo = this.categorySelectStep.categoriesForm.value;
    let attributeValues: any[] = [];

    this.categorySelectStep.categoryAttributes.forEach((attribute, index) => {
      if (categoryInfo[attribute.name]) {
        attributeValues.push({
          attributeId: attribute.attributeId,
          value: categoryInfo[attribute.name],
        });
      }
    });

    let programRequest: ProgramAddRequest = {
      userId: this.userContext.getUserId(),
      name: basicInfo.name,
      description: basicInfo.description,
      location: basicInfo.location,
      difficulty: basicInfo.difficulty,
      price: basicInfo.price,
      startDate: basicInfo.dateRange[0],
      endDate: basicInfo.dateRange[1],
      categoryId: categoryInfo.category.categoryId,
      attributeValues: attributeValues,
      pictures: this.pictureSelectStep.programPuctures,
      demonstrations: this.demonstrationVideosStep.demonstrationVideos,
    };
    let successMsg: string = `Fitness progam ${this.basicInfoStep.basicInfoForm.controls['name'].value} created successfully!`;
    this.fitnessProgramService
      .addFitnessProgram(programRequest)
      .subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'info',
            summary: 'Successfull',
            detail: successMsg,
          });
          this.onSubmit.emit(this.isEdit);
        },
        error: (err: HttpErrorResponse) => {
          if (err.status === 500) {
            this.messageService.add({
              severity: 'error',
              summary: 'Server error',
              detail: 'Unable to create program. Please, try again later',
            });
          } else if (err.status === 400) {
            this.messageService.add({
              severity: 'error',
              summary: 'Invalid format',
              detail:
                'Unable to create program. Please, check if all data is typed correctly',
            });
          } else {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Unable to create fitness program. Data is invalid.',
            });
          }
        },
      })
      .add(() => {
        this.isLoading = false;
      });
  }

  private updateFitnessProgram() {
    let basicInfo = this.basicInfoStep.basicInfoForm.value;
    let categoryInfo = this.categorySelectStep.categoriesForm.value;

    if (this.programId === undefined) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Fitness program in not selected. Please, try again',
      });
      return;
    }

    let attributeValues: any[] = [];
    this.categorySelectStep.categoryAttributes.forEach((attribute, index) => {
      if (categoryInfo[attribute.name]) {
        attributeValues.push({
          attributeId: attribute.attributeId,
          value: categoryInfo[attribute.name],
        });
      }
    });

    let infoUpdateRequest: ProgramInfoUpdateRequest = {
      userId: this.userContext.getUserId(),
      name: basicInfo.name,
      description: basicInfo.description,
      location: basicInfo.location,
      difficulty: basicInfo.difficulty,
      price: basicInfo.price,
      startDate: basicInfo.dateRange[0],
      endDate: basicInfo.dateRange[1],
      categoryId: categoryInfo.category.categoryId,
      values: attributeValues,
    };

    let successMsg: string = `Fitness progam ${this.basicInfoStep.basicInfoForm.controls['name'].value} updated successfully!`;
    let observables = [];
    let updateInfoObservable =
      this.fitnessProgramService.updateProgramInformations(
        infoUpdateRequest,
        this.programId
      );
    observables.push(updateInfoObservable);
    if (this.pictureSelectStep.isStepChanged) {
      let updatePicturesObservable =
        this.fitnessProgramService.updateProgramPictures(
          this.pictureSelectStep.programPuctures,
          this.programId
        );
      observables.push(updatePicturesObservable);
    }
    if (this.demonstrationVideosStep.isStepChanged) {
      let updateDemonstrationsObservable =
        this.fitnessProgramService.updateProgramDemonstrations(
          this.demonstrationVideosStep.demonstrationVideos,
          this.programId
        );
      observables.push(updateDemonstrationsObservable);
    }

    zip(observables)
      .subscribe({
        next: (_) => {
          this.messageService.add({
            severity: 'info',
            summary: 'Successfull',
            detail: successMsg,
          });
          this.onSubmit.emit(this.isEdit);
        },
        error: (_) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Unable to update fitness program. Please, try again later.',
          });
        },
      })
      .add(() => {
        this.isLoading = false;
      });
  }

  closeModal() {
    this.onClose.emit();
  }

  submitModal(){
    this.onSubmit.emit(this.isEdit);
    this.pictureSelectStep.isStepChanged = false;
    this.demonstrationVideosStep.isStepChanged = false;
  }
}

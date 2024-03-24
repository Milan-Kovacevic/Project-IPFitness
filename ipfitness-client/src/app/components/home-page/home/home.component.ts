import { Component, OnInit } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { FooterComponent } from '../../../shared/components/footer/footer.component';
import { MessageService } from 'primeng/api';
import { FitnessNewsService } from '../../../services/fitness-news.service';
import { FitnessNewsResponse } from '../../../interfaces/responses/fitness-news-response';
import { FitnessNewsComponent } from '../news/fitness-news/fitness-news.component';
import { FitnessExercisesComponent } from '../exercises/fitness-exercises/fitness-exercises.component';
import { FitnessExercisesService } from '../../../services/fitness-exercises.service';
import { DailyExercisesResponse } from '../../../interfaces/responses/daily-exercises-response';
import { UserContextService } from '../../../shared/context/user-context.service';
import { firstValueFrom, zip } from 'rxjs';
import { CategorySubscriptionComponent } from '../category-subscription/category-subscription.component';
import { CategorySubscriptionResponse } from '../../../interfaces/responses/category-subscription-response';
import { CategoryService } from '../../../services/category.service';

@Component({
  selector: 'app-home',
  standalone: true,
  providers: [MessageService],
  imports: [
    PrimeNgModule,
    FooterComponent,
    FitnessNewsComponent,
    FitnessExercisesComponent,
    CategorySubscriptionComponent,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {
  isAccountActivated: boolean;
  news: FitnessNewsResponse | undefined;
  dailyExercises: DailyExercisesResponse | undefined;
  isLoadingNews: boolean = false;
  isLoadingExercises: boolean = false;
  newsError: boolean = false;
  exercisesError: boolean = false;
  categoriesError: boolean = false;
  categories: CategorySubscriptionResponse[] = [];

  constructor(
    private fitnessNewsService: FitnessNewsService,
    private fitnessExercisesService: FitnessExercisesService,
    private categoryService: CategoryService,
    private messageService: MessageService,
    userContext: UserContextService
  ) {
    this.isAccountActivated = userContext.isActivated();
  }

  ngOnInit(): void {
    this.isLoadingNews = true;
    firstValueFrom(this.fitnessNewsService.getAllFitnessNews())
      .then((response) => {
        this.news = response;
      })
      .catch(() => {
        this.messageService.add({
          severity: 'info',
          summary: 'News Unavailable',
          detail: 'Unable to load fitness news. Please, try again later',
        });
        this.newsError = true;
      })
      .finally(() => setTimeout(() => (this.isLoadingNews = false), 1000));

    this.isLoadingExercises = true;

    if (this.isAccountActivated) {
      firstValueFrom(this.fitnessExercisesService.getDailyExercises())
        .then((response) => {
          this.dailyExercises = response;
        })
        .catch(() => {
          this.messageService.add({
            severity: 'info',
            summary: 'Exercises Unavailable',
            detail:
              'Unable to load daily exercise suggestions. Please, try again later',
          });
          this.exercisesError = true;
        })
        .finally(() =>
          setTimeout(() => (this.isLoadingExercises = false), 1000)
        );

      this.loadUserCategorySubscriptions();
    }
  }

  onCategorySubscriptionChanged(categoryId: number, isSubscribed: boolean) {
    let index: number = this.categories.findIndex(
      (c) => c.categoryId === categoryId
    );
    if (index != -1) {
      this.categoryService
        .changeUserSubscriptionOnCategory(categoryId, isSubscribed)
        .subscribe({
          next: () => {
            this.categories[index].isSubscribed = isSubscribed;
          },
          error: () => {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail:
                'Unable to change category subscription status. Please, try again later',
            });
          },
        });
    }
  }

  private loadUserCategorySubscriptions() {
    firstValueFrom(this.categoryService.getAllUserCategorySubscriptions())
      .then((response) => {
        this.categories = response;
      })
      .catch(() => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail:
            'Unable to load your category subscriptions. Please, try again later',
        });
        this.categoriesError = true;
      });
  }
}

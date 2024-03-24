import { Component, Input, OnInit } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';
import { ExerciseItemComponent } from '../exercise-item/exercise-item.component';
import { DailyExercisesResponse } from '../../../../interfaces/responses/daily-exercises-response';
import { FitnessExercisesService } from '../../../../services/fitness-exercises.service';

@Component({
  selector: 'app-fitness-exercises',
  standalone: true,
  imports: [PrimeNgModule, ExerciseItemComponent],
  templateUrl: './fitness-exercises.component.html',
  styleUrl: './fitness-exercises.component.css',
})
export class FitnessExercisesComponent {
  @Input({ required: true }) isLoading: boolean = false;
  @Input({ required: true }) hasErrors: boolean = false;
  @Input({ required: true }) dailyExercises: DailyExercisesResponse | undefined;
}

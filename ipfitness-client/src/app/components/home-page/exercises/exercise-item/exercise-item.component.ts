import { Component, Input } from '@angular/core';
import { ExerciseResponse } from '../../../../interfaces/responses/exercise-response';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';

@Component({
  selector: 'app-exercise-item',
  standalone: true,
  imports: [PrimeNgModule],
  templateUrl: './exercise-item.component.html',
  styleUrl: './exercise-item.component.css'
})
export class ExerciseItemComponent {
  @Input({required: true}) exercise!: ExerciseResponse;

}

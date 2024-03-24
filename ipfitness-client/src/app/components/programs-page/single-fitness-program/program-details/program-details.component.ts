import { Component, Input } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';
import { ProgramDetailsResponse } from '../../../../interfaces/responses/program-details-response';
import { ProgramDetailsSkeletonComponent } from '../program-details-skeleton/program-details-skeleton.component';

@Component({
  selector: 'app-program-details',
  standalone: true,
  imports: [PrimeNgModule, ProgramDetailsSkeletonComponent],
  templateUrl: './program-details.component.html',
  styleUrl: './program-details.component.css'
})
export class ProgramDetailsComponent {
  @Input({required: true}) program: ProgramDetailsResponse | undefined;
  @Input({required: true}) ownerAvatar!: string;
  @Input({required: true}) isLoading!: boolean;
}

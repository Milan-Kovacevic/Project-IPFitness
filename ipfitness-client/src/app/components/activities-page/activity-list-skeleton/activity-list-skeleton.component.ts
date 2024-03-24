import { Component } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';

@Component({
  selector: 'app-activity-list-skeleton',
  standalone: true,
  imports: [PrimeNgModule],
  templateUrl: './activity-list-skeleton.component.html',
  styleUrl: './activity-list-skeleton.component.css'
})
export class ActivityListSkeletonComponent {

}

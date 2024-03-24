import { Component, Input } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { ActivityResponse } from '../../../interfaces/responses/activity-response';

@Component({
  selector: 'app-activity-item',
  standalone: true,
  imports: [PrimeNgModule],
  templateUrl: './activity-item.component.html',
  styleUrl: './activity-item.component.css',
})
export class ActivityItemComponent {
  @Input({required: true}) activity!: ActivityResponse;
}

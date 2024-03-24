import { Component } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';

@Component({
  selector: 'app-purchase-item-skeleton',
  standalone: true,
  imports: [PrimeNgModule],
  templateUrl: './purchase-item-skeleton.component.html',
  styleUrl: './purchase-item-skeleton.component.css',
})
export class PurchaseItemSkeletonComponent {}

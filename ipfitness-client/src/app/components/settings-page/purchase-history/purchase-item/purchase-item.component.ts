import { Component, Input } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';
import { PurchaseItemResponse } from '../../../../interfaces/responses/purchase-item-response';
import { Router } from '@angular/router';

@Component({
  selector: 'app-purchase-item',
  standalone: true,
  imports: [PrimeNgModule],
  templateUrl: './purchase-item.component.html',
  styleUrl: './purchase-item.component.css',
})
export class PurchaseItemComponent {
  @Input({ required: true }) purchaseItem!: PurchaseItemResponse;

  constructor(private router: Router) {}

  openProgramDetails() {
    this.router.navigate([`programs/${this.purchaseItem.programId}`]);
  }
}

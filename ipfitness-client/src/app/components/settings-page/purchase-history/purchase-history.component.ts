import { Component, OnInit } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { MessageService } from 'primeng/api';
import { PurchaseItemComponent } from './purchase-item/purchase-item.component';
import { ProgramPurchaseService } from '../../../services/program-purchase.service';
import { PurchaseItemResponse } from '../../../interfaces/responses/purchase-item-response';
import { firstValueFrom } from 'rxjs';
import {
  BACKEND_BASE_URL,
  paymentTypes,
  programDifficulties,
  programLocations,
} from '../../../utils/constants';
import { PurchaseItemSkeletonComponent } from './purchase-item-skeleton/purchase-item-skeleton.component';

@Component({
  selector: 'app-purchase-history',
  standalone: true,
  providers: [MessageService],
  imports: [
    PrimeNgModule,
    PurchaseItemComponent,
    PurchaseItemSkeletonComponent,
  ],
  templateUrl: './purchase-history.component.html',
  styleUrl: './purchase-history.component.css',
})
export class PurchaseHistoryComponent implements OnInit {
  private PICTURES_URL = `${BACKEND_BASE_URL}/storage?fileName=`;
  private DEFAULT_FITNESS_PROGRAM_IMAGE = 'assets/article-heading-img.png';
  isLoading: boolean = false;
  hasErrors: boolean = false;
  purchaseItems: PurchaseItemResponse[] = [];

  constructor(
    private messageService: MessageService,
    private programPurchaseService: ProgramPurchaseService
  ) {}

  ngOnInit(): void {
    this.isLoading = true;
    firstValueFrom(this.programPurchaseService.getPurchaseHistoryForUser())
      .then((response) => {
        response.forEach((item) => {
          item.location =
            programLocations.find((l) => l.key === item.location)
              ?.displayName ?? item.location;
          item.difficulty =
            programDifficulties.find((d) => d.key === item.difficulty)
              ?.displayName ?? item.difficulty;
          item.paymentType =
            paymentTypes.find((p) => p.key === item.paymentType)?.displayName ??
            item.paymentType;
          item.programPicture = item.programPicture
            ? `${this.PICTURES_URL}${item.programPicture}`
            : this.DEFAULT_FITNESS_PROGRAM_IMAGE;
        });
        this.purchaseItems = response;
      })
      .catch(() => {
        this.messageService.add({
          severity: 'error',
          summary: 'Purchase history unavailable',
          detail:
            'Unable to load your purchase history. Please, try again later',
        });
        this.hasErrors = true;
      })
      .finally(() => setTimeout(() => (this.isLoading = false), 750));
  }
}

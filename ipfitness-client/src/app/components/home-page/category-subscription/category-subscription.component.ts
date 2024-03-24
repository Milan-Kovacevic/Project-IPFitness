import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { CategorySubscriptionResponse } from '../../../interfaces/responses/category-subscription-response';

@Component({
  selector: 'app-category-subscription',
  standalone: true,
  imports: [PrimeNgModule],
  templateUrl: './category-subscription.component.html',
  styleUrl: './category-subscription.component.css',
})
export class CategorySubscriptionComponent {
  @Input({ required: true }) categories!: CategorySubscriptionResponse[];
  @Output() onCategorySubscribe = new EventEmitter<number>();
  @Output() onCategoryUnsubscribe = new EventEmitter<number>();

  subscribeToCategory(category: CategorySubscriptionResponse) {
    this.onCategorySubscribe.emit(category.categoryId);
  }
  unsubscribeFromCategory(category: CategorySubscriptionResponse) {
    this.onCategoryUnsubscribe.emit(category.categoryId);
  }
}

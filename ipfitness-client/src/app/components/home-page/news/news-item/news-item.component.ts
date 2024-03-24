import { Component, Input } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';
import { NewsItemResponse } from '../../../../interfaces/responses/news-item-response';

@Component({
  selector: 'app-news-item',
  standalone: true,
  imports: [PrimeNgModule],
  templateUrl: './news-item.component.html',
  styleUrl: './news-item.component.css'
})
export class NewsItemComponent {
  @Input({required: true}) newsItem!: NewsItemResponse;

  
}

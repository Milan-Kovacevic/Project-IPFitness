import { Component, Input } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';
import { FitnessNewsResponse } from '../../../../interfaces/responses/fitness-news-response';
import { NewsItemComponent } from '../news-item/news-item.component';
import { NewsItemSkeletonComponent } from '../news-item-skeleton/news-item-skeleton.component';

@Component({
  selector: 'app-fitness-news',
  standalone: true,
  imports: [PrimeNgModule, NewsItemComponent, NewsItemSkeletonComponent],
  templateUrl: './fitness-news.component.html',
  styleUrl: './fitness-news.component.css',
})
export class FitnessNewsComponent {
  @Input({ required: true }) news!: FitnessNewsResponse | undefined;
  @Input({ required: true }) isLoading: boolean = false;
  @Input({ required: true }) hasErrors: boolean = false;
}

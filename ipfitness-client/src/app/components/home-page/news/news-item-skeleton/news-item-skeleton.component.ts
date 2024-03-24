import { Component } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';

@Component({
  selector: 'app-news-item-skeleton',
  standalone: true,
  imports: [PrimeNgModule],
  templateUrl: './news-item-skeleton.component.html',
  styleUrl: './news-item-skeleton.component.css'
})
export class NewsItemSkeletonComponent {

}

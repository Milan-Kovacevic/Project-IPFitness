import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { BackgroundComponent } from '../../auth-page/background/background.component';

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [RouterLink, BackgroundComponent],
  templateUrl: './not-found.component.html',
  styleUrl: './not-found.component.css',
})
export class NotFoundComponent {
  constructor(private router: Router) {}

  navigateToHomePage() {
    this.router.navigate(['/home']);
  }
}

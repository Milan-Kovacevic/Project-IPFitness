import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { HeaderComponent } from '../../shared/components/header/header.component';

@Component({
  selector: 'app-navigation-layout',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, RouterLink],
  templateUrl: './navigation-layout.component.html',
  styleUrl: './navigation-layout.component.css'
})
export class NavigationLayoutComponent {

}

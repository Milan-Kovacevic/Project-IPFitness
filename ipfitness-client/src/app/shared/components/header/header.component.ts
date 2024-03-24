import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { PrimeNgModule } from '../../prime-ng/prime-ng.module';
import { UserContextService } from '../../context/user-context.service';
import { BACKEND_BASE_URL } from '../../../utils/constants';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, PrimeNgModule, RouterLinkActive],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  public isLoggedIn: boolean;
  public isActivated: boolean;
  public isMenuOpened: boolean = false;
  public profileImage: string;
  public userDisplayName: string;
  private STORAGE_URL = `${BACKEND_BASE_URL}/storage`;

  constructor(private userContext: UserContextService, private router: Router) {
    this.isLoggedIn = userContext.isLoggedIn();
    this.isActivated = userContext.isActivated();
    let user = userContext.getUser();
    this.profileImage =
      user == undefined || user?.avatar == undefined
        ? ''
        : `${this.STORAGE_URL}?fileName=${user.avatar}`;

    this.userDisplayName =
      user?.firstName +
      ' ' +
      user?.lastName +
      ' - ' +
      (this.isActivated ? 'Activated' : 'Not Activated');
  }

  logout() {
    this.userContext.logoutUser();
    this.isLoggedIn = false;
    this.router.navigate(['/auth/login']);
  }

  onMenuClick() {
    this.isMenuOpened = !this.isMenuOpened;
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';

@Component({
  selector: 'app-activate-account',
  standalone: true,
  imports: [PrimeNgModule, RouterLink],
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.css',
})
export class ActivateAccountComponent implements OnInit {
  isActivated: boolean = false;
  isLoading: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Send verify token
    this.route.queryParams.subscribe((params) => {
      if (params['token']) {
        this.authService
          .verify(params['token'])
          .subscribe({
            complete: () => {
              this.isActivated = true;
            },
            error: (err) => {
              if (err instanceof HttpErrorResponse) {
                let httpErr = err as HttpErrorResponse;
                if (httpErr.status === 404) {
                  this.isActivated = false;
                }
              }
            },
          })
          .add(() => {
            this.isLoading = false;
          });
      }
    });
  }
}

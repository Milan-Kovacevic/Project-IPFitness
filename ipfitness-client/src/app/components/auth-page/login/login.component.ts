import { Component } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { Router, RouterLink } from '@angular/router';

import {
  FormBuilder,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../../services/auth.service';
import { LoginRequest } from '../../../interfaces/requests/login-request';
import { UserContextService } from '../../../shared/context/user-context.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [PrimeNgModule, ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  providers: [MessageService],
})
export class LoginComponent {
  constructor(
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private authService: AuthService,
    private router: Router,
    private userContext: UserContextService
  ) {}

  public isLoading: boolean = false;
  public loginForm = this.formBuilder.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]],
  });

  public login() {
    this.isLoading = true;
    const loginData = this.loginForm.value;

    this.authService
      .login(loginData as LoginRequest)
      .subscribe({
        next: (response) => {
          this.userContext.loginUser(response);
          if(response.active === false){
            this.messageService.add({
              severity: 'info',
              summary: 'Verification Required',
              detail: 'Please, check your email to activate your account.',
            });
          }
          else {
            this.navigateToHomePage();
          }
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Unable to login',
            detail: 'Invalid username or password',
          });
        },
      })
      .add(() => {
        this.isLoading = false;
      });
  }

  public navigateToHomePage() {
    this.router.navigate(['/home']);
  }
}

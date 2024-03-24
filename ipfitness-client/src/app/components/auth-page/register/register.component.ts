import { Component } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { MessageService } from 'primeng/api';
import { RouterLink } from '@angular/router';
import { passwordMatchValidator } from '../../../shared/validators/password-match.validatior';
import { RecaptchaModule } from 'ng-recaptcha';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { FileSelectEvent } from 'primeng/fileupload';
import { RECAPTCHA_API_KEY } from '../../../utils/constants';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    PrimeNgModule,
    RouterLink,
    RecaptchaModule,
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
  providers: [MessageService],
})
export class RegisterComponent {
  public isLoading: boolean = false;
  public isSubmitting: boolean = false;
  private selectedAvatar?: File;
  public captchaKey: string = RECAPTCHA_API_KEY;

  constructor(
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private authService: AuthService
  ) {}

  registerForm: FormGroup = this.formBuilder.group(
    {
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', Validators.required],
      repeatPassword: ['', [Validators.required]],
      city: ['', [Validators.required]],
      mail: ['', [Validators.required, Validators.email]],
      avatar: [undefined],
    },
    {
      validators: passwordMatchValidator,
    }
  );

  public selectAvatar(event: FileSelectEvent) {
    if (event.currentFiles.length > 0) {
      this.selectedAvatar = event.currentFiles[0];
    }
  }

  public onRegister(){
    this.isSubmitting = true;
  }

  public register() {
    this.isLoading = true;
    const registerData = this.registerForm.value;
    delete registerData.repeatPassword;
    registerData.picture = this.selectedAvatar;

    this.authService
      .register(registerData)
      .subscribe({
        complete: () => {
          this.messageService.add({
            severity: 'info',
            summary: 'Successfull registration',
            detail:
              'Please, verify your account using link sent to you by email',
            sticky: true,
            closable: true,
          });
        },
        error: (err) => {
          if (err instanceof HttpErrorResponse) {
            let message = '';
            if (err.status == 409) message = 'Username is already taken';
            else message = 'Unexpected error. Please, try again later';

            this.messageService.add({
              severity: 'error',
              summary: 'Unable to register',
              detail: message,
            });
          }
        },
      })
      .add(() => {
        this.isLoading = false;
      });
  }

  public resolveCaptcha(captchaResposne: string | null) {
    if (captchaResposne === null) return;
    this.isSubmitting = false;
    this.register();
  }
}

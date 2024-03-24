import { Component, OnInit } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { ConfirmationService, MessageService } from 'primeng/api';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { UserProfileService } from '../../../services/user-profile.service';
import { UserInfoResponse } from '../../../interfaces/responses/user-info-response';
import { BlockableDivComponent } from '../../../shared/components/blockable-div/blockable-div.component';
import { UserInfoRequest } from '../../../interfaces/requests/user-info-request';
import { HttpErrorResponse } from '@angular/common/http';
import { UserAvatarResponse } from '../../../interfaces/responses/user-avatar-response';
import { passwordMatchValidator } from '../../../shared/validators/password-match.validatior';
import { UserPasswordRequest } from '../../../interfaces/requests/user-password-request';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import { UserContextService } from '../../../shared/context/user-context.service';
import { BACKEND_BASE_URL } from '../../../utils/constants';
import { FileBeforeUploadEvent, FileSelectEvent } from 'primeng/fileupload';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    PrimeNgModule,
    ReactiveFormsModule,
    FormsModule,
    BlockableDivComponent,
    ConfirmDialogComponent,
  ],
  providers: [ConfirmationService, MessageService],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {
  public userInfoForm: FormGroup = new FormGroup({});
  public userPasswordForm: FormGroup = new FormGroup({});

  public isPasswordModalOpen: boolean = false;
  public isPictureModalOpen: boolean = false;
  public isConfirmDialogOpen: boolean = false;
  public isLoading: boolean = false;
  public isProfileUnavailable: boolean = false;
  public isLoggedIn: boolean = true;
  public avatar?: string;
  public selectedPicture?: File;
  private STORAGE_URL = `${BACKEND_BASE_URL}/storage`;

  constructor(
    private formBuilder: FormBuilder,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private userProfileService: UserProfileService,
    private userContext: UserContextService
  ) {
    this.isLoggedIn = userContext.isLoggedIn();
  }

  ngOnInit(): void {
    if (this.isLoggedIn) {
      this.isLoading = true;
      setTimeout(() => this.loadUserProfileInfo(), 1500);
    }

    this.userInfoForm = this.formBuilder.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      city: ['', [Validators.required]],
      username: ['', [Validators.required]],
      mail: ['', [Validators.required]],
      biography: [''],
      contactInfo: [''],
    });

    this.userPasswordForm = this.formBuilder.group(
      {
        previousPassword: ['', [Validators.required]],
        password: ['', [Validators.required]],
        repeatPassword: ['', [Validators.required]],
      },
      {
        validators: passwordMatchValidator,
      }
    );
  }

  public showConfirmChangeDialog() {
    this.confirmationService.confirm({
      accept: () => {
        this.updateUserProfileInfo();
      },
    });
  }

  public updateUserPassword() {
    const userPasswordData = this.userPasswordForm.value;
    let passwordChangeRequest: UserPasswordRequest = {
      oldPassword: userPasswordData.previousPassword,
      newPassword: userPasswordData.password,
    };

    this.userProfileService
      .updateUserPassword(passwordChangeRequest)
      .subscribe({
        next: (response) => {
          this.messageService.add({
            severity: 'info',
            summary: 'Successfull',
            detail: 'Password changed succesfully!',
          });
          this.isPasswordModalOpen = false;
        },
        error: (err) => {
          if (err instanceof HttpErrorResponse) {
            let httpErr: HttpErrorResponse = err as HttpErrorResponse;
            if (httpErr.status === 400) {
              this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail:
                  'Specified current password is invalid. Please, try again.',
              });
            }
          } else {
            this.messageService.add({
              severity: 'error',
              summary: 'Unexpected error',
              detail: 'Unable to change password',
            });
          }
        },
      });
  }

  public setPasswordModalOpen(isOpen: boolean) {
    this.isPasswordModalOpen = isOpen;
  }

  public setPictureModalOpen(isOpen: boolean) {
    this.isPictureModalOpen = isOpen;
  }

  public selectPicture(event: FileSelectEvent) {
    if (event.currentFiles.length > 0) {
      this.selectedPicture = event.currentFiles[0];
    }
  }

  public updateUserProfileAvatar(event: FileBeforeUploadEvent) {
    this.isPictureModalOpen = false;
    if (!this.selectedPicture) return;

    this.isLoading = true;
    this.userProfileService
      .saveProfileAvatar(this.selectedPicture)
      .subscribe({
        next: (response) => {
          let avatarJson: UserAvatarResponse = response as UserAvatarResponse;
          if (avatarJson) {
            this.avatar = `${this.STORAGE_URL}?fileName=${avatarJson.avatar}`;
            this.messageService.add({
              severity: 'info',
              summary: 'Successfull',
              detail: 'Profile avatar changed succesfully!',
            });

            this.userContext.updateUserAvatarContext(avatarJson);
          }
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Unable to change profile avatar',
          });
        },
      })
      .add(() => {
        this.isLoading = false;
      });
  }

  private loadUserProfileInfo() {
    if (!this.isLoggedIn) return;

    let user: UserInfoResponse;
    this.userProfileService
      .getUserInfo()
      .subscribe({
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Server Not Available',
            detail: 'Unable to load user profile. Please, try again later',
          });
          this.isProfileUnavailable = true;
        },
        next: (response) => {
          user = response as UserInfoResponse;
          this.userInfoForm = this.formBuilder.group({
            firstName: [user?.firstName, [Validators.required]],
            lastName: [user?.lastName, [Validators.required]],
            city: [user?.city, [Validators.required]],
            username: [user?.username, [Validators.required]],
            mail: [user?.mail, [Validators.required]],
            biography: [user?.biography],
            contactInfo: [user?.contactInfo],
          });

          if (user.avatar) {
            this.avatar =
              'http://localhost:9001/api/v1/storage?fileName=' + user.avatar;
          } else {
            this.avatar = 'assets/default-avatar.png';
          }
        },
      })
      .add(() => {
        this.isLoading = false;
      });
  }

  private updateUserProfileInfo() {
    this.isLoading = true;
    const userInfoData = this.userInfoForm.value;
    delete userInfoData.username;
    this.userProfileService;
    const userInfo: UserInfoRequest = userInfoData as UserInfoRequest;
    this.userProfileService
      .updateUserInfo(userInfo)
      .subscribe({
        complete: () => {
          this.messageService.add({
            severity: 'info',
            summary: 'Successfull',
            detail: 'Profile informations changed succesfully!',
          });
          this.userContext.updateUserInfoContext(userInfo);
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Unable to change profile informations',
          });
        },
      })
      .add(() => {
        this.isLoading = false;
      });
  }
}

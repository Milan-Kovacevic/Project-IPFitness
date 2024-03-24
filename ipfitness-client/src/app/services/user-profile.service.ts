import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserInfoRequest } from '../interfaces/requests/user-info-request';
import { UserPasswordRequest } from '../interfaces/requests/user-password-request';
import { BACKEND_BASE_URL } from '../utils/constants';
import { UserContextService } from '../shared/context/user-context.service';

@Injectable({
  providedIn: 'root',
})
export class UserProfileService {
  constructor(
    private http: HttpClient,
    private userContext: UserContextService
  ) {}
  private USER_INFO_URL: string = `${BACKEND_BASE_URL}/users`;

  public getUserInfo() {
    let userId = this.userContext.getUserId();
    let getUserEndpoint = `${this.USER_INFO_URL}/${userId}/info`;
    return this.http.get(getUserEndpoint, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${this.userContext.getToken()}`
      },
    });
  }

  public updateUserInfo(request: UserInfoRequest) {
    let userId = this.userContext.getUserId();
    let updateInfoEndpoint = `${this.USER_INFO_URL}/${userId}/info`;
    return this.http.put(updateInfoEndpoint, request, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${this.userContext.getToken()}`
      },
    });
  }

  public updateUserPassword(request: UserPasswordRequest) {
    let userId = this.userContext.getUserId();
    let updatePasswordEndpoint = `${this.USER_INFO_URL}/${userId}/info/password`;
    return this.http.post(updatePasswordEndpoint, request, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${this.userContext.getToken()}`
      },
    });
  }

  public saveProfileAvatar(avatar: File) {
    let userId = this.userContext.getUserId();
    let formData = new FormData();
    formData.append('avatar', avatar);

    let updateAvatarEndpoint = `${this.USER_INFO_URL}/${userId}/info/avatar`;
    return this.http.put(updateAvatarEndpoint, formData, {
      headers: {
        'Authorization': `Bearer ${this.userContext.getToken()}`,
      }
    });
  }
}

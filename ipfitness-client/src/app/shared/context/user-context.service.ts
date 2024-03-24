import { Injectable } from '@angular/core';
import { LoginResponse } from '../../interfaces/responses/login-response';
import { UserInfoRequest } from '../../interfaces/requests/user-info-request';
import { UserAvatarResponse } from '../../interfaces/responses/user-avatar-response';

@Injectable({
  providedIn: 'root',
})
export class UserContextService {
  private USER_STORAGE_KEY = 'IPFITNESS_USER';
  private user: LoginResponse | undefined;

  constructor() {
    this.loadUserContext();
  }

  public loginUser(loggedUser: LoginResponse) {
    this.user = loggedUser;
    this.saveUserContext();
  }

  public logoutUser() {
    this.user = undefined;
    localStorage.removeItem(this.USER_STORAGE_KEY);
  }

  public isActivated(){
    return this.user != undefined && this.user.active == true;
  }

  public isLoggedIn(): boolean {
    return this.user != undefined;
  }

  public getUser(): LoginResponse | undefined {
    return this.user;
  }

  public getToken(): string {
    return this.user?.token ?? '';
  }

  public getUserId(): number {
    return this.user?.userId ?? 0;
  }

  updateUserAvatarContext(avatarJson: UserAvatarResponse) {
    if (!this.user) return;

    this.user.avatar = avatarJson.avatar;
    this.saveUserContext();
  }

  updateUserInfoContext(userInfo: UserInfoRequest) {
    if (!this.user) return;

    this.user.firstName = userInfo.firstName;
    this.user.lastName = userInfo.lastName;
    this.saveUserContext();
  }

  private loadUserContext() {
    if (!this.user) {
      let userJson = localStorage.getItem(this.USER_STORAGE_KEY);
      if (userJson) this.user = JSON.parse(userJson);
    }
  }

  private saveUserContext() {
    if (this.user)
      localStorage.setItem(this.USER_STORAGE_KEY, JSON.stringify(this.user));
  }
}

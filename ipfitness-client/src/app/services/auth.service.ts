import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../interfaces/requests/login-request';
import { RegisterRequest } from '../interfaces/requests/register-request';
import { LoginResponse } from '../interfaces/responses/login-response';
import { BACKEND_BASE_URL } from '../utils/constants';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}
  private LOGIN_URL: string = `${BACKEND_BASE_URL}/auth/login`;
  private REGISTER_URL: string = `${BACKEND_BASE_URL}/auth/register`;
  private VERIFY_URL: string = `${BACKEND_BASE_URL}/auth/verify`;

  public login(request: LoginRequest) {
    return this.http.post<LoginResponse>(this.LOGIN_URL, request, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
  }

  public register(request: RegisterRequest) {
    const formData = new FormData();
    formData.append('firstName', request.firstName);
    formData.append('lastName', request.lastName);
    formData.append('username', request.username);
    formData.append('password', request.password);
    formData.append('city', request.city);
    formData.append('mail', request.mail);
    if (request.picture != undefined)
      formData.append('picture', request.picture);

    return this.http.post(this.REGISTER_URL, formData);
  }

  public verify(token: string) {
    return this.http.get(this.VERIFY_URL, { params: { token } });
  }
}

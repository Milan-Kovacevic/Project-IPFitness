import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BACKEND_BASE_URL } from '../utils/constants';
import { UserContextService } from '../shared/context/user-context.service';
import { ActivityResponse } from '../interfaces/responses/activity-response';
import { ActivityAddRequest } from '../interfaces/requests/activity-add-request';
import { ChartDataResponse } from '../interfaces/responses/chart-data-response';

@Injectable({
  providedIn: 'root',
})
export class ActivityService {
  constructor(
    private http: HttpClient,
    private userContext: UserContextService
  ) {}

  public getAllUserActivities(month: number, year: number) {
    let params = this.setRequiredUrlParams(month, year);
    let userActivitiesUrl = `${BACKEND_BASE_URL}/users/${this.userContext.getUserId()}/activities`;
    return this.http.get<ActivityResponse[]>(userActivitiesUrl, {
      params: params,
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }

  public addNewActivity(request: ActivityAddRequest) {
    request.userId = this.userContext.getUserId();
    let activitiesUrl = `${BACKEND_BASE_URL}/activities`;
    return this.http.post<ActivityResponse>(activitiesUrl, request, {
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }

  public getMonthlyActivityData(month: number, year: number) {
    let params = this.setRequiredUrlParams(month, year);
    let activitiesDataUrl = `${BACKEND_BASE_URL}/users/${this.userContext.getUserId()}/activities/monthly-data`;
    return this.http.get<ChartDataResponse[]>(activitiesDataUrl, {
      params: params,
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }

  downloadUserActivities(month: number, year: number) {
    let params = this.setRequiredUrlParams(month, year);
    let downloadActivitiesEndpoint = `${BACKEND_BASE_URL}/users/${this.userContext.getUserId()}/activities/download`;
    return this.http.get(downloadActivitiesEndpoint, {
      responseType: 'blob',
      params: params,
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }

  private setRequiredUrlParams(month: number, year: number): HttpParams {
    let queryParams = new HttpParams();
    const params = queryParams.appendAll({
      month: month,
      year: year,
    });
    return params;
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CategoryResponse } from '../interfaces/responses/category-response';
import { CategoryAttributeResponse } from '../interfaces/responses/category-attribute-response';
import { BACKEND_BASE_URL } from '../utils/constants';
import { UserContextService } from '../shared/context/user-context.service';
import { CategorySubscriptionResponse } from '../interfaces/responses/category-subscription-response';
import { CategorySubscriptionChangeRequest } from '../interfaces/requests/category-subscription-change-request';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private CATEGORIES_URL: string = `${BACKEND_BASE_URL}/categories`;
  private USERS_URL: string = `${BACKEND_BASE_URL}/users`;
  constructor(
    private http: HttpClient,
    private userContext: UserContextService
  ) {}

  public getAllCategories() {
    return this.http.get<CategoryResponse[]>(this.CATEGORIES_URL);
  }

  public getAllUserCategorySubscriptions() {
    let userId = this.userContext.getUserId();
    let token = this.userContext.getToken();
    const categorySubscriptionsEndpoint = `${this.USERS_URL}/${userId}/categories`;
    return this.http.get<CategorySubscriptionResponse[]>(
      categorySubscriptionsEndpoint,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
  }

  public getCategoryAttributes(categoryId: number) {
    return this.http.get<CategoryAttributeResponse[]>(
      `${this.CATEGORIES_URL}/${categoryId}/attributes`
    );
  }

  public changeUserSubscriptionOnCategory(
    categoryId: number,
    isSubscribed: boolean
  ) {
    let userId = this.userContext.getUserId();
    let token = this.userContext.getToken();
    const categorySubscriptionsEndpoint = `${this.USERS_URL}/${userId}/categories`;
    let request: CategorySubscriptionChangeRequest = {
      categoryId: categoryId,
      subscribe: isSubscribed,
    };
    return this.http.put<void>(categorySubscriptionsEndpoint, request, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  }
}

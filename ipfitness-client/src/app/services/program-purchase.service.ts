import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BACKEND_BASE_URL } from '../utils/constants';
import { ProgramPurchaseRequest } from '../interfaces/requests/program-purchase-request';
import { UserContextService } from '../shared/context/user-context.service';
import { PaymentItem } from '../models/payment-item';
import { PurchaseItemResponse } from '../interfaces/responses/purchase-item-response';

@Injectable({
  providedIn: 'root',
})
export class ProgramPurchaseService {
  private PROGRAMS_URL: string = `${BACKEND_BASE_URL}/programs`;
  private USERS_URL: string = `${BACKEND_BASE_URL}/users`;

  constructor(
    private http: HttpClient,
    private userContext: UserContextService
  ) {}

  public purchaseFitnessProgram(
    programId: number,
    paymentType: PaymentItem,
    cardNumber: string
  ) {
    let userId = this.userContext.getUserId();
    let token = this.userContext.getToken();
    let purchaseProgramEndpoint = `${this.PROGRAMS_URL}/${programId}/purchase`;
    let purchaseRequest: ProgramPurchaseRequest = {
      userId: userId,
      paymentType: paymentType.key,
      cardNumber: cardNumber,
    };
    return this.http.post(purchaseProgramEndpoint, purchaseRequest, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  public getPurchaseHistoryForUser() {
    let userId = this.userContext.getUserId();
    let token = this.userContext.getToken();
    let purchaseHistoryEndpoint = `${this.USERS_URL}/${userId}/purchases`;
    return this.http.get<PurchaseItemResponse[]>(purchaseHistoryEndpoint, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  }
}

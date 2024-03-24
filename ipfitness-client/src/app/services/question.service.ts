import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserContextService } from '../shared/context/user-context.service';
import { BACKEND_BASE_URL } from '../utils/constants';
import { QuestionRequest } from '../interfaces/requests/question-request';

@Injectable({
  providedIn: 'root',
})
export class QuestionService {
  private QUESTIONS_URL: string = `${BACKEND_BASE_URL}/questions`;

  constructor(
    private http: HttpClient,
    private userContext: UserContextService
  ) {}

  public sendQuestion(content: string) {
    let request: QuestionRequest = {
      userId: this.userContext.getUserId(),
      content: content,
    };
    return this.http.post(this.QUESTIONS_URL, request, {
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }
}

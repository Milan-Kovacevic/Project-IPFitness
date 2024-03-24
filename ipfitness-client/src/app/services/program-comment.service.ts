import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BACKEND_BASE_URL } from '../utils/constants';
import { UserContextService } from '../shared/context/user-context.service';
import { ProgramCommentRequest } from '../interfaces/requests/program-comment-request';
import { ProgramCommentResponse } from '../interfaces/responses/program-comment-response';

@Injectable({
  providedIn: 'root',
})
export class ProgramCommentService {
  private PROGRAMS_URL = `${BACKEND_BASE_URL}/programs`;

  constructor(
    private http: HttpClient,
    private userContext: UserContextService
  ) {}

  public sendCommentOnProgram(programId: number, content: string) {
    let token: string = this.userContext.getToken();
    let userId: number = this.userContext.getUserId();
    let commentRequest: ProgramCommentRequest = {
      userId: userId,
      content: content,
    };
    let commentsEndpoint = `${this.PROGRAMS_URL}/${programId}/comments`;
    return this.http.post<ProgramCommentResponse>(
      commentsEndpoint,
      commentRequest,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
  }

  public getAllProgramComments(programId: number) {
    let commentsEndpoint = `${this.PROGRAMS_URL}/${programId}/comments`;
    return this.http.get<ProgramCommentResponse[]>(commentsEndpoint);
  }
}

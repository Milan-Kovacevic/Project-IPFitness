import { Injectable } from '@angular/core';
import { BACKEND_BASE_URL } from '../utils/constants';
import { HttpClient } from '@angular/common/http';
import { UserContextService } from '../shared/context/user-context.service';
import { MessageSenderResponse } from '../interfaces/responses/message-sender-response';
import { ChatMessageResponse } from '../interfaces/responses/chat-message-response';
import { MessageSendRequest } from '../interfaces/requests/message-send-request';

@Injectable({
  providedIn: 'root',
})
export class TextMessageService {
  private MESSAGES_URL = `${BACKEND_BASE_URL}/messages`;
  private USERS_URL = `${BACKEND_BASE_URL}/users`;

  constructor(
    private http: HttpClient,
    private userContext: UserContextService
  ) {}

  public getAllMessageChats() {
    let msgChatsEndpoint = `${
      this.USERS_URL
    }/${this.userContext.getUserId()}/chats`;
    return this.http.get<MessageSenderResponse[]>(msgChatsEndpoint, {
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }

  public getAllChatMessages(chatUserId: number) {
    let loggedUserId = this.userContext.getUserId();
    let chatMessagesEndpoint = `${this.USERS_URL}/${loggedUserId}/chats/${chatUserId}`;

    return this.http.get<ChatMessageResponse[]>(chatMessagesEndpoint, {
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }

  public discoverChatForUsername(username: string) {
    let userId = this.userContext.getUserId();
    let chatEndpoint = `${this.USERS_URL}/${userId}/chats/discover?username=${username}`;

    return this.http.get<MessageSenderResponse>(chatEndpoint, {
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }

  public sendMessageToUser(request: MessageSendRequest) {
    return this.http.post<ChatMessageResponse>(this.MESSAGES_URL, request, {
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }
}

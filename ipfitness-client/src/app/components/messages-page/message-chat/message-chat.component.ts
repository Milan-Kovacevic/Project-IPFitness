import { Component } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { MessageSenderResponse } from '../../../interfaces/responses/message-sender-response';
import { ChatListSelectorComponent } from './chat-list-selector/chat-list-selector.component';
import { BlockableDivComponent } from '../../../shared/components/blockable-div/blockable-div.component';
import { UserContextService } from '../../../shared/context/user-context.service';
import { ChatMessageResponse } from '../../../interfaces/responses/chat-message-response';
import { TextMessageService } from '../../../services/text-message.service';
import { firstValueFrom } from 'rxjs';
import { MessageService } from 'primeng/api';
import { BACKEND_BASE_URL } from '../../../utils/constants';
import { MessageSendRequest } from '../../../interfaces/requests/message-send-request';
import { DirectChatSkeletonComponent } from './direct-chat-skeleton/direct-chat-skeleton.component';

@Component({
  selector: 'app-message-chat',
  standalone: true,
  imports: [
    PrimeNgModule,
    ChatListSelectorComponent,
    BlockableDivComponent,
    DirectChatSkeletonComponent,
  ],
  providers: [MessageService],
  templateUrl: './message-chat.component.html',
  styleUrl: './message-chat.component.css',
})
export class MessageChatComponent {
  currentUserId: number;
  currentUserAvatar: string;
  hasErrors: boolean = false;
  isLoadingChat: boolean = false;
  isLoadingList: boolean = false;
  chatMessages: ChatMessageResponse[] = [];
  otherUser: MessageSenderResponse | undefined;
  messageText: string = '';

  constructor(
    userContext: UserContextService,
    private textMessageService: TextMessageService,
    private messageService: MessageService
  ) {
    this.hasErrors = !userContext.isActivated();
    this.currentUserId = userContext.getUserId();
    this.currentUserAvatar = `${BACKEND_BASE_URL}/storage?fileName=${
      userContext.getUser()?.avatar
    }`;
  }

  onHasErrors() {
    setTimeout(() => {
      this.isLoadingChat = false;
      this.isLoadingList = false;
      this.hasErrors = true;
    }, 1000);
  }

  onIsLoadingListChanged(value: boolean) {
    this.isLoadingList = value;
  }

  onOpenDirectChat(user: MessageSenderResponse) {
    this.isLoadingChat = true;
    this.otherUser = user;
    firstValueFrom(this.textMessageService.getAllChatMessages(user.userId))
      .then((response) => {
        this.chatMessages = response;
      })
      .catch((_) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Unable to load message chats. Please, try again later',
        });
      })
      .finally(() => setTimeout(() => (this.isLoadingChat = false), 1000));
  }

  sendMessage() {
    if (this.messageText.trim() === '') return;

    let request: MessageSendRequest = {
      content: this.messageText,
      userFromId: this.currentUserId,
      userToId: this.otherUser?.userId ?? 0,
    };
    this.isLoadingChat = true;
    firstValueFrom(this.textMessageService.sendMessageToUser(request))
      .then((response) => {
        this.chatMessages.unshift(response);
        this.messageText = '';
      })
      .catch((_) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Unable to send message. Please, try again later',
        });
      })
      .finally(() => setTimeout(() => (this.isLoadingChat = false), 10));
  }

  dispalyMessageDate(dateStr: string): string {
    let date = new Date(dateStr);
    const currentDate = new Date();
    const timeDifference = currentDate.getTime() - date.getTime();
    const daysDifference = Math.floor(timeDifference / (1000 * 3600 * 24));

    if (daysDifference > 5) {
      // If date is older than 5 days
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      const formattedDate = `${hours}:${minutes}, ${date.toLocaleDateString(
        'en-US',
        { day: '2-digit', month: '2-digit', year: 'numeric' }
      )}`;
      return formattedDate;
    } else {
      // If date is 5 days old or newer
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      const dayOfWeek = date.toLocaleDateString('en-US', { weekday: 'long' });
      const formattedDate = `${hours}:${minutes}, ${dayOfWeek}`;
      return formattedDate;
    }
  }
}

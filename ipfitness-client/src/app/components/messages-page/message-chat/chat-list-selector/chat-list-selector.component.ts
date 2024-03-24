import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';
import { MessageSenderResponse } from '../../../../interfaces/responses/message-sender-response';
import { MessageService } from 'primeng/api';
import { TextMessageService } from '../../../../services/text-message.service';
import { firstValueFrom } from 'rxjs';
import { BACKEND_BASE_URL } from '../../../../utils/constants';
import { ListboxChangeEvent } from 'primeng/listbox';
import { ChatListSelectorSkeletonComponent } from '../chat-list-selector-skeleton/chat-list-selector-skeleton.component';

@Component({
  selector: 'chat-list-selector',
  standalone: true,
  imports: [PrimeNgModule, ChatListSelectorSkeletonComponent],
  providers: [MessageService],
  templateUrl: './chat-list-selector.component.html',
  styleUrl: './chat-list-selector.component.css',
})
export class ChatListSelectorComponent implements OnInit {
  @Output() onHasError = new EventEmitter<void>();
  @Output() onUserSelected = new EventEmitter<MessageSenderResponse>();

  isLoading: boolean = false;
  @Output() isLoadingChanged = new EventEmitter<boolean>();

  searchText: string = '';
  listUsers: MessageSenderResponse[] = [];
  messageChats: MessageSenderResponse[] = [];
  public hasErrors: boolean = false;

  constructor(
    private textMessageService: TextMessageService,
    private messageService: MessageService
  ) {}

  setIsLoading(value: boolean) {
    this.isLoading = value;
    this.isLoadingChanged.emit(this.isLoading);
  }

  ngOnInit(): void {
    this.setIsLoading(true);
    firstValueFrom(this.textMessageService.getAllMessageChats())
      .then((response) => {
        this.messageChats = response.map((e) => {
          e.avatar = `${BACKEND_BASE_URL}/storage?fileName=${e.avatar}`;
          return e;
        });
        this.listUsers = [...this.messageChats];
        setTimeout(() => this.setIsLoading(false), 1000);
      })
      .catch(() => {
        setTimeout(() => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Unable to load message chats. Please, try again later',
          });
          this.onHasError.emit();
          setTimeout(() => this.setIsLoading(false), 1000);
        }, 900);
      });
  }

  public discoverMessageUser() {
    this.setIsLoading(true);
    firstValueFrom(
      this.textMessageService.discoverChatForUsername(this.searchText)
    )
      .then((response) => {
        response.avatar = `${BACKEND_BASE_URL}/storage?fileName=${response.avatar}`;
        this.messageChats.push(response);
      })
      .then(() => {
        this.searchText = '';
        this.listUsers = [...this.messageChats];
      })
      .catch(() => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: `Unable to find user with username '${this.searchText}'`,
        });
      })
      .finally(() => {
        setTimeout(() => this.setIsLoading(false), 1000);
      });
  }

  public onListItemChange(event: ListboxChangeEvent) {
    let messageSender: MessageSenderResponse | undefined =
      event.value as MessageSenderResponse;
    if (messageSender !== undefined) this.onUserSelected.emit(messageSender);
  }

  public clearMessageChatFilters() {
    if (this.searchText === '') {
      this.listUsers = [...this.messageChats];
    }
  }

  public filterMessageChats() {
    this.listUsers = [
      ...this.messageChats.filter((u) => {
        return u.username.includes(this.searchText);
      }),
    ];
  }
}

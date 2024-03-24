import { Component, OnInit } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { FooterComponent } from '../../../shared/components/footer/footer.component';
import { MessageService } from 'primeng/api';
import { MessageChatComponent } from '../message-chat/message-chat.component';
import { QuestionService } from '../../../services/question.service';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { ContactAdvisorComponent } from '../contact-advisor/contact-advisor.component';

@Component({
  selector: 'app-messages',
  standalone: true,
  imports: [
    PrimeNgModule,
    FooterComponent,
    MessageChatComponent,
    ContactAdvisorComponent,
    ReactiveFormsModule,
  ],
  providers: [MessageService],
  templateUrl: './messages.component.html',
  styleUrl: './messages.component.css',
})
export class MessagesComponent {
  isModalOpen: boolean = false;

  setModalOpen(isOpen: boolean) {
    this.isModalOpen = isOpen;
  }
}

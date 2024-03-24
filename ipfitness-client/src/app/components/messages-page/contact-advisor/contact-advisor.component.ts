import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { firstValueFrom } from 'rxjs';
import { QuestionService } from '../../../services/question.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-contact-advisor',
  standalone: true,
  imports: [PrimeNgModule, ReactiveFormsModule],
  templateUrl: './contact-advisor.component.html',
  styleUrl: './contact-advisor.component.css',
})
export class ContactAdvisorComponent implements OnInit {
  @Input({required: true}) isModalOpen: boolean = false;
  @Output() isModalOpenChanged = new EventEmitter<boolean>();
  isLoading: boolean = false;
  questionForm: FormGroup = new FormGroup({});

  constructor(
    private questionService: QuestionService,
    private formBuilder: FormBuilder,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
     this.questionForm = this.formBuilder.group({
      content: ['', Validators.required],
    });
  }

  onCloseModal(isOpen: boolean) {
    this.isModalOpenChanged.emit(isOpen);
  }

  sendQuestionToAdvisor() {
    this.isLoading = true;
    firstValueFrom(
      this.questionService.sendQuestion(
        this.questionForm.controls['content'].value
      )
    )
      .then(() => {
        this.onCloseModal(false);
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Question sent successfully. Check you e-mail for response',
        });
      })
      .catch((_) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail:
            'Unable to send question to out advisors. Please, try again later.',
        });
      })
      .finally(() => (this.isLoading = false));
  }
}

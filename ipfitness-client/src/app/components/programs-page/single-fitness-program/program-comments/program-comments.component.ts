import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';
import { ProgramDetailsResponse } from '../../../../interfaces/responses/program-details-response';
import { ProgramCommentResponse } from '../../../../interfaces/responses/program-comment-response';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ProgramCommentService } from '../../../../services/program-comment.service';
import { MessageService } from 'primeng/api';
import { UserContextService } from '../../../../shared/context/user-context.service';
import { ProgramCommentsSkeletonComponent } from '../program-comments-skeleton/program-comments-skeleton.component';

@Component({
  selector: 'app-program-comments',
  standalone: true,
  imports: [
    PrimeNgModule,
    ReactiveFormsModule,
    ProgramCommentsSkeletonComponent,
  ],
  templateUrl: './program-comments.component.html',
  styleUrl: './program-comments.component.css',
})
export class ProgramCommentsComponent implements OnInit {
  @Input({ required: true }) comments!: ProgramCommentResponse[];
  @Input({ required: true }) program!: ProgramDetailsResponse | null;
  @Input({ required: true }) isLoading!: boolean;
  @Output() onCommentPost = new EventEmitter<ProgramCommentResponse>();
  hasErrors: boolean = false;
  isAccountActivated: boolean = false;
  commentForm: FormGroup = new FormGroup({});

  constructor(
    private formBuilder: FormBuilder,
    private programCommentService: ProgramCommentService,
    private messageService: MessageService,
    userContext: UserContextService
  ) {
    this.isAccountActivated = userContext.isActivated();
  }

  ngOnInit(): void {
    this.commentForm = this.formBuilder.group({
      comment: ['', Validators.required],
    });
  }

  onCommentSubmit() {
    let content: string = this.commentForm.controls['comment'].value;
    let programId: number | null = this.program?.programId ?? null;

    if (content.trim() === '' || programId === null) return;

    this.programCommentService
      .sendCommentOnProgram(programId, content)
      .subscribe({
        next: (response) => {
          this.onCommentPost.emit(response);
          this.commentForm.reset();
        },
        error: (err) => {
          this.hasErrors = true;
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Unable to send comment. Please, try again later',
          });
        },
      });
  }
}

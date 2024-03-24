import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivityAddRequest } from '../../../interfaces/requests/activity-add-request';

@Component({
  selector: 'app-activity-modal',
  standalone: true,
  imports: [PrimeNgModule, ReactiveFormsModule],
  templateUrl: './activity-modal.component.html',
  styleUrl: './activity-modal.component.css',
})
export class ActivityModalComponent implements OnInit {
  @Input({ required: true }) isOpen!: boolean;
  @Output() isOpenChanged = new EventEmitter<boolean>();
  @Output() modalSubmited = new EventEmitter<ActivityAddRequest>();
  activityForm: FormGroup = new FormGroup({});

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.activityForm = this.formBuilder.group({
      activityType: ['', Validators.required],
      trainingDuration: [null, [Validators.required, Validators.min(0)]],
      percentageCompleted: [
        null,
        [Validators.required, Validators.min(0), Validators.max(100)],
      ],
      result: [null, [Validators.required, Validators.min(0)]],
      summary: [''],
    });
  }

  onCloseModal(isOpen: boolean) {
    this.isOpenChanged.emit(isOpen);
  }

  onModalSubmit() {
    let request = this.activityForm.value as ActivityAddRequest;
    this.modalSubmited.emit(request);
  }
}

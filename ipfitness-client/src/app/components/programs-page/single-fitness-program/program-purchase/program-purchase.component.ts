import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { EnumItem } from '../../../../models/enum-item';
import { paymentTypes } from '../../../../utils/constants';
import { DropdownChangeEvent } from 'primeng/dropdown';
import { PaymentItem } from '../../../../models/payment-item';
import { ProgramDetailsResponse } from '../../../../interfaces/responses/program-details-response';
import { ProgramPurchaseService } from '../../../../services/program-purchase.service';

@Component({
  selector: 'app-program-purchase',
  standalone: true,
  imports: [PrimeNgModule, ReactiveFormsModule],
  templateUrl: './program-purchase.component.html',
  styleUrl: './program-purchase.component.css',
})
export class ProgramPurchaseComponent implements OnInit {
  @Input({ required: true }) program!: ProgramDetailsResponse | null;
  @Input({required: true}) programOverviewPicture!: string;
  @Input({ required: true }) isModalOpen: boolean = false;
  @Output() isModalOpenChanged = new EventEmitter<boolean>();
  @Output() onProgramPurchase = new EventEmitter<boolean>();
  isLoading: boolean = false;
  purchaseForm: FormGroup = new FormGroup({});
  paymentType: PaymentItem[] = [];
  
  selectedPaymentType: PaymentItem | undefined;

  constructor(
    private formBuilder: FormBuilder,
    private programPurchaseService: ProgramPurchaseService
  ) {}

  ngOnInit(): void {
    this.purchaseForm = this.formBuilder.group({
      payment: [undefined, Validators.required],
      cardNumber: ['', [Validators.required]],
    });
    this.paymentType = [...paymentTypes];
  }

  changePaymentType(event: DropdownChangeEvent) {
    this.selectedPaymentType = this.paymentType.find(
      (x) => x.key == event.value
    );
    this.purchaseForm = this.formBuilder.group({
      payment: [this.selectedPaymentType?.key, Validators.required],
      cardNumber: [
        '',
        [
          Validators.required,
          Validators.pattern(this.selectedPaymentType?.pattern ?? ''),
        ],
      ],
    });
  }

  onProgramPurchaseSubmit() {
    if (this.program === null || this.selectedPaymentType === undefined) return;
    this.isLoading = true;
    this.programPurchaseService
      .purchaseFitnessProgram(
        this.program.programId,
        this.selectedPaymentType,
        this.purchaseForm.controls['cardNumber'].value
      )
      .subscribe({
        next: () => {
          setTimeout(() => {
            this.isLoading = false;
            this.onProgramPurchase.emit(true);
            this.purchaseForm.reset();
            this.selectedPaymentType = undefined;
          }, 1000);
        },
        error: () => {
          setTimeout(() => {
            this.isLoading = false;
            this.onProgramPurchase.emit(false);
          }, 1000);
        },
      });
  }

  onCloseModal(isOpen: boolean) {
    this.isModalOpenChanged.emit(isOpen);
  }
}

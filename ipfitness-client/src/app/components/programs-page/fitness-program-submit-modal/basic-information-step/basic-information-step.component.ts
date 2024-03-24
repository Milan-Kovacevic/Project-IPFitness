import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { SingleFitnessProgramResponse } from '../../../../interfaces/responses/single-fitness-program-response';
import { EnumItem } from '../../../../models/enum-item';

@Component({
  selector: 'app-basic-information-step',
  standalone: true,
  imports: [PrimeNgModule, ReactiveFormsModule],
  templateUrl: './basic-information-step.component.html',
  styleUrl: './basic-information-step.component.css',
})
export class BasicInformationStepComponent implements OnInit {
  @Input({ required: true }) locations!: EnumItem[];
  @Input({ required: true }) difficulties!: EnumItem[];
  @Output() onValidityChanges = new EventEmitter<boolean>();
  @Output() onLocationChanges = new EventEmitter<string>();
  
  basicInfoForm: FormGroup = new FormGroup({});
  minimalDate: Date = new Date();
  maximalDate: Date = new Date(2040, 1, 1);
  private isFormValid: boolean = false;
  private selectedLocation: string | undefined;
  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.basicInfoForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      location: [undefined, [Validators.required]],
      difficulty: [undefined, [Validators.required]],
      description: [''],
      price: [0, [Validators.required]],
      dateRange: [null, [Validators.required]],
    });

    this.basicInfoForm.valueChanges.subscribe({
      next: (value) => {
        // Form validity changes...
        if (this.isFormValid !== this.basicInfoForm.valid) {
          this.onValidityChanges.emit(this.basicInfoForm.valid);
        }
        this.isFormValid = this.basicInfoForm.valid;

        // Location changes...
        if (value.location != this.selectedLocation) {
          this.onLocationChanges.emit(value.location);
        }
        this.selectedLocation = value.location;
      },
    });
  }

  clear() {
    this.basicInfoForm.reset();
  }

  public setInitialBasicInformations(program: SingleFitnessProgramResponse) {
    let startDate = new Date(program.startDate);
    let endDate = new Date(program.endDate);
    let today = new Date();
    this.minimalDate = startDate < today ? startDate : today;
    this.basicInfoForm.setValue({
      name: program.name,
      location: program.location,
      difficulty: program.difficulty,
      description: program.description,
      price: program.price,
      dateRange: [startDate, endDate],
    });
  }
}

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CategoryResponse } from '../../../../interfaces/responses/category-response';
import { DropdownChangeEvent } from 'primeng/dropdown';
import { CategoryAttributeResponse } from '../../../../interfaces/responses/category-attribute-response';
import { CategoryService } from '../../../../services/category.service';
import { SingleFitnessProgramResponse } from '../../../../interfaces/responses/single-fitness-program-response';

@Component({
  selector: 'app-category-select-step',
  standalone: true,
  imports: [PrimeNgModule, ReactiveFormsModule],
  templateUrl: './category-select-step.component.html',
  styleUrl: './category-select-step.component.css',
})
export class CategorySelectStepComponent implements OnInit {
  @Input({ required: true }) categories!: CategoryResponse[];

  public categoriesForm: FormGroup = new FormGroup({});
  selectedCategory: CategoryResponse | undefined;
  categoryAttributes: CategoryAttributeResponse[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private categoryService: CategoryService
  ) {}

  ngOnInit(): void {
    this.categoriesForm = this.formBuilder.group({
      category: [undefined, Validators.required],
    });
  }

  setInitialCategoryInformations(response: SingleFitnessProgramResponse) {
    this.selectedCategory = this.categories.find(
      (c) => c.categoryId === response.categoryId
    );
    if (this.selectedCategory === undefined) return;

    this.categoryService
      .getCategoryAttributes(this.selectedCategory.categoryId)
      .subscribe({
        next: (result) => {
          this.categoryAttributes = result;

          this.categoriesForm = this.formBuilder.group({
            category: [this.selectedCategory, Validators.required],
          });
          for (let i = 0; i < this.categoryAttributes.length; i++) {
            let attributeValue: string =
              response.attributeValues.find(
                (a) => a.attributeId == this.categoryAttributes[i].attributeId
              )?.value ?? '';
            this.categoriesForm.addControl(
              this.categoryAttributes[i].name,
              this.formBuilder.control(attributeValue, Validators.required)
            );
          }
        },
        error: (err) => {},
      });
  }

  clear() {
    this.categoriesForm.reset();
  }

  onCategoryChange(event: DropdownChangeEvent) {
    if (event.value) {
      this.selectedCategory = event.value as CategoryResponse;
      this.loadCategoryAttributes();
    } else {
      this.selectedCategory = undefined;
      this.categoryAttributes = [];
      this.updateCategoriesForm();
    }
  }

  private loadCategoryAttributes() {
    if (!this.selectedCategory) return;

    this.categoryService
      .getCategoryAttributes(this.selectedCategory.categoryId)
      .subscribe({
        next: (result) => {
          this.categoryAttributes = result;
          this.updateCategoriesForm();
        },
        error: (err) => {},
      });
  }

  private updateCategoriesForm() {
    this.categoriesForm = this.formBuilder.group({
      category: [this.selectedCategory, Validators.required],
    });

    this.categoryAttributes.forEach((attribute) => {
      this.categoriesForm.addControl(
        attribute.name,
        this.formBuilder.control('', Validators.required)
      );
    });
  }
}

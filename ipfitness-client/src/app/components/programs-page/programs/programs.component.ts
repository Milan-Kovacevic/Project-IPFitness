import { Component, OnInit } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { FooterComponent } from '../../../shared/components/footer/footer.component';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { FitnessProgramCardComponent } from '../fitness-program-card/fitness-program-card.component';
import { PaginatorState } from 'primeng/paginator';
import { FitnessProgramResponse } from '../../../interfaces/responses/fitness-program-response';
import { FitnessProgramService } from '../../../services/fitness-program.service';
import { PageRequest } from '../../../interfaces/requests/page-request';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CategoryService } from '../../../services/category.service';
import { CategoryResponse } from '../../../interfaces/responses/category-response';
import { UserContextService } from '../../../shared/context/user-context.service';
import { ConfirmDialogComponent } from '../../../shared/components/confirm-dialog/confirm-dialog.component';
import { FitnessProgramSubmitModalComponent } from '../fitness-program-submit-modal/fitness-program-submit-modal.component';
import {
  additionalOptions,
  programDifficulties,
  programLocations,
  programStates,
} from '../../../utils/constants';
import { EnumItem } from '../../../models/enum-item';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-programs',
  standalone: true,
  imports: [
    PrimeNgModule,
    FooterComponent,
    FitnessProgramCardComponent,
    ReactiveFormsModule,
    ConfirmDialogComponent,
    FitnessProgramSubmitModalComponent,
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: './programs.component.html',
  styleUrl: './programs.component.css',
})
export class ProgramsComponent implements OnInit {
  public isAccountActivated: boolean = false;
  public isLoading: boolean = false;
  public isModalOpen: boolean = false;
  public programId: number | undefined;

  locations: EnumItem[] = [];
  difficulties: EnumItem[] = [];
  states: EnumItem[] = [];
  categories: CategoryResponse[] = [];
  sortItems: MenuItem[] = [];
  // Additional options that get logged users
  options: EnumItem[] = [];

  public filterForm: FormGroup = new FormGroup({});
  public selectedSorting: string | undefined;
  public searchQuery: string | undefined;
  public showUserPrograms: boolean = false;

  public fitnessPrograms: FitnessProgramResponse[] | undefined;
  public rowsPerPageOptions: number[] = [4, 8, 16, 24];
  private DEFAULT_PAGE_SIZE: number = 4;

  public first: number = 0; // first element index in entire array
  public rows: number = this.DEFAULT_PAGE_SIZE; // number of elements per page
  public page: number = 0; // current page
  public totalRecords: number = 0; // total records for a given filters&search

  constructor(
    private fitnessProgramsService: FitnessProgramService,
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private router: Router,
    private categoryService: CategoryService,
    private confirmationService: ConfirmationService,
    userContext: UserContextService
  ) {
    this.isAccountActivated = userContext.isActivated();
  }

  private initializeSortMenuItems() {
    this.sortItems = [
      {
        label: 'Options',
        items: [
          {
            label: 'Price Ascending',
            icon: 'pi pi-sort-numeric-down',
            id: '1',
            command: () => {
              this.onPriceSort(true);
            },
          },
          {
            label: 'Price Descending',
            icon: 'pi pi-sort-numeric-down-alt',
            id: '2',
            command: () => {
              this.onPriceSort(false);
            },
          },
          {
            label: 'Program Name Ascending',
            icon: 'pi pi-sort-alpha-down',
            id: '3',
            command: () => {
              this.onNameSort(true);
            },
          },
          {
            label: 'Program Name Descending',
            icon: 'pi pi-sort-alpha-down-alt',
            id: '4',
            command: () => {
              this.onNameSort(false);
            },
          },
        ],
      },
    ];
  }

  ngOnInit(): void {
    // Initializing filter components
    this.locations = [...programLocations];
    this.difficulties = [...programDifficulties];
    this.states = [...programStates];
    this.options = [...additionalOptions];
    firstValueFrom(this.categoryService.getAllCategories())
      .then((response) => {
        this.categories = response;
      })
      .then(() => {
        this.route.queryParams.subscribe((params) => {
          if (params['category']) {
            let categoryId = params['category'];
            let selectedCategory = this.categories?.find(
              (e) => e.categoryId == categoryId
            );
            this.filterForm.controls['category'].setValue(
              selectedCategory?.categoryId
            );
          }
        });
      })
      .catch((err) => {});
    this.initializeSortMenuItems();

    // Building initial filter form
    this.filterForm = this.formBuilder.group({
      category: [undefined],
      location: [undefined],
      difficulty: [undefined],
      state: [this.states.length > 0 ? this.states[0].key : undefined],
      option: [undefined],
    });

    // Initializing filters with query path variables
    this.route.queryParams.subscribe((params) => {
      if (this.isAccountActivated && params['add']) {
        this.programId = undefined;
        this.isModalOpen = true;
      } else if (
        this.isAccountActivated &&
        params['edit'] &&
        params['programId']
      ) {
        this.programId = params['programId'];
        this.isModalOpen = true;
      }

      if (params['page']) {
        this.page = params['page'];
      }
      if (params['size']) {
        this.rows = params['size'];
      }
      if (params['search']) {
        this.searchQuery = params['search'];
      }
      if (params['location']) {
        let selectedLocation: EnumItem | undefined = this.locations.find(
          (x) => x.key == params['location']
        );
        this.filterForm.controls['location'].setValue(selectedLocation?.key);
      }
      if (params['difficulty']) {
        let selectedDifficulty: EnumItem | undefined = this.difficulties.find(
          (x) => x.key == params['difficulty']
        );
        this.filterForm.controls['difficulty'].setValue(
          selectedDifficulty?.key
        );
      }
      if (params['option'] && this.isAccountActivated) {
        let selectedOption: EnumItem | undefined = this.options.find(
          (x) => x.key == params['option']
        );
        this.filterForm.controls['option'].setValue(selectedOption?.key);
      }
      if (params['state']) {
        let selectedState: EnumItem | undefined = this.states.find(
          (x) => x.key == params['state']
        );
        this.filterForm.controls['state'].setValue(selectedState?.key);
      }
      if (params['sort']) {
        this.selectedSorting = params['sort'];
      }
    });

    // Loading fitness program based upon filters
    this.isLoading = true;
    setTimeout(() => {
      this.loadFitnessPrograms();
    }, 1000);
  }

  public onPageChange(event: PaginatorState) {
    let size = event.rows ?? this.DEFAULT_PAGE_SIZE;
    let page = event.page ?? 0;
    this.first = size * page;
    this.rows = size;
    this.page = page;
    this.loadFitnessPrograms();
  }

  public onProgramSearch() {
    this.loadFitnessPrograms();
  }

  private onPriceSort(isAscending: boolean) {
    if (isAscending) this.selectedSorting = 'price,ASC';
    else this.selectedSorting = 'price,DESC';
    this.loadFitnessPrograms();
  }

  private onNameSort(isAscending: boolean) {
    if (isAscending) this.selectedSorting = 'name,ASC';
    else this.selectedSorting = 'name,DESC';
    this.loadFitnessPrograms();
  }

  public onClearFilters() {
    this.filterForm.reset();
    this.selectedSorting = undefined;
    this.searchQuery = '';
    this.loadFitnessPrograms();
  }

  public onApplyFilters() {
    this.loadFitnessPrograms();
  }

  public showConfirmDeleteDialog(program: FitnessProgramResponse | undefined) {
    this.confirmationService.confirm({
      accept: () => {
        this.deleteFitnessProgram(program?.programId);
      },
    });
  }

  public navigateToAddProgram() {
    this.router.navigate(['/programs'], {
      queryParams: { add: 'true' },
    });
  }

  public navigateToEditProgram(programId: number) {
    this.router.navigate(['/programs'], {
      queryParams: { edit: 'true', programId: programId },
    });
  }

  public navigateToProgramDetails(programId: number) {
    this.router.navigate([`/programs/${programId}`]);
  }

  public onProgramSubmit(isEdit: boolean) {
    this.isModalOpen = false;
    this.router.navigate(['/programs']).then(() => {
      // Refresh view...
      setTimeout(() => location.reload(), 1000);
    });
  }

  public onCloseProgramModal() {
    this.isModalOpen = false;
    this.router.navigate(['/programs']);
  }

  private deleteFitnessProgram(programId: number | undefined) {
    this.fitnessProgramsService.deleteFitnessProgram(programId ?? 0).subscribe({
      next: (_) => {
        this.loadFitnessPrograms();
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Fitness program deleted successfully!',
        });
      },
      error: (_) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail:
            'Unable to delete selected fitness program. Please, try again',
        });
      },
    });
  }

  private loadFitnessPrograms() {
    this.isLoading = true;

    let pageRequest: PageRequest = {
      page: this.page,
      size: this.rows,
      searchQuery: this.searchQuery,
      locationFilter: this.filterForm.controls['location'].value,
      difficultyFilter: this.filterForm.controls['difficulty'].value,
      categoryFilter: this.filterForm.controls['category'].value,
      stateFilter: this.filterForm.controls['state'].value,
      additionalOption: this.filterForm.controls['option'].value,
      sort: this.selectedSorting,
    };
    this.fitnessProgramsService
      .getAllPrograms(pageRequest)
      .subscribe({
        next: (response) => {
          this.fitnessPrograms = response.content.map(
            (e: FitnessProgramResponse) => {
              e.location =
                programLocations.find((l) => l.key == e.location)
                  ?.displayName ?? e.location;
              e.difficulty =
                programDifficulties.find((d) => d.key == e.difficulty)
                  ?.displayName ?? e.difficulty;
              return e;
            }
          );
          this.first = response.size * response.number;
          this.rows = response.size;
          this.page = response.number;
          this.totalRecords = response.totalElements;
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Unexpected error',
            detail: 'Unable to load fitness programs. Please, try again later',
          });
        },
      })
      .add(() => {
        this.isLoading = false;
      });

    // Add necessary query params, so appropriate filters can be applied on next page reload...
    if (!this.router.url.startsWith('/programs')) return;

    if (this.router.url.includes('add')) {
      this.navigateToAddProgram();
    } else if (this.router.url.includes('edit')) {
      let programId = 0;
      this.route.queryParams.subscribe((params) => {
        if (params['edit'] && params['programId']) {
          programId = params['programId'];
          this.navigateToEditProgram(programId);
        }
      });
    } else {
      this.router.navigate(['/programs'], {
        queryParams: {
          page: this.page,
          size: this.rows,
          search: this.searchQuery,
          location: this.filterForm.controls['location'].value,
          difficulty: this.filterForm.controls['difficulty'].value,
          category: this.filterForm.controls['category'].value,
          state: this.filterForm.controls['state'].value,
          option: this.isAccountActivated
            ? this.filterForm.controls['option'].value
            : undefined,
          sort: this.selectedSorting,
        },
      });
    }
  }
}

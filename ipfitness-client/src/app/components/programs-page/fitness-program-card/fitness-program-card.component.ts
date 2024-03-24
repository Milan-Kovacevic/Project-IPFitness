import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { FitnessProgramResponse } from '../../../interfaces/responses/fitness-program-response';
import { UserContextService } from '../../../shared/context/user-context.service';

@Component({
  selector: 'app-fitness-program-card',
  standalone: true,
  imports: [PrimeNgModule],
  templateUrl: './fitness-program-card.component.html',
  styleUrl: './fitness-program-card.component.css',
})
export class FitnessProgramCardComponent implements OnInit {
  @Input() program?: FitnessProgramResponse;
  @Output('onProgramDelete') onProgramDelete = new EventEmitter<
    FitnessProgramResponse | undefined
  >();
  @Output('onProgramEdit') onProgramEdit = new EventEmitter<number>();
  @Output('onViewProgramDetails') onViewProgramDetails = new EventEmitter<number>();

  constructor(private userContext: UserContextService) {}

  public images: any[] | undefined;
  private BASE_IMAGE_URL = 'http://localhost:9001/api/v1/storage?fileName=';
  private DEFAULT_FITNESS_PROGRAM_IMAGE1 = 'assets/default-fitness-program.png';
  private DEFAULT_FITNESS_PROGRAM_IMAGE2 = 'assets/default-fitness-program.jpg';
  isOwner: boolean = false;

  ngOnInit(): void {
    this.isOwner = this.program?.userId == this.userContext.getUserId();
    if (
      this.program?.images?.length != undefined &&
      this.program?.images?.length > 0
    )
      this.images = this.program?.images?.map(
        (img) => this.BASE_IMAGE_URL + img
      );
    else
      this.images = [
        this.DEFAULT_FITNESS_PROGRAM_IMAGE1,
        this.DEFAULT_FITNESS_PROGRAM_IMAGE2,
      ];
  }

  deleteFitnessProgram() {
    this.onProgramDelete.emit(this.program);
  }

  updateFitnessProgram() {
    this.onProgramEdit.emit(this.program?.programId);
  }

  openFitnessProgramDetails(){
    this.onViewProgramDetails.emit(this.program?.programId);
  }
}

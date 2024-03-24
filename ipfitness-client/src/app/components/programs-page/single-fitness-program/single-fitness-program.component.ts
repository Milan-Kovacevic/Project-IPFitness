import { Component, OnInit } from '@angular/core';
import { PrimeNgModule } from '../../../shared/prime-ng/prime-ng.module';
import { ProgramCommentsComponent } from './program-comments/program-comments.component';
import { ProgramDetailsComponent } from './program-details/program-details.component';
import { ProgramPurchaseComponent } from './program-purchase/program-purchase.component';
import { FitnessProgramService } from '../../../services/fitness-program.service';
import { MessageService } from 'primeng/api';
import { ActivatedRoute, Router } from '@angular/router';
import { ProgramDetailsResponse } from '../../../interfaces/responses/program-details-response';
import { firstValueFrom } from 'rxjs';
import {
  BACKEND_BASE_URL,
  ONLINE_LOCATION,
  programDifficulties,
  programLocations,
} from '../../../utils/constants';
import { HttpErrorResponse } from '@angular/common/http';
import { ProgramCommentService } from '../../../services/program-comment.service';
import { ProgramCommentResponse } from '../../../interfaces/responses/program-comment-response';
import { TabViewChangeEvent } from 'primeng/tabview';
import { UserContextService } from '../../../shared/context/user-context.service';
import { ProgramParticipationComponent } from './program-participation/program-participation.component';

@Component({
  selector: 'app-single-fitness-program',
  standalone: true,
  imports: [
    PrimeNgModule,
    ProgramCommentsComponent,
    ProgramDetailsComponent,
    ProgramPurchaseComponent,
    ProgramParticipationComponent,
  ],
  providers: [MessageService],
  templateUrl: './single-fitness-program.component.html',
  styleUrl: './single-fitness-program.component.css',
})
export class SingleFitnessProgramComponent implements OnInit {
  private PICTURES_URL = `${BACKEND_BASE_URL}/storage?fileName=`;
  private DEFAULT_FITNESS_PROGRAM_IMAGE = 'assets/article-heading-img.png';
  private DEFAULT_PROGRAM_OWNER_AVATAR = 'assets/default-avatar.png';
  isAccountActivated: boolean = false;
  isOwner: boolean = false;
  isPurhcaseModalOpen: boolean = false;
  isParticipationModalOpen: boolean = false;
  isLoadingDetails: boolean = false;
  isLoadingComments: boolean = false;
  isLoadingProgram: boolean = false;
  hasErrors: boolean = false;

  program: ProgramDetailsResponse | undefined;
  programComments: ProgramCommentResponse[] = [];
  programPictures: string[] = [];
  ownerAvatar: string = '';
  programLocation: string = '';
  programDifficulty: string = '';
  programStart: string = '';
  programEnd: string = '';
  isLocationOnline: boolean = false;

  private userId: number = 0;
  constructor(
    private fitnessProgramService: FitnessProgramService,
    private messageService: MessageService,
    private programCommentService: ProgramCommentService,
    private route: ActivatedRoute,
    private router: Router,
    userContext: UserContextService
  ) {
    this.isAccountActivated = userContext.isActivated();
    this.userId = userContext.getUserId();
  }

  ngOnInit(): void {
    this.isLoadingProgram = true;
    firstValueFrom(this.route.params)
      .then((params) => {
        return params['id'];
      })
      .then((id: number | null) => {
        if (id !== undefined && id !== null) {
          this.isLoadingDetails = true;
          this.fitnessProgramService
            .getFitnessProgramDetails(id)
            .subscribe({
              next: (response) => {
                this.program = response;
                this.isOwner = response.ownerId == this.userId;
                if (response.pictures.length > 0) {
                  response.pictures.forEach((p) => {
                    this.programPictures.push(`${this.PICTURES_URL}${p}`);
                  });
                } else {
                  this.programPictures.push(this.DEFAULT_FITNESS_PROGRAM_IMAGE);
                }
                this.ownerAvatar = response.ownerAvatar
                  ? `${this.PICTURES_URL}${response.ownerAvatar}`
                  : this.DEFAULT_PROGRAM_OWNER_AVATAR;

                this.programLocation =
                  programLocations.find((l) => l.key === response.location)
                    ?.displayName ?? response.location;
                this.programDifficulty =
                  programDifficulties.find((d) => d.key === response.difficulty)
                    ?.displayName ?? response.difficulty;

                let startDate: Date = new Date(response.startDate);
                let endDate: Date = new Date(response.endDate);
                this.programStart = `${startDate.toDateString()}`;
                this.programEnd = `${endDate.toDateString()}`;

                this.isLocationOnline = response.location === ONLINE_LOCATION;
              },
              error: (err) => {
                let httpErr = err as HttpErrorResponse;
                if (httpErr && httpErr.status === 404) {
                  this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: 'Fitness program is not available.',
                  });
                } else {
                  this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail:
                      'Unable to load fitness program details. Please, try again later',
                  });
                }
                this.hasErrors = true;
              },
            })
            .add(() => {
              setTimeout(() => {
                this.isLoadingDetails = false;
                this.isLoadingProgram = false;
              }, 1000);
            });
          this.loadProgramComments(id);
        }
      });
  }

  openPurhcaseModal() {
    this.isPurhcaseModalOpen = true;
  }

  onIsPurhcaseModalOpenChanged(value: boolean) {
    this.isPurhcaseModalOpen = value;
  }

  onFitnessProgramPurchase(value: boolean) {
    if (value === true) {
      this.messageService.add({
        severity: 'success',
        summary: 'Thank you',
        detail: 'Fitness program purchased successfully!',
      });
      this.isPurhcaseModalOpen = false;
      setTimeout(() => location.reload(), 1000);
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail:
          'Unexpected error occured when trying to make program purhcase. Please, try again later',
        closable: true,
      });
    }
  }

  openParticipationModal() {
    this.isParticipationModalOpen = true;
  }

  onIsParticipationModalOpenChanged(isOpen: boolean) {
    this.isParticipationModalOpen = isOpen;
  }

  private loadProgramComments(programId: number) {
    this.isLoadingComments = true;
    this.programCommentService
      .getAllProgramComments(programId)
      .subscribe({
        next: (response) => {
          response.forEach((x) => {
            x.avatar = `${this.PICTURES_URL}${x.avatar}`;
            x.datePosted = this.convertToCommentDate(x.datePosted);
          });
          this.programComments = [...response];
        },
        error: (err) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Unable to load fitness program comments.',
          });
        },
      })
      .add(() => {
        setTimeout(() => (this.isLoadingComments = false), 1000);
      });
  }

  onTabViewChange(event: TabViewChangeEvent) {
    if (event.index == 0) {
      this.isLoadingDetails = true;
      setTimeout(() => (this.isLoadingDetails = false), 500);
    } else if (event.index == 1) {
      if (this.program) {
        let id = this.program.programId;
        this.loadProgramComments(id);
      } else {
        this.isLoadingComments = true;
        setTimeout(() => (this.isLoadingComments = false), 500);
      }
    }
  }

  onProgramCommentPosted(comment: ProgramCommentResponse) {
    if (this.program) {
      let id = this.program.programId;
      this.loadProgramComments(id);
      this.program.numberOfComments++;
    } else {
      this.programComments.push(comment);
      comment.avatar = `${this.PICTURES_URL}${comment.avatar}`;
      comment.datePosted = this.convertToCommentDate(comment.datePosted);
      this.isLoadingComments = true;
      setTimeout(() => (this.isLoadingComments = false), 500);
    }
  }

  private convertToCommentDate(dateStr: string): string {
    let date = new Date(dateStr);
    const currentDate = new Date();
    const timeDifference = currentDate.getTime() - date.getTime();
    const daysDifference = Math.floor(timeDifference / (1000 * 3600 * 24));

    if (daysDifference > 5) {
      // If date is older than 5 days
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      const formattedDate = `${hours}:${minutes}, ${date.toLocaleDateString(
        'en-US',
        { day: '2-digit', month: '2-digit', year: 'numeric' }
      )}`;
      return formattedDate;
    } else {
      // If date is 5 days old or newer
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      const dayOfWeek = date.toLocaleDateString('en-US', { weekday: 'long' });
      const formattedDate = `${hours}:${minutes}, ${dayOfWeek}`;
      return formattedDate;
    }
  }
}

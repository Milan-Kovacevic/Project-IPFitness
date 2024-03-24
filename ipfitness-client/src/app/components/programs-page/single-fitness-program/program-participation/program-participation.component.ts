import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';
import { ProgramDetailsResponse } from '../../../../interfaces/responses/program-details-response';
import { Router } from '@angular/router';
import { FitnessProgramService } from '../../../../services/fitness-program.service';
import { firstValueFrom } from 'rxjs';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { YouTubeSafeUrlPipe } from '../../../../pipes/you-tube-safe-url.pipe';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-program-participation',
  standalone: true,
  imports: [PrimeNgModule, YouTubeSafeUrlPipe, CommonModule],
  templateUrl: './program-participation.component.html',
  styleUrl: './program-participation.component.css',
})
export class ProgramParticipationComponent {
  @Input({ required: true }) isModalOpen!: boolean;
  @Output() isModalOpenChanged = new EventEmitter<boolean>();
  @Input({ required: true }) program!: ProgramDetailsResponse;
  @Input({ required: true }) programLocation!: string;
  @Input({ required: true }) isOnline!: boolean;
  isLoading: boolean = true;
  programDemonstrations: string[] = [];

  constructor(
    private router: Router,
    private fitnessProgramService: FitnessProgramService
  ) {}

  onCloseModal(isOpen: boolean) {
    this.isLoading = true;
    this.isModalOpenChanged.emit(isOpen);
  }

  onModalOpened() {
    this.loadFitnessProgramDemonstrations();
  }

  onRecordActivityClicked() {
    this.router.navigate(['activities'], { queryParams: { add: '1' } });
  }

  private loadFitnessProgramDemonstrations() {
    if (this.isOnline) {
      this.isLoading = true;
      firstValueFrom(
        this.fitnessProgramService.getFitnessProgramDemonstrations(
          this.program.programId
        )
      )
        .then((response) => {
          this.programDemonstrations = response.map((video) =>
            this.convertYoutubeVideoToEmbeded(video)
          );
        })
        .finally(() => setTimeout(() => (this.isLoading = false), 1000));
    } else {
      setTimeout(() => (this.isLoading = false), 750);
    }
  }

  private convertYoutubeVideoToEmbeded(videoUrl: string): string {
    var matches =
      /(?:http:\/\/)?(?:www\.)?(?:youtube\.com|youtu\.be)\/(?:watch\?v=)?([^& \n]+)/g.exec(
        videoUrl
      );
    if (!matches) return '';
    return `http://www.youtube-nocookie.com/embed/${matches[1]}?mute=1`;
  }
}

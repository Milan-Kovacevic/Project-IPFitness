import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';

@Component({
  selector: 'app-demonstration-videos-step',
  standalone: true,
  imports: [PrimeNgModule],
  templateUrl: './demonstration-videos-step.component.html',
  styleUrl: './demonstration-videos-step.component.css',
})
export class DemonstrationVideosStepComponent {
  @Input() isLocationOnline: boolean = false;
  @Output() onVideoCountChange = new EventEmitter<number>();
  public demonstration: string = '';
  public demonstrationVideos: string[] = [];
  public isStepChanged: boolean = false;
  public hasVideos: boolean = this.demonstrationVideos.length > 0;

  public setInitialDemonstrations(demonstrations: string[]) {
    this.demonstrationVideos = [...demonstrations];
    this.demonstration = '';
    this.emitOnVideoCountChanges();
  }

  public clear() {
    this.demonstrationVideos = [];
    this.demonstration = '';
    this.isStepChanged = true;
    this.emitOnVideoCountChanges();
  }

  public addDemonstration() {
    if (
      this.demonstration != '' &&
      !this.demonstrationVideos.includes(this.demonstration)
    ) {
      this.demonstrationVideos.push(this.demonstration);
      this.emitOnVideoCountChanges();
      this.isStepChanged = true;
    }
  }

  public removeDemonstration(item: string) {
    let index: number = this.demonstrationVideos.findIndex((e) => e == item);
    if (index >= 0) {
      this.demonstrationVideos.splice(index, 1);
      this.emitOnVideoCountChanges();
      this.isStepChanged = true;
    }
  }

  public emitOnVideoCountChanges() {
    this.onVideoCountChange.emit(this.demonstrationVideos.length);
  }

  public getDemonstrationVideos(): string[] | undefined {
    if (this.isLocationOnline) return this.demonstrationVideos;
    return undefined;
  }
}

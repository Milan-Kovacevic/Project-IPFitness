import { Component, ViewChild } from '@angular/core';
import { PrimeNgModule } from '../../../../shared/prime-ng/prime-ng.module';
import {
  FileRemoveEvent,
  FileSelectEvent,
  FileUpload,
} from 'primeng/fileupload';
import { StorageAccessService } from '../../../../services/storage-access.service';

@Component({
  selector: 'app-program-pictures-step',
  standalone: true,
  imports: [PrimeNgModule],
  templateUrl: './program-pictures-step.component.html',
  styleUrl: './program-pictures-step.component.css',
})
export class ProgramPicturesStepComponent {
  programPuctures: File[] = [];
  public isStepChanged: boolean = false;
  @ViewChild('pictureUpload') pictureUpload!: FileUpload;

  constructor(private storageAccessService: StorageAccessService) {}

  public onPictureSubmit(event: FileSelectEvent) {
    if (event.currentFiles.length > 0 && event.currentFiles.length <= 6) {
      this.programPuctures = [...event.currentFiles];
    } else {
      this.pictureUpload.clear();
    }

    this.isStepChanged = true;
  }

  public setInitialPictures(pictures: string[]) {
    let images: File[] = [];
    pictures.forEach((fileName) => {
      this.storageAccessService.getFile(fileName).subscribe({
        next: (response) => {
          const blob = new Blob([response]);
          const file = new File([blob], fileName, { type: 'image/jpeg' });
          images.push(file);
        },
      });
    });
    this.programPuctures = images;
    // Doesnt render on component...
    this.pictureUpload.files = [...this.programPuctures];
  }

  public clearManually() {
    this.pictureUpload.clear();
  }

  public clear() {
    this.onPictureClear(undefined);
  }

  public onPictureClear(event: any) {
    this.programPuctures = [];
    this.isStepChanged = true;
  }

  public onPictureRemove(event: FileRemoveEvent) {
    const index = this.programPuctures.findIndex(
      (f) => f.name == event.file.name
    );
    if (index >= 0 && index < this.programPuctures.length)
      this.programPuctures.splice(index, 1);

    this.isStepChanged = true;
  }
}

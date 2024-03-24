import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BACKEND_BASE_URL } from '../utils/constants';

@Injectable({
  providedIn: 'root',
})
export class StorageAccessService {
  private PICTURE_URL: string = `${BACKEND_BASE_URL}/storage`;

  constructor(private http: HttpClient) {}

  public getFile(pictureName: string): Observable<ArrayBuffer> {
    let pictureEndpoint: string = `${this.PICTURE_URL}?fileName=${pictureName}`;
    return this.http.get(pictureEndpoint, {
      responseType: 'arraybuffer',
    });
  }
}

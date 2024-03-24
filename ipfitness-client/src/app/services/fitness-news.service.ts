import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BACKEND_BASE_URL } from '../utils/constants';
import { FitnessNewsResponse } from '../interfaces/responses/fitness-news-response';

@Injectable({
  providedIn: 'root'
})
export class FitnessNewsService {
  private FITNESS_NEWS_URL: string = `${BACKEND_BASE_URL}/news`;
  constructor(private http: HttpClient) {}

  public getAllFitnessNews(){
    return this.http.get<FitnessNewsResponse>(this.FITNESS_NEWS_URL);
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BACKEND_BASE_URL } from '../utils/constants';
import { UserContextService } from '../shared/context/user-context.service';
import { DailyExercisesResponse } from '../interfaces/responses/daily-exercises-response';

@Injectable({
  providedIn: 'root',
})
export class FitnessExercisesService {
  private FITNESS_EXERCISES_URL: string = `${BACKEND_BASE_URL}/exercises`;
  constructor(
    private http: HttpClient,
    private userContext: UserContextService
  ) {}

  public getDailyExercises() {
    return this.http.get<DailyExercisesResponse>(this.FITNESS_EXERCISES_URL, {
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }
}

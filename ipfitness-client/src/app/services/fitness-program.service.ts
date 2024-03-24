import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PageRequest } from '../interfaces/requests/page-request';
import { PageableResponse } from '../interfaces/responses/pageable-response';
import { FitnessProgramResponse } from '../interfaces/responses/fitness-program-response';
import { UserContextService } from '../shared/context/user-context.service';
import { ProgramAddRequest } from '../interfaces/requests/program-add-request';
import { SingleFitnessProgramResponse } from '../interfaces/responses/single-fitness-program-response';
import { ProgramInfoUpdateRequest } from '../interfaces/requests/program-info-update-request';
import { BACKEND_BASE_URL, programStates } from '../utils/constants';
import { EnumItem } from '../models/enum-item';
import {
  SHOW_OWN_PROGRAMS_KEY,
  SHOW_PURCHASED_PROGRAMS_KEY,
} from '../utils/constants';
import { ProgramDetailsResponse } from '../interfaces/responses/program-details-response';

@Injectable({
  providedIn: 'root',
})
export class FitnessProgramService {
  constructor(
    private http: HttpClient,
    private userContext: UserContextService
  ) {}
  private ALL_PROGRAMS_URL: string = `${BACKEND_BASE_URL}/programs`;
  private USER_PROGRAMS_URL: string = `${BACKEND_BASE_URL}/users`;

  public getAllPrograms(page: PageRequest) {
    let queryParams = new HttpParams();
    const params = queryParams.appendAll({
      size: page.size,
      page: page.page,
      sort: page.sort ?? '',
      categoryFilter: page.categoryFilter ?? '',
      difficultyFilter: page.difficultyFilter ?? '',
      locationFilter: page.locationFilter ?? '',
      stateFilter: page.stateFilter ?? '',
      searchQuery: page.searchQuery ?? '',
    });

    if (page.additionalOption !== undefined) {
      let userId = this.userContext.getUserId();
      let userToken = this.userContext.getToken();
      let options = {
        params: params,
        headers: {
          Authorization: `Bearer ${userToken}`,
        },
      };
      if (page.additionalOption === SHOW_OWN_PROGRAMS_KEY) {
        let ownProgramsEndpoint = `${this.USER_PROGRAMS_URL}/${userId}/own-programs`;
        return this.http.get<PageableResponse<FitnessProgramResponse>>(
          ownProgramsEndpoint,
          options
        );
      } else if (page.additionalOption === SHOW_PURCHASED_PROGRAMS_KEY) {
        let purchasedProgramsEndpoint = `${this.USER_PROGRAMS_URL}/${userId}/purchased-programs`;
        return this.http.get<PageableResponse<FitnessProgramResponse>>(
          purchasedProgramsEndpoint,
          options
        );
      } else {
        return this.getAllFitnessPrograms(params);
      }
    } else {
      return this.getAllFitnessPrograms(params);
    }
  }

  private getAllFitnessPrograms(params: HttpParams) {
    return this.http.get<PageableResponse<FitnessProgramResponse>>(
      this.ALL_PROGRAMS_URL,
      { params: params }
    );
  }

  public deleteFitnessProgram(programId: number) {
    let deleteEndpoint: string = `${this.ALL_PROGRAMS_URL}/${programId}`;
    return this.http.delete(deleteEndpoint, {
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }

  public addFitnessProgram(request: ProgramAddRequest) {
    const formData = new FormData();
    formData.append('userId', request.userId.toString());
    formData.append('name', request.name);
    formData.append('description', request.description ?? '');
    formData.append('location', request.location);
    formData.append('difficulty', request.difficulty);
    formData.append('price', request.price.toString());
    formData.append('startDate', request.startDate.toString());
    formData.append('endDate', request.endDate?.toString() ?? '');
    formData.append('categoryId', request.categoryId.toString());
    for (let i = 0; i < request.attributeValues.length; i++) {
      const keyPrefix = 'attributeValues[' + i.toString() + '].';
      formData.append(
        `${keyPrefix}attributeId`,
        request.attributeValues[i].attributeId.toString()
      );
      formData.append(`${keyPrefix}value`, request.attributeValues[i].value);
    }
    request.pictures.forEach((picture) => {
      formData.append('pictures', picture);
    });
    request.demonstrations.forEach((videoUrl) => {
      formData.append('demonstrations', videoUrl);
    });

    return this.http.post(this.ALL_PROGRAMS_URL, formData, {
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }

  public updateProgramInformations(
    request: ProgramInfoUpdateRequest,
    programId: number
  ) {
    let updateInfoEndpoint = `${this.ALL_PROGRAMS_URL}/${programId}/info`;
    return this.http.put(updateInfoEndpoint, request, {
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }

  public updateProgramPictures(pictures: File[], programId: number) {
    const formData = new FormData();
    pictures.forEach((picture) => {
      formData.append('pictures', picture);
    });
    let updatePicturesEndpoint = `${this.ALL_PROGRAMS_URL}/${programId}/pictures`;
    return this.http.put(updatePicturesEndpoint, formData, {
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }

  public updateProgramDemonstrations(videos: string[], programId: number) {
    let updateDemonstrationsEndpoint = `${this.ALL_PROGRAMS_URL}/${programId}/videos`;
    return this.http.put(updateDemonstrationsEndpoint, videos, {
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }

  public getFitnessProgram(programId: number) {
    let getProgramEndpoint = `${this.ALL_PROGRAMS_URL}/${programId}`;
    return this.http.get<SingleFitnessProgramResponse>(getProgramEndpoint);
  }

  public getFitnessProgramDetails(programId: number) {
    let getProgramDetailsEndpoint = `${this.ALL_PROGRAMS_URL}/${programId}/details`;
    if (this.userContext.isLoggedIn()) {
      let token = this.userContext.getToken();
      return this.http.get<ProgramDetailsResponse>(getProgramDetailsEndpoint, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
    } else
      return this.http.get<ProgramDetailsResponse>(getProgramDetailsEndpoint);
  }

  public getFitnessProgramDemonstrations(programId: number) {
    let getProgramDemonstrationsEndpoint = `${this.ALL_PROGRAMS_URL}/${programId}/videos`;
    return this.http.get<string[]>(getProgramDemonstrationsEndpoint, {
      headers: {
        Authorization: `Bearer ${this.userContext.getToken()}`,
      },
    });
  }
}

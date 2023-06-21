import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { InstitutionBasicInfoDto, MasterDataDto, WorklistDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class WorklistService {

  constructor(
    private readonly http: HttpClient,
    private readonly contextService: ContextService
  ) { }

  getWorklistStatus(): Observable<MasterDataDto[]> {
    const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/worklist/informer-status`;
    return this.http.get<MasterDataDto[]>(url);
  }

  getByModalityAndInstitution(modalityId?: number): Observable<WorklistDto[]> {
    let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/worklist/by-modality`;

    let queryParams: HttpParams = new HttpParams();
    if (modalityId) queryParams = queryParams.append('modalityId', JSON.stringify(modalityId));

    return this.http.get<WorklistDto[]>(url, { params: queryParams });
  }

  getInformerInstitutions(): Observable<InstitutionBasicInfoDto[]> {
    const url = `${environment.apiBase}/institution/imageSector`;
    return this.http.get<InstitutionBasicInfoDto[]>(url);
  }
}

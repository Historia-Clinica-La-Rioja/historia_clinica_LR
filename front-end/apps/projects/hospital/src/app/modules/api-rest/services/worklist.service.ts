import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MasterDataDto, WorklistDto } from '@api-rest/api-model';
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

  getByModalityAndInstitution(modalityId: number): Observable<WorklistDto[]> {
    const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/worklist/by-modality/${modalityId}`;
    return this.http.get<WorklistDto[]>(url);
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ViolenceReportDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ViolenceReportService {

  private readonly BASE_URL: string;

  constructor(
    private http: HttpClient, private readonly contextService: ContextService
  ) {
    this.BASE_URL = `${environment.apiBase}/institution/${this.contextService.institutionId}/violence-report/`;
  }

  saveNewViolenceReport(newViolenceReport: ViolenceReportDto, patientId: number) {
    const url = this.BASE_URL + 'patient/' + patientId;
    return this.http.post<ViolenceReportDto>(url, newViolenceReport);
  }
}

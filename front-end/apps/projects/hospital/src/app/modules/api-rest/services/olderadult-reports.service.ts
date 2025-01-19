import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { ContextService } from '@core/services/context.service';
import { DownloadService } from '@core/services/download.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OlderadultReportsService {

  constructor(
    private contextService: ContextService,
    private downloadService: DownloadService,
  ) { }

  private getOlderadultReport(params: any, fileName: string, url: any): Observable<any> {
    let requestParams: HttpParams = new HttpParams();
    requestParams = requestParams.append('fromDate', toApiFormat(params.startDate));
    requestParams = requestParams.append('toDate', toApiFormat(params.endDate));

    return this.downloadService.downloadXlsWithRequestParams(url, fileName, requestParams);
  }

  getOlderAdultsOutpatientReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/olderadultsreports/${this.contextService.institutionId}/older-adults-outpatient`;
    return this.getOlderadultReport(params, fileName, url);
  }

  getOlderAdultsHospitalizationReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/olderadultsreports/${this.contextService.institutionId}/older-adults-hospitalization`;
    return this.getOlderadultReport(params, fileName, url);
  }

  getPolypharmacyReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/olderadultsreports/${this.contextService.institutionId}/polypharmacy`;
    return this.getOlderadultReport(params, fileName, url);
  }
  
  getGerontologicalAssessmentsExcelReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/olderadultsreports/${this.contextService.institutionId}/gerontological-assessments`;
    return this.getOlderadultReport(params, fileName, url);
  }
}
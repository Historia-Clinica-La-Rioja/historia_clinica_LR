import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { DownloadService } from '@core/services/download.service';
import { environment } from '@environments/environment.prod';
import { Observable } from 'rxjs';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';


@Injectable({
  providedIn: 'root'
})
export class EnfermeriaReportService {

  constructor(
    private contextService: ContextService,
    private downloadService: DownloadService,
  ) { }

  private getEnfermeriaReport(params: any, fileName: string, url: any): Observable<any> {
    let requestParams: HttpParams = new HttpParams();
    requestParams = requestParams.append('fromDate', momentFormat(params.startDate, DateFormat.API_DATE));
		requestParams = requestParams.append('toDate', momentFormat(params.endDate, DateFormat.API_DATE));

    return this.downloadService.downloadXlsWithRequestParams(url, fileName, requestParams);
  }
  
  getNursingInternmentReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/nursingreports/${this.contextService.institutionId}/hospitalizationNursing`;
    return this.getEnfermeriaReport(params, fileName, url);
  }

  getPatientEmergenciesReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/nursingreports/${this.contextService.institutionId}/nursingEmergencies`;
    return this.getEnfermeriaReport(params, fileName, url);
  }

  getOutpatientNursingReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/nursingreports/${this.contextService.institutionId}/outpatientNursing`;
    return this.getEnfermeriaReport(params, fileName, url);
  }

  getTotalNursingRecoveryReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/nursingreports/${this.contextService.institutionId}/totalNursingRecovery`;
    return this.getEnfermeriaReport(params, fileName, url);
  }
  
     
}
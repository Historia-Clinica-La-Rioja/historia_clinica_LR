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
export class GeneralReportService {

  constructor(
    private contextService: ContextService,
    private downloadService: DownloadService,
  ) { }

  private getGeneralReport(params: any, fileName: string, url: any): Observable<any> {
    let requestParams: HttpParams = new HttpParams();
    requestParams = requestParams.append('fromDate', momentFormat(params.startDate, DateFormat.API_DATE));
		requestParams = requestParams.append('toDate', momentFormat(params.endDate, DateFormat.API_DATE));

    return this.downloadService.downloadXlsWithRequestParams(url, fileName, requestParams);
  }
  
  getDaiylyEmergencyReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/generalreports/${this.contextService.institutionId}/dailyEmergency`;
    return this.getGeneralReport(params, fileName, url);
  }

  getDiabeticsReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/generalreports/${this.contextService.institutionId}/diabetics`;
    return this.getGeneralReport(params, fileName, url);
  }
  getHypertensiveReport(params: any, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/generalreports/${this.contextService.institutionId}/hypertensive`;
		return this.getGeneralReport(params, fileName, url);
	}
  getPatientEmergenciesReport(params: any, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/generalreports/${this.contextService.institutionId}/patientEmergencies`;
		return this.getGeneralReport(params, fileName, url);
	}


    
}
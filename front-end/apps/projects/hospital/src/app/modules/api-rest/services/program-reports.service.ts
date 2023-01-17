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
export class ProgramReportsService {

  constructor(
    private contextService: ContextService,
    private downloadService: DownloadService,
  ) { }

  private getReportProgram(params: any, fileName: string, url: any): Observable<any> {
    let requestParams: HttpParams = new HttpParams();
    requestParams = requestParams.append('fromDate', momentFormat(params.startDate, DateFormat.API_DATE));
		requestParams = requestParams.append('toDate', momentFormat(params.endDate, DateFormat.API_DATE));
    if (params.specialtyId) {
			requestParams = requestParams.append('clinicalSpecialtyId', params.specialtyId);
		}
		if (params.professionalId) {
			requestParams = requestParams.append('doctorId', params.professionalId);
		}
		return this.downloadService.downloadXlsWithRequestParams(url, fileName, requestParams);

  }

  getMonthlyEpiIReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/programreports/${this.contextService.institutionId}/monthlyEpiI`;
    return this.getReportProgram(params, fileName, url);
  }

  getMonthlyEpiIIReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/programreports/${this.contextService.institutionId}/monthlyEpiII`;
    return this.getReportProgram(params, fileName, url);
  }
  getMonthlyRecupero(params: any, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/programreports/${this.contextService.institutionId}/monthlyRecupero`;
		return this.getReportProgram(params, fileName, url);
	}

	getMonthlySumar(params: any, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/programreports/${this.contextService.institutionId}/monthlySumar`;
		return this.getReportProgram(params, fileName, url);
	}

  getMonthlyOdontologyReport(params: any, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/programreports/${this.contextService.institutionId}/odontology`;
		return this.getReportProgram(params, fileName, url);
	}
}


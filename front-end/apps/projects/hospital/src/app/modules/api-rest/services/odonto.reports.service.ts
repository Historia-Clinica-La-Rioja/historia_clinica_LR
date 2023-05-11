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
export class OdontoReportService {

  constructor(
    private contextService: ContextService,
    private downloadService: DownloadService,
  ) { }

  private getOdontoReport(params: any, fileName: string, url: any): Observable<any> {
    let requestParams: HttpParams = new HttpParams();
    requestParams = requestParams.append('fromDate', momentFormat(params.startDate, DateFormat.API_DATE));
		requestParams = requestParams.append('toDate', momentFormat(params.endDate, DateFormat.API_DATE));

    return this.downloadService.downloadXlsWithRequestParams(url, fileName, requestParams);
  }

  getMonthlyPromocionReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/odontologyreports/${this.contextService.institutionId}/monthlyPromocionPrimerNivel`;
    return this.getOdontoReport(params, fileName, url);
  }

  getMonthlyPrevencionReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/odontologyreports/${this.contextService.institutionId}/monthlyPrevencionPrimerNivel`;
    return this.getOdontoReport(params, fileName, url);
  }
  getMonthlyPrevencionGrupalReport(params: any, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/odontologyreports/${this.contextService.institutionId}/monthlyPrevencionGrupalPrimerNivel`;
		return this.getOdontoReport(params, fileName, url);
	}

	getMonthlyOperatoriaReport(params: any, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/odontologyreports/${this.contextService.institutionId}/monthlyOperatoriaSegundoNivel`;
		return this.getOdontoReport(params, fileName, url);
	}

    getMonthlyEndodonciaReport(params: any, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/odontologyreports/${this.contextService.institutionId}/monthlyEndodonciaSegundoNivel`;
		return this.getOdontoReport(params, fileName, url);
	}

  getMonthlyRecuperoReport(params: any, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/odontologyreports/${this.contextService.institutionId}/recuperoOdontology`;
		return this.getOdontoReport(params, fileName, url);
	}
    
}


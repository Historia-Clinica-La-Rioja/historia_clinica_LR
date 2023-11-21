import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { DownloadService } from '@core/services/download.service';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PregnantpeopleReportsService {

  constructor(
    private contextService: ContextService,
    private downloadService: DownloadService,
  ) { }

  private getPregnantpeopleReport(params: any, fileName: string, url: any): Observable<any> {
    let requestParams: HttpParams = new HttpParams();
    requestParams = requestParams.append('fromDate', momentFormat(params.startDate, DateFormat.API_DATE));
    requestParams = requestParams.append('toDate', momentFormat(params.endDate, DateFormat.API_DATE));

    return this.downloadService.downloadXlsWithRequestParams(url, fileName, requestParams);
  }

  getPregnantAttentionsReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/pregnantpeoplereports/${this.contextService.institutionId}/pregnant-attentions`;
    return this.getPregnantpeopleReport(params, fileName, url);
  }

  getPregnantControlsReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/pregnantpeoplereports/${this.contextService.institutionId}/pregnant-controls`;
    return this.getPregnantpeopleReport(params, fileName, url);
  }

}

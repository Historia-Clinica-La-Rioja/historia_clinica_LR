import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { DownloadService } from '@core/services/download.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DengueReportsService {

  constructor(
    private contextService: ContextService,
    private downloadService: DownloadService,
  ) { }

  private getDengueReport(params: any, fileName: string, url: any): Observable<any> {
    let requestParams: HttpParams = new HttpParams();
    return this.downloadService.downloadXlsWithRequestParams(url, fileName, requestParams);
  }

  getDengueAttentionsReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/epidemiologyreports/${this.contextService.institutionId}/dengue-patient-control`;
    return this.getDengueReport(params, fileName, url);
  }

  getDengueControlsReport(params: any, fileName: string): Observable<any> {
    const url = `${environment.apiBase}/epidemiologyreports/${this.contextService.institutionId}/complete-dengue`;
    return this.getDengueReport(params, fileName, url);
  }

}

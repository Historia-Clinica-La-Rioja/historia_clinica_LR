import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { DownloadService } from '@core/services/download.service';
import { environment } from '@environments/environment.prod';
import { Observable } from 'rxjs';

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
}

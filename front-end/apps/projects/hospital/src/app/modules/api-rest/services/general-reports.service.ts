import { HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { toApiFormat } from "@api-rest/mapper/date.mapper";
import { ContextService } from "@core/services/context.service";
import { DownloadService } from "@core/services/download.service";
import { environment } from "@environments/environment";
import { Observable } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class GeneralReportsService {

    constructor(
        private contextService: ContextService,
        private downloadService: DownloadService,
    ) { }

    private getGeneralReport(params: any, fileName: string, url: any): Observable<any> {
        let requestParams: HttpParams = new HttpParams();
        requestParams = requestParams.append('fromDate', toApiFormat(params.startDate));
        requestParams = requestParams.append('toDate', toApiFormat(params.endDate));

        return this.downloadService.downloadXlsWithRequestParams(url, fileName, requestParams);
    }
    
    getEmergencyReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/generalreports/${this.contextService.institutionId}/emergency`;
        return this.getGeneralReport(params, fileName, url);
    }

    getDiabeticReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/generalreports/${this.contextService.institutionId}/diabetic`;
        return this.getGeneralReport(params, fileName, url);
    }

    getHypertensiveReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/generalreports/${this.contextService.institutionId}/hypertensive`;
        return this.getGeneralReport(params, fileName, url);
    }

    getComplementaryStudiesReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/generalreports/${this.contextService.institutionId}/complementary-studies`;
        return this.getGeneralReport(params, fileName, url);
    }
    
}
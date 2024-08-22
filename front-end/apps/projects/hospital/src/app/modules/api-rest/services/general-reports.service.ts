import { HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ContextService } from "@core/services/context.service";
import { DownloadService } from "@core/services/download.service";
import { DateFormat, momentFormat } from "@core/utils/moment.utils";
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
        requestParams = requestParams.append('fromDate', momentFormat(params.startDate, DateFormat.API_DATE));
        requestParams = requestParams.append('toDate', momentFormat(params.endDate, DateFormat.API_DATE));

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
    getMedicationPrescriptionReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/generalreports/${this.contextService.institutionId}/medicines-prescription`;
        return this.getGeneralReport(params, fileName, url);
    }
    
}
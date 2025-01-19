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
export class NursingReportsService {

    constructor(
        private contextService: ContextService,
        private downloadService: DownloadService,
    ) { }

    private getNursingReport(params: any, fileName: string, url: any): Observable<any> {
        let requestParams: HttpParams = new HttpParams();
        requestParams = requestParams.append('fromDate', toApiFormat(params.startDate));
        requestParams = requestParams.append('toDate', toApiFormat(params.endDate));

        return this.downloadService.downloadXlsWithRequestParams(url, fileName, requestParams);
    }

    getNursingEmergencyReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/nursingreports/${this.contextService.institutionId}/nursing-emergency`;
        return this.getNursingReport(params, fileName, url);
    }

    getNursingOutpatientReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/nursingreports/${this.contextService.institutionId}/nursing-outpatient`;
        return this.getNursingReport(params, fileName, url);
    }

    getNursingHospitalizationReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/nursingreports/${this.contextService.institutionId}/nursing-hospitalization`;
        return this.getNursingReport(params, fileName, url);
    }
    
    getNursingProceduresReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/nursingreports/${this.contextService.institutionId}/nursing-procedures`;
        return this.getNursingReport(params, fileName, url);
    }

    getNursingVaccineReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/nursingreports/${this.contextService.institutionId}/nursing-vaccine`;
        return this.getNursingReport(params, fileName, url);
    }
    
}
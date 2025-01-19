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
export class ProgramReportsService {

    constructor(
        private contextService: ContextService,
        private downloadService: DownloadService,
    ) { }

    private getProgramReport(params: any, fileName: string, url: any): Observable<any> {
        let requestParams: HttpParams = new HttpParams();
        requestParams = requestParams.append('fromDate', toApiFormat(params.startDate));
		requestParams = requestParams.append('toDate', toApiFormat(params.endDate));
        if (params.specialtyId) {
			requestParams = requestParams.append('clinicalSpecialtyId', params.specialtyId);
		}
		if (params.professionalId) {
			requestParams = requestParams.append('doctorId', params.professionalId);
		}
		return this.downloadService.downloadXlsWithRequestParams(url, fileName, requestParams);
    }

    getEpidemiologyOneReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/programreports/${this.contextService.institutionId}/epidemiology-one`;
        return this.getProgramReport(params, fileName, url);
    }

    getEpidemiologyTwoReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/programreports/${this.contextService.institutionId}/epidemiology-two`;
        return this.getProgramReport(params, fileName, url);
    }

    getRecuperoGeneralReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/programreports/${this.contextService.institutionId}/recupero-general`;
        return this.getProgramReport(params, fileName, url);
    }

    getRecuperoOdontologicoReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/programreports/${this.contextService.institutionId}/recupero-odontologico`;
        return this.getProgramReport(params, fileName, url);
    }

    getSumarGeneralExcelReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/programreports/${this.contextService.institutionId}/sumar-general`;
        return this.getProgramReport(params, fileName, url);
    }

    getSumarOdontologicoReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/programreports/${this.contextService.institutionId}/sumar-odontologico`;
        return this.getProgramReport(params, fileName, url);
    }
    
}
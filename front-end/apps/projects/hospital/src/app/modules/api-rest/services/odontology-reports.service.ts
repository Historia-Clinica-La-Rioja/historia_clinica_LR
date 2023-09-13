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
export class OdontologyReportsService {
    
    constructor(
        private contextService: ContextService,
        private downloadService: DownloadService,
    ) { }

    private getOdontologyReport(params: any, fileName: string, url: any): Observable<any> {
        let requestParams: HttpParams = new HttpParams();
        requestParams = requestParams.append('fromDate', momentFormat(params.startDate, DateFormat.API_DATE));
        requestParams = requestParams.append('toDate', momentFormat(params.endDate, DateFormat.API_DATE));

        return this.downloadService.downloadXlsWithRequestParams(url, fileName, requestParams);
    }

    getPromocionPrimerNivelReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/odontologicalreports/${this.contextService.institutionId}/promocion-primer-nivel`;
        return this.getOdontologyReport(params, fileName, url);
    }

    getPrevencionPrimerNivelReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/odontologicalreports/${this.contextService.institutionId}/prevencion-primer-nivel`;
        return this.getOdontologyReport(params, fileName, url);
    }

    getPrevencionGrupalPrimerNivelReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/odontologicalreports/${this.contextService.institutionId}/prevencion-grupal-primer-nivel`;
        return this.getOdontologyReport(params, fileName, url);
    }

    getOperatoriaSegundoNivelReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/odontologicalreports/${this.contextService.institutionId}/operatoria-segundo-nivel`;
        return this.getOdontologyReport(params, fileName, url);
    }

    getEndodonciaSegundoNivelReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/odontologicalreports/${this.contextService.institutionId}/endodoncia-segundo-nivel`;
        return this.getOdontologyReport(params, fileName, url);
    }

    getOdontologicalProceduresReport(params: any, fileName: string): Observable<any> {
        const url = `${environment.apiBase}/odontologicalreports/${this.contextService.institutionId}/odontological-procedures`;
        return this.getOdontologyReport(params, fileName, url);
    }
    
}
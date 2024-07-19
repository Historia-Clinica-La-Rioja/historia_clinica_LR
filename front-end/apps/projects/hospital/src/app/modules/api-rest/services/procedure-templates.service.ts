import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ProcedureTemplateFullSummaryDto, SnomedDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';


@Injectable({
	providedIn: 'root'
})
export class ProcedureTemplatesService {
	private URL_PREFIX = `${environment.apiBase}/procedure-templates`;

	constructor(
		private http: HttpClient,
	) { }

	findById(procedureTemplateId: number): Observable<ProcedureTemplateFullSummaryDto> {
		const url = this.URL_PREFIX + `/${procedureTemplateId}`;
		return this.http.get<ProcedureTemplateFullSummaryDto>(url);
	}

	findByDiagnosticReportId(diagnosticReportId: number): Observable<ProcedureTemplateFullSummaryDto[]> {
		const url = this.URL_PREFIX + `/diagnostic-report/${diagnosticReportId}`;
		return this.http.get<ProcedureTemplateFullSummaryDto[]>(url);
	}

	findAvailableForSnomedConcept(snomed: SnomedDto): Observable<ProcedureTemplateFullSummaryDto[]> {
		const url = this.URL_PREFIX + `/snomed`;
		let params = new HttpParams();
		params = params.append("sctid", snomed.sctid);
		params = params.append("pt", snomed.pt);
		return this.http.get<ProcedureTemplateFullSummaryDto[]>(url, { params });
	}
}


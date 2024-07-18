import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { UIComponentDto } from '@extensions/extensions-model';


@Injectable({
	providedIn: 'root'
})
export class ReportsCubeService {

	constructor(
		private contextService: ContextService,
		private http: HttpClient
	) { }

	getInstitutionReport(reportName: string): Observable<UIComponentDto> {
		const url = `${environment.apiBase}/reports/institution/${this.contextService.institutionId}/${reportName}`;
		return this.http.get<UIComponentDto>(url);
	}

}

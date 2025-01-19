import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SurgicalReportDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class SurgicalReportService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) { }

	saveSurgicalReport(internmentEpisodeId: number, surgicalReport: SurgicalReportDto){
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/surgical-report`;
		return this.http.post<boolean>(url, surgicalReport);
	}

	getSurgicalReport(internmentEpisodeId: number, surgicalReportId: number){
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/surgical-report/${surgicalReportId}`;
		return this.http.get<SurgicalReportDto>(url);
	}

	deleteSurgicalReport(surgicalReportId: number, internmentEpisodeId: number, reason: string){
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/surgical-report/${surgicalReportId}`;
		return this.http.delete<boolean>(url, {
			body: reason
		});
	}

	editSurgicalReport(internmentEpisodeId: number, surgicalReportId: number, surgicalReport: SurgicalReportDto){
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/surgical-report/${surgicalReportId}`;
		return this.http.put<number>(url, surgicalReport);
	}
}

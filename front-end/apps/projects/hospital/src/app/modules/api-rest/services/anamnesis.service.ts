import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AnamnesisDto, ResponseAnamnesisDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { DownloadService } from '@core/services/download.service';
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class AnamnesisService {

	constructor(
		private http: HttpClient,
		private downloadService: DownloadService,
		private contextService: ContextService,
	) { }

	createAnamnesis(anamnesis: AnamnesisDto, internmentEpisodeId: number): Observable<ResponseAnamnesisDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/anamnesis`;
		return this.http.post<ResponseAnamnesisDto>(url, anamnesis);
	}

	getAnamnesis(anamnesisId: number, internmentEpisodeId: number): Observable<ResponseAnamnesisDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/anamnesis/${anamnesisId}`;
		return this.http.get<ResponseAnamnesisDto>(url);
	}

	getPDF(anamnesisId: number, internmentEpisodeId: number): Observable<any> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/anamnesis/${anamnesisId}/report`;
		const fileName = `Evaluacion_ingreso_internacion_${internmentEpisodeId}`;
		return this.downloadService.downloadPdf(url, fileName);
	}

}

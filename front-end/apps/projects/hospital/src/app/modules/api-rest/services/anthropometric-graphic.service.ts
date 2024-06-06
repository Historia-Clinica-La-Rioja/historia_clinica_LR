import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EAnthropometricGraphicType, EAnthropometricGraphicOption, AnthropometricGraphicDataDto, AnthropometricGraphicEnablementDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { EvolutionChartOptions } from '@historia-clinica/components/evolution-chart-options/evolution-chart-options.component';
import { AnthropometricData } from '@historia-clinica/services/patient-evolution-charts.service';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class AnthropometricGraphicService {

	private readonly prefixUrl = `${environment.apiBase}/institution`;
	private readonly basicUrl = `/percentiles`;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) { }

	canShowPercentilesGraphic(patientId: number): Observable<AnthropometricGraphicEnablementDto> {
		const url = `${this.prefixUrl}/${this.contextService.institutionId}${this.basicUrl}/patient/${patientId}/can-show-graphic`;
		return this.http.get<AnthropometricGraphicEnablementDto>(url);
	}

	getChartOptions(patientId: number): Observable<EAnthropometricGraphicOption[]> {
		const url = `${this.prefixUrl}/${this.contextService.institutionId}${this.basicUrl}/chart-options`;
		const params = new HttpParams().append('patientId', patientId);
		return this.http.get<EAnthropometricGraphicOption[]>(url, { params });
	}

	getAvailableGraphicTypes(chartOptionId: number, patientId: number): Observable<EAnthropometricGraphicType[]> {
		const url = `${this.prefixUrl}/${this.contextService.institutionId}${this.basicUrl}/patient/${patientId}/available-graphics`;
		const params = new HttpParams().append('chartOptionId', chartOptionId);
		return this.http.get<EAnthropometricGraphicType[]>(url, { params });
	}

	getPercentilesGraphicData(chartOption: EvolutionChartOptions, patientId: number, actualValue: AnthropometricData): Observable<AnthropometricGraphicDataDto> {
		const url = `${this.prefixUrl}/${this.contextService.institutionId}${this.basicUrl}/patient/${patientId}/graphic-data`;
		let params = new HttpParams().append('graphicOptionId', chartOption.chart.id);
		params = params.append('graphicTypeId', chartOption.type.id);
		if (actualValue)
			params = params.append('actualValue', JSON.stringify(actualValue));
		return this.http.get<AnthropometricGraphicDataDto>(url, { params });
	}
}


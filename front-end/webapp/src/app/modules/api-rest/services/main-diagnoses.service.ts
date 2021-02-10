import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { Observable } from 'rxjs';
import { MainDiagnosisDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class MainDiagnosesService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) { }

	addMainDiagnosis(internmentId: number, newMainDiagnosis: MainDiagnosisDto): Observable<MainDiagnosisDto> {
		// Por el modelo siempre se crea y el anterior diagnóstico principal pasara a ser uno alternativo
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentId}/main-diagnoses`;
		return this.http.post<MainDiagnosisDto>(url, newMainDiagnosis);
	}

}

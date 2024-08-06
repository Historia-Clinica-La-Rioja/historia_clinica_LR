import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GetCommercialMedicationSnomedDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class CommercialMedicationService {

	private BASE_URL = `${environment.apiBase}/institution/${this.contextService.institutionId}/commercial-medication`;

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getCommercialMedicationSnomedList(commercialMedicationName: string): Observable<GetCommercialMedicationSnomedDto[]> {
		const url = `${this.BASE_URL}/get-by-name`;
		let params = new HttpParams().append('commercialMedicationName', commercialMedicationName);
		return this.http.get<GetCommercialMedicationSnomedDto[]>(url, { params });
	}
}

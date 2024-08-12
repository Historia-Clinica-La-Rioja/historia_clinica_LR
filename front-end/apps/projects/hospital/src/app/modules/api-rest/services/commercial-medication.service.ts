import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GetCommercialMedicationSnomedDto, SharedSnomedDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class CommercialMedicationService {

	private BASE_URL = `${environment.apiBase}/institution/${this.contextService.institutionId}`;
	private BASE_URL2 = `${environment.apiBase}/institutions/${this.contextService.institutionId}`;

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getCommercialMedicationSnomedList(commercialMedicationName: string): Observable<GetCommercialMedicationSnomedDto[]> {
		const url = `${this.BASE_URL}/commercial-medication/get-by-name`;
		let params = new HttpParams().append('commercialMedicationName', commercialMedicationName);
		return this.http.get<GetCommercialMedicationSnomedDto[]>(url, { params });
	}

	getSuggestedCommercialMedicationSnomedListByGeneric(genericMedicationSctid: string): Observable<SharedSnomedDto[]> {
		const url = `${this.BASE_URL}/commercial-medication/get-by-generic/${genericMedicationSctid}`;
		return this.http.get<SharedSnomedDto[]>(url);
	}

	getMedicationPresentationUnits(medicationSctid: string): Observable<number[]> {
		const url = `${this.BASE_URL2}/snomed-medication/${medicationSctid}/get-presentation-units`;
		return this.http.get<number[]>(url);
	}
}

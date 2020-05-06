import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { BedDto, HealthcareProfessionalDto } from "@api-rest/api-model";
import { environment } from "@environments/environment";

@Injectable({
	providedIn: 'root'
})
export class HealthcareProfessionalService {

	constructor(private http: HttpClient) {
	}

	getAllDoctors(institutionId): Observable<HealthcareProfessionalDto[]> {
		let url = `${environment.apiBase}/institution/${institutionId}/healthcareprofessional/doctors`;
		return this.http.get<HealthcareProfessionalDto[]>(url);
	}
}

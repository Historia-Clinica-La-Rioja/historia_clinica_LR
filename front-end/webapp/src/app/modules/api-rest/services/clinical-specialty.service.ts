import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class ClinicalSpecialtyService {

	constructor(private http: HttpClient, private readonly contextService: ContextService) {
	}

	getClinicalSpecialty(professionalId): Observable<any[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/clinicalspecialty/professional/${professionalId}`;
		return this.http.get<any[]>(url);
	}
}

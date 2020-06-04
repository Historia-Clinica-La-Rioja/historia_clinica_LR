import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class ClinicalSpecialtySectorService {

	constructor(private http: HttpClient, private readonly contextService: ContextService) {
	}

	getClinicalSpecialty(sectorId): Observable<any[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/sector/${sectorId}/clinicalspecialty`;
		return this.http.get<any[]>(url);
	}
}

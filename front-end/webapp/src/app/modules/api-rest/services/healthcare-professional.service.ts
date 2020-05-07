import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { BedDto, HealthcareProfessionalDto } from "@api-rest/api-model";
import { environment } from "@environments/environment";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";
import { ContextService } from "@core/services/context.service";

@Injectable({
	providedIn: 'root'
})
export class HealthcareProfessionalService {

	constructor(private http: HttpClient,
				private contextService: ContextService) {
	}

	getAllDoctors(): Observable<HealthcareProfessionalDto[]> {
		let url = `${environment.apiBase}/institution/${this.contextService.institutionId}/healthcareprofessional/doctors`;
		return this.http.get<HealthcareProfessionalDto[]>(url);
	}
}

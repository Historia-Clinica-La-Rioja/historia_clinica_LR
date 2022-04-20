import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { DietDto } from "@api-rest/api-model";

@Injectable({
	providedIn: 'root'
})

export class InternmentIndicationService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getInternmentEpisodeDiets(internmentEpisodeId: number): Observable<DietDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/diets`;
		return this.http.get<DietDto[]>(url, {
			params: {
				institutionId: this.contextService.institutionId,
				internmentEpisodeId: internmentEpisodeId,
			}
		});
	}
	addDiet(indication: DietDto, internmentEpisodeId: number): Observable<DietDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/diet`;
		return this.http.post<DietDto>(url, indication);
	}
}

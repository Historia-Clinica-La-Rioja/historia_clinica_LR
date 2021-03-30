import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { map } from 'rxjs/operators';
import { SnomedDto, SnomedResponseDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';

export const SNOMED_RESULTS_LIMIT = '30';

@Injectable({
	providedIn: 'root'
})
export class SnowstormService {

	constructor(
		private readonly http: HttpClient
	) { }

	getSNOMEDConcepts(params): Observable<SnomedResponseDto> {
		const url = `${environment.apiBase}/snowstorm/concepts`;
		return this.http.get<any>(url, { params }).pipe(map(results => {
			const newItems = results.items.map((i: any): SnomedDto => {
				return {
					sctid: i.conceptId,
					pt: i.pt.term,
					// TODO no llegan las siguientes propiedades desde este endpoint de snowstorm
					parentFsn: '',
					parentId: ''
				};
			});
			results.items = newItems;
			return results;
		}));
	}

}

export interface ConceptRequestParams {
	term: string;
	ecl?: string;
}

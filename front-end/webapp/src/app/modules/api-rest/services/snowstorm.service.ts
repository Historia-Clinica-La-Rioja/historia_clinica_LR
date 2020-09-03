import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { environment } from '@environments/environment';
import { map } from 'rxjs/operators';
import { SnomedDto, SnomedResponseDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
const CONCEPTS = 'concepts';
const PREFERRED_AND_ACEPTABLE_CONCEPTS = ['450828004'];
export const SNOMED_RESULTS_LIMIT = '30';

@Injectable({
	providedIn: 'root'
})
export class SnowstormService {

	private params = new HttpParams()
		.set('termActive', 'true')
		.set('preferredOrAcceptableIn', `${PREFERRED_AND_ACEPTABLE_CONCEPTS.join(",")}`)
		.set('limit', SNOMED_RESULTS_LIMIT);

	private readonly headers = new HttpHeaders().set('Accept-Language', 'es-AR;q=0.8,en-GB;q=0.6')

	constructor(
		private readonly http: HttpClient
	) { }

	getSNOMEDConcepts(params: ConceptRequestParams): Observable<SnomedResponseDto> {
		Object.keys(params).forEach(
			keyParam => this.params = this.params.set(`${keyParam}`, `${params[keyParam]}`)
		);
		const url = `${environment.snowstorm}/${CONCEPTS}`;
		return this.http.get<any>(url, {
			headers: this.headers,
			params: this.params
		}).pipe(map(results => {
			const newItems = results.items.map((i: any): SnomedDto => {
				return {
					id: i.conceptId,
					pt: i.pt.term,
					// TODO no llegan las siguientes propiedades desde este endpoint de snowstorm
					parentFsn: '',
					parentId: ''
				};
			});
			results.items = newItems;
			return results;
		}
		));
	}

}

export interface ConceptRequestParams {
	term: string;
	ecl?: string;
	limit?: number;
}

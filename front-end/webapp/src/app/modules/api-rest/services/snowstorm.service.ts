import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { environment } from '@environments/environment';

const CONCEPTS = 'concepts';
const PREFERRED_CONCEPT = '900000000000548007';
const ACCEPTABLE_CONCEPT = '900000000000549004';
const RESULTS_LIMIT = '20';

@Injectable({
	providedIn: 'root'
})
export class SnowstormService {

	private params = new HttpParams()
		.set('termActive', 'true')
		.set('preferredIn', `${PREFERRED_CONCEPT}%${ACCEPTABLE_CONCEPT}`)
		.set('limit', RESULTS_LIMIT);

	private headers = new HttpHeaders().set('Accept-Language', 'es-AR;q=0.8,en-GB;q=0.6')

	constructor(
		private http: HttpClient
	) { }

	getSNOMEDConcepts(params: ConceptRequestParams) {
		Object.keys(params).forEach(
			keyParam => this.params = this.params.set(`${keyParam}`, `${params[keyParam]}`)
		);
		let url = `${environment.snowstorm}/${CONCEPTS}`;
		return this.http.get<any>(url, {
			headers: this.headers,
			params: this.params
		});
	}

}

export interface ConceptRequestParams {
	term: string;
	ecl?: string;
	limit?: number;
}

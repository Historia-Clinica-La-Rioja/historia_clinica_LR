import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { InternmentEpisodeDto, InternmentPatientDto } from "@api-rest/api-model";
import { environment } from "@environments/environment";
import { HttpClient } from "@angular/common/http";

const BASIC_URL_PREFIX = '/institutions'
const BASIC_URL_SUFIX = '/internments'

@Injectable({
	providedIn: 'root'
})
export class InternmentEpisodeService {

	constructor(private http: HttpClient) {
	}

	setNewInternmentEpisode(institutionId: number, intenmentEpisode): Observable<any>{
		let url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${institutionId}` + BASIC_URL_SUFIX;
		return this.http.post<any>(url, intenmentEpisode);
	}

}

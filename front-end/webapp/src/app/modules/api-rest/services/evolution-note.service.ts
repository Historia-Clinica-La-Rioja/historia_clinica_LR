import { Injectable } from '@angular/core';
import { EvolutionNoteDto, ResponseEvolutionNoteDto } from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

const HARD_CODE_INSTITUTION = 10;

@Injectable({
  providedIn: 'root'
})
export class EvolutionNoteService {

	constructor(
		private http: HttpClient
	) { }

	createDocument(evolutionNote: EvolutionNoteDto, internmentEpisodeId: number): Observable<ResponseEvolutionNoteDto> {
		const url = `${environment.apiBase}/institutions/${HARD_CODE_INSTITUTION}/internments/${internmentEpisodeId}/evolutionNote`;
		return this.http.post<ResponseEvolutionNoteDto>(url, evolutionNote);
	}

}

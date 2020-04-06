import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class LanguageService {

	private languages: string[] = ['es-AR', 'en-US'];

	constructor(private http: HttpClient) {
	}

	getCurrentLanguage(): Observable<any> {
		return of(this.languages);
		// TODO habilitar cuando se termine el kickoff del backend
		/*return this.http.get<any>(
			environment.apiBase + `/i18n/support-languages`
		);*/
	}
}

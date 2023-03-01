import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root',
})
export class EnvironmentVariableService {

	constructor(
		private http: HttpClient,
	) {}

	getDigitalRecipeDomainNumber(): Observable<number> {
		const url = `${environment.apiBase}/environment/variable/digital-recipe-domain-number`;
		return this.http.get<number>(url);
	}

}

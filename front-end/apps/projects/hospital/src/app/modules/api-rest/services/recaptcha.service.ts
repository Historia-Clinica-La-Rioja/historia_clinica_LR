import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class RecaptchaService {

	constructor(private http: HttpClient) {}

	isRecaptchaEnable(): Observable<boolean> {
		return this.http.get<boolean>(`${environment.apiBase}/recaptcha/is-enable`);
	}

}

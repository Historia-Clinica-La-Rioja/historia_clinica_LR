import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PasswordTokenExpirationService {

  constructor(private http: HttpClient) { }

	get(): Observable<number>{
		const url = `${environment.apiBase}/auth/password-token-expiration`;
		return this.http.get<number>(url);
	}
}

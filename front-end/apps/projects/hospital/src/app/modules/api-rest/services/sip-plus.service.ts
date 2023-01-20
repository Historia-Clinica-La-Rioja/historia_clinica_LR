import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SipPlusService {

  constructor(private readonly http: HttpClient) { }

  private baseUrl = `${environment.apiBase}/sip-plus`;

  getSipPlusURLBase(): Observable<string>{
	const url = this.baseUrl + '/url-base';
	return this.http.get<string>(url, {responseType: 'text' as 'json'})
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SipPlusUrlDataDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SipPlusService {

  constructor(private readonly http: HttpClient) { }

  private baseUrl = `${environment.apiBase}/sip-plus`;

  getInfoSipPlus(): Observable<SipPlusUrlDataDto>{
	const url = this.baseUrl + '/url-info';
	return this.http.get<SipPlusUrlDataDto >(url)
  }
}

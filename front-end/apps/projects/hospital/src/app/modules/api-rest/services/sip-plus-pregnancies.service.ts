import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SipPlusPregnanciesService {

  constructor(private readonly http: HttpClient,private readonly contextService: ContextService) { }

  private baseUrl = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/`;

  getPregnancies(patientId:number): Observable<string[]> {
	const url = this.baseUrl +patientId+'/sip-plus/pregnancies';
	return this.http.get<string[]>(url);
  }

  createPregnancy(patientId:number,valuePregnancy:number){
	const url = this.baseUrl +patientId+'/sip-plus/pregnancies/'+valuePregnancy;
	return this.http.post(url, undefined);
  }

}

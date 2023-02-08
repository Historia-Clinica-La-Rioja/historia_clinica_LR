import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SipPlusMotherService {

	constructor(private readonly http: HttpClient,private readonly contextService: ContextService) { }

	private baseUrl = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/`;

	createMother(patientId:number,valuePregnancy:number){
		const url = this.baseUrl +patientId+'/sip-plus/mother/'+valuePregnancy;
		return this.http.post(url,undefined);
	}
}

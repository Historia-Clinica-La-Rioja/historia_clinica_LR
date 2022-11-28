import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ProfessionalLicenseNumberDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ProfessionalLicenseService {
	private URL_PREFIX = `${environment.apiBase}/institution/${this.contextService.institutionId}/professional-license-number/healthcareprofessional/`;

	constructor(private http: HttpClient, private contextService: ContextService) { }

	saveProfessionalLicensesNumber(healthcareProfessionalId: number, professionalLicenses: ProfessionalLicenseNumberDto[]): Observable<number> {
		const url = this.URL_PREFIX + `${healthcareProfessionalId}`;
		return this.http.post<number>(url, professionalLicenses);
	}

	getLicenseNumberByProfessional(healthcareProfessionalId: number): Observable<ProfessionalLicenseNumberDto[]> {
		const url =  this.URL_PREFIX + `${healthcareProfessionalId}`;
		return this.http.get<ProfessionalLicenseNumberDto[]>(url);
	}
}

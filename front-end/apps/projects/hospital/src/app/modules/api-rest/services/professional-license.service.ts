import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
	LicenseDataDto, LicenseNumberTypeDto,
	ProfessionalLicenseNumberDto,
	ProfessionalRegistrationNumbersDto,
	ValidatedLicenseDataDto
} from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ProfessionalLicenseService {
	private URL_PREFIX: string;

	constructor(private http: HttpClient, private readonly contextService: ContextService) { 
		this.contextService.institutionId$.subscribe(institutionId => 
            this.URL_PREFIX = `${environment.apiBase}/institution/${institutionId}/professional-license-number/healthcareprofessional`
        )
    }

	saveProfessionalLicensesNumber(healthcareProfessionalId: number, professionalLicenses: ProfessionalLicenseNumberDto[]): Observable<number> {
		const url = this.URL_PREFIX + `/${healthcareProfessionalId}`;
		return this.http.post<number>(url, professionalLicenses);
	}

	removeProfessionalLicenseNumber(healthcareProfessionalId: number, professionalLicenses: ProfessionalLicenseNumberDto): Observable<number> {
		const url = this.URL_PREFIX + `/${healthcareProfessionalId}/delete`;
		return this.http.post<number>(url, professionalLicenses);
	}

	getLicenseNumberByProfessional(healthcareProfessionalId: number): Observable<ProfessionalLicenseNumberDto[]> {
		const url =  this.URL_PREFIX + `/${healthcareProfessionalId}`;
		return this.http.get<ProfessionalLicenseNumberDto[]>(url);
	}

	validateLicenseNumber(healthcareProfessionalId: number, licenseNumbers: LicenseDataDto[]): Observable<ValidatedLicenseDataDto[]> {
		const url =  this.URL_PREFIX + `/${healthcareProfessionalId}/validate`;
		return this.http.post<ValidatedLicenseDataDto[]>(url, licenseNumbers);
	}

	getAllProfessionalRegistrationNumbers(): Observable<ProfessionalRegistrationNumbersDto[]> {
		const url = this.URL_PREFIX + `/registration-numbers`;
		return this.http.get<ProfessionalRegistrationNumbersDto[]>(url);
	}

	getLicensesType(): Observable<LicenseNumberTypeDto[]> {
		const url = this.URL_PREFIX + `/masterdata`;
		return this.http.get<LicenseNumberTypeDto[]>(url);
	}

}

import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GetSanitaryResponsibilityAreaInstitutionAddressDto, GlobalCoordinatesDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  	providedIn: 'root'
})
export class GisService {

	private readonly URL_PREFIX = `${environment.apiBase}/institution`;
	institutionId: number;

  	constructor(private readonly http: HttpClient,
				private readonly contextService: ContextService) {}

	getInstitutionAddressById = (): Observable<GetSanitaryResponsibilityAreaInstitutionAddressDto> => {
		const url = `${this.URL_PREFIX}/${this.contextService.institutionId}/sanitary-responsibility-area/get-institution-address`;
		return this.http.get<GetSanitaryResponsibilityAreaInstitutionAddressDto>(url);
	}

	getInstitutionCoordinatesFromAddress = (address: string): Observable<GlobalCoordinatesDto> => {
		const url = `${this.URL_PREFIX}/sanitary-responsibility-area/get-global-coordinates-by-address`;
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('address', address);
		return this.http.get<GlobalCoordinatesDto>(url, {params: queryParams});
	}

	getInstitutionCoordinatesByInstitutionId = (): Observable<GlobalCoordinatesDto> => {
		const url = `${this.URL_PREFIX}/${this.contextService.institutionId}/sanitary-responsibility-area/get-institution-global-coordinates`;
		return this.http.get<GlobalCoordinatesDto>(url);
	}
}

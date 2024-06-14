import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GetSanitaryResponsibilityAreaInstitutionAddressDto, GlobalCoordinatesDto, SaveInstitutionAddressDto, SaveInstitutionResponsibilityAreaDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  	providedIn: 'root'
})
export class GisService {

	private readonly URL_PREFIX = `${environment.apiBase}/institution`;

  	constructor(private readonly http: HttpClient,
				private readonly contextService: ContextService) {}

	getInstitutionAddressById = (): Observable<GetSanitaryResponsibilityAreaInstitutionAddressDto> => {
		const url = `${this.URL_PREFIX}/${this.contextService.institutionId}/sanitary-responsibility-area/get-institution-address`;
		return this.http.get<GetSanitaryResponsibilityAreaInstitutionAddressDto>(url);
	}

	getInstitutionCoordinatesFromAddress = (address: string): Observable<GlobalCoordinatesDto> => {
		const url = `${environment.apiBase}/sanitary-responsibility-area/get-global-coordinates-by-address`;
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('address', address);
		return this.http.get<GlobalCoordinatesDto>(url, {params: queryParams});
	}

	getInstitutionCoordinatesByInstitutionId = (): Observable<GlobalCoordinatesDto> => {
		const url = `${this.URL_PREFIX}/${this.contextService.institutionId}/sanitary-responsibility-area/get-institution-global-coordinates`;
		return this.http.get<GlobalCoordinatesDto>(url);
	}

	saveInstitutionAddress = (address: SaveInstitutionAddressDto): Observable<number> => {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/sanitary-responsibility-area/save-institution-address`;
		return this.http.post<number>(url, address);
	}

	saveInstitutionCoordinates = (coordinates: GlobalCoordinatesDto): Observable<number> => {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/sanitary-responsibility-area/save-institution-global-coordinates`;
		return this.http.post<number>(url, coordinates);
	}

	saveInstitutionArea = (area: SaveInstitutionResponsibilityAreaDto): Observable<number> => {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/sanitary-responsibility-area/save-institution-responsibility-area`;
		return this.http.post<number>(url, area);
	}
}

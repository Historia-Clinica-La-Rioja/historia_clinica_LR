import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '@environments/environment';
import { UIMenuItemDto, UIPageDto } from '@api-rest/api-model';

const EXTENSION_URL = `${environment.apiBase}/extensions`;

@Injectable({
	providedIn: 'root'
})
export class ExtensionsService {

	constructor(
		private http: HttpClient,
	) { }

	getSystemMenu(): Observable<UIMenuItemDto[]> {
		const systemMenuUrl = `${EXTENSION_URL}/menu`;
		return this.http.get<UIMenuItemDto[]>(systemMenuUrl);
	}

	getSystemPage(menuId: string): Observable<UIPageDto> {
		const systemPageUrl = `${EXTENSION_URL}/page/${menuId}`;
		return this.http.get<UIPageDto>(systemPageUrl);
	}

	getInstitutionMenu(institutionId: number): Observable<UIMenuItemDto[]> {
		const institutionMenuUrl = `${EXTENSION_URL}/institution/${institutionId}/menu`;
		return this.http.get<UIMenuItemDto[]>(institutionMenuUrl);
	}

	getInstitutionPage(institutionId: number, menuId: string): Observable<UIPageDto> {
		const institutionPageUrl = `${EXTENSION_URL}/institution/${institutionId}/page/${menuId}`;
		return this.http.get<UIPageDto>(institutionPageUrl);
	}

	getPatientMenu(patientId: number): Observable<UIMenuItemDto[]> {
		const patientMenuUrl = `${EXTENSION_URL}/patient/${patientId}/menu`;
		return this.http.get<UIMenuItemDto[]>(patientMenuUrl);
	}

	getPatientPage(patientId: number, menuId: string): Observable<UIPageDto> {
		const patientPageUrl = `${EXTENSION_URL}/patient/${patientId}/page/${menuId}`;
		return this.http.get<UIPageDto>(patientPageUrl);
	}

}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '@environments/environment';
import { UIMenuItemDto, UIPageDto } from '@extensions/extensions-model';

export const EXTENSION_URL = `${environment.apiBase}/extensions`;

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

	getInstitutionMenu(institutionId: number): Observable<UIMenuItemDto[]> {
		const institutionMenuUrl = `${EXTENSION_URL}/institution/${institutionId}/menu`;
		return this.http.get<UIMenuItemDto[]>(institutionMenuUrl);
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

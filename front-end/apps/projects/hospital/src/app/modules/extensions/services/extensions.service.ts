import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { ExtensionComponentDto, UIMenuItemDto, UIPageDto } from '@extensions/extensions-model';
import { HttpErrorResponse } from '@angular/common/http';
import { WCInfo } from './wc-extensions.service';


export const EXTENSION_URL = `${environment.apiBase}/extensions`;

const handleError = <T>(response: T) => (error: HttpErrorResponse): Observable<T> => {
	if (error.status === 0) {
		// A client-side or network error occurred. Handle it accordingly.
		console.error('An error occurred:', error.error);
	} else {
		// The backend returned an unsuccessful response code.
		// The response body may contain clues as to what went wrong.
		console.error(`Backend returned code ${error.status}, body was: `, error.error);
	}
	// Return an observable with a user-facing error message.
	return of(response);
}


@Injectable({
	providedIn: 'root'
})
export class ExtensionsService {

	constructor(
		private http: HttpClient,
	) { }

	getSystemMenu(): Observable<UIMenuItemDto[]> {
		const systemMenuUrl = `${EXTENSION_URL}/menu`;
		return this.http.get<UIMenuItemDto[]>(systemMenuUrl).pipe(
			catchError(handleError([]))
		);
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

	getExtensions(): Observable<ExtensionComponentDto[]> {
		return this.http.get<ExtensionComponentDto[]>(EXTENSION_URL);
	}

	getDefinition(url: string): Observable<WCInfo[]> {
		return this.http.get<WCInfo[]>(url);
	}

}

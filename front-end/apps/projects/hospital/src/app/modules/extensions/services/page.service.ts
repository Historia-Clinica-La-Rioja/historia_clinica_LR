import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { EXTENSION_URL } from './extensions.service';
import { UIPageDto } from '@extensions/extensions-model';
import { HttpClient } from '@angular/common/http';

@Injectable({
	providedIn: 'root'
})
export class PageService {

	constructor(
		private http: HttpClient,
	) { }

	getSystemPage(menuId: string): Observable<UIPageDto> {
		const systemPageUrl = `${EXTENSION_URL}/page/${menuId}`;
		return this.http.get<UIPageDto>(systemPageUrl);
	}

	getInstitutionPage(institutionId: number, menuId: string): Observable<UIPageDto> {
		const institutionPageUrl = `${EXTENSION_URL}/institution/${institutionId}/page/${menuId}`;
		return this.http.get<UIPageDto>(institutionPageUrl);
	}
}

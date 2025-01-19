import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, } from 'rxjs';

import { environment } from '@environments/environment';
import { ExtensionComponentDto } from '@extensions/extensions-model';
import { WCInfo } from './wc-extensions.service';


export const EXTENSION_URL = `${environment.apiBase}/extensions`;

@Injectable({
	providedIn: 'root'
})
export class ExtensionsService {

	constructor(
		private http: HttpClient,
	) { }

	getExtensions(): Observable<ExtensionComponentDto[]> {
		return this.http.get<ExtensionComponentDto[]>(EXTENSION_URL);
	}

	getDefinition(url: string): Observable<WCInfo[]> {
		return this.http.get<WCInfo[]>(url + `?ts=${Date.now()}`);
	}

}

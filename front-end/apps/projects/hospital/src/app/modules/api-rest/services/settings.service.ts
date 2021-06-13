import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class SettingsService {

	constructor(
		private readonly http: HttpClient,
	) { }

	private baseUrl = `${environment.apiBase}/settings/assets/`;

	uploadFile(fileName: string, file: File): Observable<boolean> {
		const url = `${this.baseUrl}${fileName}`;
		const filesFormdata = new FormData();
		filesFormdata.append('file', file);
		return this.http.post<boolean>(url, filesFormdata);
	}

	deleteFile(fileName: string): Observable<boolean> {
		const url = `${this.baseUrl}${fileName}`;
		return this.http.delete<boolean>(url);
	}
}

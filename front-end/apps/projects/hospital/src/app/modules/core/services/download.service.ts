import { Injectable } from '@angular/core';
import { saveAs } from 'file-saver';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
	providedIn: 'root'
})
export class DownloadService {

	constructor(
		private http: HttpClient,
	) {	}

	downloadXlsWithRequestParams(url: string, fileName: string, params: any): Observable<any> {
		const httpOptions = {
			responseType  : 'arraybuffer' as 'json',
			params: params
		};
		return this.http.get<any>(url, httpOptions).pipe(
			tap((data: any) => {
				const blobType = { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' };
				const file = new Blob([data], blobType);
				saveAs(file, fileName);
			})
		);
	}

	downloadPdf(url: string, fileName: string): Observable<any> {
		const httpOptions = {
			responseType  : 'arraybuffer' as 'json'
		};
		return this.http.get<any>(url, httpOptions).pipe(
			tap((data: any) => {
				const blobType = { type: 'application/pdf' };
				const file = new Blob([data], blobType);
				saveAs(file, fileName);
			})
		);
	}

	downloadPdfWithRequestParams(url: string, fileName: string, params: any): Observable<any> {
		const httpOptions = {
			responseType  : 'arraybuffer' as 'json',
			params
		};
		return this.http.get<any>(url, httpOptions).pipe(
			tap((data: any) => {
				const blobType = { type: 'application/pdf' };
				const file = new Blob([data], blobType);
				saveAs(file, fileName);
			})
		);
	}

}

import { Injectable } from '@angular/core';
import { saveAs } from 'file-saver';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { FileFetchService } from '@api-rest/services/binary/file-fetch.service';
import { ViewPdfService } from '@presentation/dialogs/view-pdf/view-pdf.service';
import { FileRequest } from '@api-rest/services/binary/file-download.model';

@Injectable({
	providedIn: 'root'
})
export class DownloadService {

	constructor(
		private http: HttpClient,
		private fileFetchService: FileFetchService,
		private viewPdfService: ViewPdfService,
	) {	}

	fetchFile(url: string, filename: string, params: Record<string, string | number | boolean> = {}): Observable<void> {
		const fileToView: FileRequest = {
			filename,
			downloadStatus: this.fileFetchService.downloadRequestParams(url, params),
		};
		return this.viewPdfService.showFile(fileToView);
	}

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

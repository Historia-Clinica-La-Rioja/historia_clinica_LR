import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { DownloadService } from '@core/services/download.service';

@Injectable({
	providedIn: 'root'
})
export class ReferenceFileService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
		private readonly downloadService: DownloadService,
	) { }

	uploadReferenceFiles(patientId: number, referenceFiles: File[]): Observable<number[]> {
		const filesFormdata = new FormData();
		Array.from(referenceFiles).forEach(file => filesFormdata.append('files', file));
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/reference-file/patient/${patientId}/uploadFiles`;
		return this.http.post<number[]>(url, filesFormdata);
	}

	deleteReferenceFiles(fileIds: number[]): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/reference-file/delete`;
		return this.http.delete<boolean>(url, {
			params: {
				fileIds
			}
		});

	}

	downloadReferenceFiles(fileId: number, fileName: string): void {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/reference-file/download/${fileId}`;
		this.downloadService.downloadPdf(
			url,
			fileName,
		).subscribe();
	}

}

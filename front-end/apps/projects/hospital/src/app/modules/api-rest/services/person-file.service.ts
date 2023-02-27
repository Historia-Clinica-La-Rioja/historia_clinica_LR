import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { DownloadService } from '@core/services/download.service';

@Injectable({
	providedIn: 'root'
})
export class PersonFileService {

	constructor(private http: HttpClient,
		private readonly contextService: ContextService,
		private readonly downloadService: DownloadService,
	) {
	}

	downloadFile(fileId: number, personId: number, fileName: string) {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/person/${personId}/file/download/${fileId}`;
		this.downloadService.downloadPdf(
			url,
			fileName,
		)
	}

	saveFiles(personId: number, files: File[]): Observable<number[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/person/${personId}/file/upload`;
		const filesFormdata = new FormData();
		Array.from(files).forEach(file => filesFormdata.append('files', file));
		if (filesFormdata.get("files")) {
			return this.http.post<number[]>(url, filesFormdata)
		}
	}
}

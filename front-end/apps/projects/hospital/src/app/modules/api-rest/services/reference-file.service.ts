import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { saveAs } from 'file-saver';

@Injectable({
	providedIn: 'root'
})
export class ReferenceFileService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	uploadReferenceFiles(patientId: number, referenceFile: File): Observable<number> {
		const fileFormdata = new FormData();
		fileFormdata.append('file', referenceFile);
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/reference-file/patient/${patientId}/uploadFile`;
		return this.http.post<number>(url, fileFormdata);
	}

	deleteReferenceFiles(fileIds: number[]): Observable<any> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/reference-file/delete`;
		return this.http.delete(url, {
			params: {
				fileIds
			}
		});

	}

	downloadReferenceFiles(fileId: number, fileName: string): void {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/reference-file/download/${fileId}`;
		this.http.get(url,
			{ responseType: 'blob' }
		).subscribe(blob => saveAs(blob, fileName));
	}
}

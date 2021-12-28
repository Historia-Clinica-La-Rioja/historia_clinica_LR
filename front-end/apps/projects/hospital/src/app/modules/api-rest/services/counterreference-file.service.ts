import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { saveAs } from 'file-saver';



@Injectable({
	providedIn: 'root'
})
export class CounterreferenceFileService {

	private URL_PREFIX = `${environment.apiBase}/institutions/${this.contextService.institutionId}/counterreference-file/`;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	uploadCounterreferenceFiles(patientId: number, counterreferenceFile: File): Observable<number> {
		const fileFormdata = new FormData();
		fileFormdata.append('file', counterreferenceFile);
		const url = this.URL_PREFIX + `patient/${patientId}/uploadFile`;
		return this.http.post<number>(url, fileFormdata);
	}

	deleteCounterreferenceFiles(filesIds: number[]): Observable<any> {
		const params = { fileIds: filesIds };
		const url = this.URL_PREFIX + `delete`;
		return this.http.delete(url, { params });
	}

	downloadCounterreferenceFiles(fileId: number, fileName: string): void {
		const url = this.URL_PREFIX + `download/${fileId}`;
		this.http.get(url,
			{ responseType: 'blob' }
		).subscribe(blob => saveAs(blob, fileName));
	}
}
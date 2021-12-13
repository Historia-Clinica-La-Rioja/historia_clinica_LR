import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class CounterreferenceFileService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	uploadCounterreferenceFiles(patientId: number, counterreferenceFile: File): Observable<number> {
		const fileFormdata = new FormData();
		fileFormdata.append('file', counterreferenceFile);
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/counterreference-file/patient/${patientId}/uploadFile`;
		return this.http.post<number>(url, fileFormdata);
	}

	deleteCounterreferenceFiles(patientId: number, filesIds: number[]): Observable<any> {
		const url =  `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/counterreference-file/delete`;
		return this.http.delete(url, {
			params: {
				fileIds: filesIds
			}
		});
	}
}
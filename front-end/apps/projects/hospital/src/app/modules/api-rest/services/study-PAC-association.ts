import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { saveAs } from 'file-saver';
import { Injectable } from '@angular/core';
import { PacsListDto, StudyFileInfoDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable, catchError, tap } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class StudyPACAssociationService {

	private readonly BASE_URL: string;

	constructor(
		private readonly contextService: ContextService,
		private http: HttpClient,
	) {
		this.BASE_URL = `${environment.apiBase}/institutions/${this.contextService.institutionId}/imagenetwork`
	 }

	getPacGlobalURL(studyInstanceUID: string): Observable<PacsListDto> {
		const url = `${this.BASE_URL}/${studyInstanceUID}/pacs`
		return this.http.get<PacsListDto>(url);
	}

	getStudyInfo(studyInstanceUID: string, pacs: PacsListDto): Observable<StudyFileInfoDto> {
		const url = `${this.BASE_URL}/${studyInstanceUID}/file-info`
		return this.http.post<StudyFileInfoDto>(url, pacs);
	}

	downloadStudy(studyUUID: string, studyInstanceUID: string, pacUrl: string, token: string): Observable<any> {
		const customHeaders = {
		  'Authorization': `Bearer ${token}`,
		  'Accept': 'application/zip'
		};
	  
		const headers = new HttpHeaders(customHeaders);
		const url = `${pacUrl}/${studyInstanceUID}/studies/${studyUUID}/archive`;
	  
		return this.http.get(url, { headers, observe: 'response', responseType: 'arraybuffer' })
		  .pipe(
			catchError((error: any) => {
			  console.error('Error en la solicitud:', error);
			  throw error;
			}),
			tap((response: HttpResponse<ArrayBuffer>) => {
			  const blobType = { type: 'application/zip' };
			  const file = new Blob([response.body], blobType);
			  saveAs(file, `${studyInstanceUID}.zip`);
			})
		  );
	  }
}
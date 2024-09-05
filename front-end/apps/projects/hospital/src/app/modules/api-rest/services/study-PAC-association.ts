import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ErrorDownloadStudyDto, PacsListDto, StudyFileInfoDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

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

    saveError(response: any, studyInfo: StudyFileInfoDto, studyInstanceUID: string){
        const url = `${this.BASE_URL}/${studyInstanceUID}/download-error`;
        const studyDownloadError: ErrorDownloadStudyDto = {
            effectiveTime: response.Timestamp,
            errorCode: response.ErrorCode,
            errorCodeDescription: response.ErrorDescription,
            fileUuid: studyInfo.uuid,
            pacServerId: studyInfo.pacServerId,
        }
        return this.http.put<ErrorDownloadStudyDto>(url, { studyDownloadError });
    }

    downloadStudy(studyInfo: StudyFileInfoDto, studyInstanceUID: string): Observable<any> {
        const customHeaders = {
            'Authorization': `Bearer ${studyInfo.token}`,
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        };
        const headers = new HttpHeaders(customHeaders);
        const url = `${studyInfo.url}/${studyInstanceUID}/studies/${studyInfo.uuid}/archive`;
        
        return this.http.post(url, { "Asynchronous": true, "Priority": 0, "Synchronous": false }, { headers: headers });
    }

    getJobStatus(url: string, studyInstanceUID: string, jobId: number, token: string): Observable<any>{
        const customHeaders = {
            'Authorization': `Bearer ${token}`,
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        };
        const headers = new HttpHeaders(customHeaders);
        const getJobURl = `${url}/${studyInstanceUID}/jobs/${jobId}`;
        return this.http.get(getJobURl, { headers: headers });
    }

    getZip(url: string, studyInstanceUID: string, jobId: number, token: string): Observable<Blob> {
        const customHeaders = {
            'Authorization': `Bearer ${token}`,
            'Accept': 'application/zip',
            'Content-Type': 'application/octet-stream',
        };
        const headers = new HttpHeaders(customHeaders);
        const getJobOutputUrl = `${url}/${studyInstanceUID}/jobs/${jobId}/archive`;
        return this.http.get(getJobOutputUrl, { headers: headers, responseType: 'blob' })
    }

    deleteJob() {
        // El pacs no permite hacer un DELETE por lo que este llamado devuelve 405 - queda comentado hasta encontrar la solución
        /* const customHeaders = {
            'Authorization': `Bearer ${this.token}`,
        };
        this.headers = new HttpHeaders(customHeaders);
        this.http.delete(`${this.pacUrl}/${this.studyInstanceUID}/jobs/${this.jobId}`, { headers: this.headers }).subscribe(); */
    }

    cancelActiveJob(url: string, studyInstanceUID: string, jobId: number, token: string): Observable<any> {
        const customHeaders = {
            'Authorization': `Bearer ${token}`,
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        };
        const headers = new HttpHeaders(customHeaders);
        return this.http.post(`${url}/${studyInstanceUID}/jobs/${jobId}/cancel`, {}, { headers: headers });
    }
}
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { saveAs } from 'file-saver';
import { Injectable } from '@angular/core';
import { ErrorDownloadStudyDto, PacsDto, StudyFileInfoDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { DownloadStatusPopupComponent } from '../../image-network/dialogs/download-status-popup/download-status-popup.component';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { MatDialogRef } from '@angular/material/dialog';
import { StudyStatusPopupComponent } from '../../image-network/dialogs/study-status-popup/study-status-popup.component';
import { TranslateService } from '@ngx-translate/core';

const INTERVAL_TIME = 5000;
@Injectable({
    providedIn: 'root'
})
export class StudyPACAssociationService {

    private readonly BASE_URL: string;
    private titleSubject = new BehaviorSubject<string>('image-network.worklist.details_study.asynchronous_download.REQUESTING_STUDY');
    title$: Observable<string> = this.titleSubject.asObservable();
    private subtitleSubject = new BehaviorSubject<string>('image-network.worklist.details_study.asynchronous_download.REQUEST_WAIT');
    subtitle$: Observable<string> = this.subtitleSubject.asObservable();
    private errorSubject = new BehaviorSubject<boolean>(false);
    error$: Observable<boolean> = this.errorSubject.asObservable();
    private canBeCancelledSubject = new BehaviorSubject<boolean>(true);
    canBeCancelled$: Observable<boolean> = this.canBeCancelledSubject.asObservable();
    interval: NodeJS.Timeout;

    private headers: HttpHeaders;
    private studyInstanceUID: string;
    private getJobURl: string;
    private jobId : number;
    private studyInfo: StudyFileInfoDto;
    private dialogRef: MatDialogRef<DownloadStatusPopupComponent>;

    constructor(
        private readonly contextService: ContextService,
        private http: HttpClient,
        private readonly dialogService: DialogService<any>,
        private readonly translateService: TranslateService,
    ) {
        this.BASE_URL = `${environment.apiBase}/institutions/${this.contextService.institutionId}/imagenetwork`
    }

    getPacGlobalURL(studyInstanceUID: string): Observable<PacsDto> {
        const url = `${this.BASE_URL}/${studyInstanceUID}/pacs`
        return this.http.get<PacsDto>(url);
    }

    getStudyInfo(studyInstanceUID: string, pacs: PacsDto): Observable<StudyFileInfoDto> {
        const url = `${this.BASE_URL}/${studyInstanceUID}/file-info`
        return this.http.post<StudyFileInfoDto>(url, pacs);
    }

    saveError(response: any){
        const url = `${this.BASE_URL}/${this.studyInstanceUID}/download-error`;
        let studyDownloadError: ErrorDownloadStudyDto = {
            effectiveTime: response.Timestamp,
            errorCode: response.ErrorCode,
            errorCodeDescription: response.ErrorDescription,
            fileUuid: this.studyInfo.uuid,
            pacServerId: this.studyInfo.pacServerId,
        }
        return this.http.put<ErrorDownloadStudyDto>(url, { studyDownloadError });
    }

    downloadStudy(studyInfo: StudyFileInfoDto, studyInstanceUID: string): Observable<any> {
        this.studyInfo = studyInfo;
        this.studyInstanceUID = studyInstanceUID;
        const customHeaders = {
            'Authorization': `Bearer ${this.studyInfo.token}`,
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        };
        this.headers = new HttpHeaders(customHeaders);
        const url = `${this.studyInfo.url}/${studyInstanceUID}/studies/${this.studyInfo.uuid}/archive`;
        
        return this.http.post(url, { "Asynchronous": true, "Priority": 0, "Synchronous": false }, { headers: this.headers })
            .pipe(tap((response: any) => {
                    this.openDownloadStatusDialog();
                    this.canBeCancelledSubject.next(true);
                    this.jobId = response.ID;
                    this.getJobURl = `${this.studyInfo.url}/${studyInstanceUID}/jobs/${this.jobId}`;
                    this.titleSubject.next('image-network.worklist.details_study.asynchronous_download.SETTING_UP_STUDY');
                    this.interval = setInterval(this.askForJobStatus,  INTERVAL_TIME);
                })
            );
    }

    private openDownloadStatusDialog(){
        this.dialogRef = this.dialogService.open(
            DownloadStatusPopupComponent,
            { dialogWidth: DialogWidth.SMALL },
            { 
                title: 'image-network.worklist.details_study.asynchronous_download.REQUESTING_STUDY', 
                subtitle: "image-network.worklist.details_study.asynchronous_download.REQUEST_WAIT"
            }
        );
        this.dialogRef.afterClosed().subscribe((cancelled) => {
            clearInterval(this.interval);
            if(cancelled){
                this.cancelActiveJob();
            }
        })
    }

    askForJobStatus = () => {
        if (this.getJobURl) {
            this.http.get(this.getJobURl, { headers: this.headers }).subscribe((response: any) => {
                if (response.State === "Success") {
                    this.handleJobStatusSuccess();
                }
                if (response.State === "Failure") {
                    this.handleJobStatusError(response);
                }
            })
        }
    }

    handleJobStatusSuccess() {
        this.canBeCancelledSubject.next(false);
        clearInterval(this.interval);
        const customHeaders = {
            'Authorization': `Bearer ${this.studyInfo.token}`,
            'Accept': 'application/zip',
            'Content-Type': 'application/octet-stream',
        };
        this.headers = new HttpHeaders(customHeaders);
        const getJobOutputUrl = `${this.studyInfo.url}/${this.studyInstanceUID}/jobs/${this.jobId}/archive`;
        this.http.get(getJobOutputUrl, { headers: this.headers, responseType: 'blob' }).subscribe((jobOutput: Blob) => {
            this.manageDialogs();
            saveAs(jobOutput, `${this.studyInstanceUID}.zip`);
        })
    }

    handleJobStatusError(response: any) {
        let studyError = this.translateService.instant('image-network.worklist.details_study.asynchronous_download.STUDY_NOT_AVAILABLE');
        this.errorSubject.next(true);
        this.titleSubject.next(`${studyError} - Error ${response.ErrorCode}`);
        this.subtitleSubject.next('image-network.worklist.details_study.asynchronous_download.DOWNLOAD_FAILED');
        this.saveError(response);
    }

    private deleteJob() {
        // El pacs no permite hacer un DELETE por lo que este llamado devuelve 405 - queda comentado hasta encontrar la solución
        /* const customHeaders = {
            'Authorization': `Bearer ${this.token}`,
        };
        this.headers = new HttpHeaders(customHeaders);
        this.http.delete(`${this.pacUrl}/${this.studyInstanceUID}/jobs/${this.jobId}`, { headers: this.headers }).subscribe(); */
    }

    private manageDialogs() {
        this.dialogRef.close();
        const successDialogRef = this.dialogService.open(
            StudyStatusPopupComponent,
            { dialogWidth: DialogWidth.SMALL },
            {
                title: 'image-network.worklist.details_study.DOWNLOAD_STUDY',
                icon: 'error_outlined',
                iconColor: 'green',
                popUpMessage: 'Estudio listo para descargarse',
                popUpMessageTranslate: 'La descarga comenzará en algunos segundos',
                acceptBtn: false,
            }
        );
        successDialogRef.afterClosed().subscribe(() => {
            this.deleteJob();
        })
    }

    retryDownload() {
        this.dialogRef.close();
        this.downloadStudy(this.studyInfo, this.studyInstanceUID);
    }

    private cancelActiveJob() {
        if(this.jobId){
            this.http.post(`${this.studyInfo.url}/${this.studyInstanceUID}/jobs/${this.jobId}/cancel`, {}, { headers: this.headers }).subscribe( res => {
                this.dialogRef.close();
            });
        }
    }
}
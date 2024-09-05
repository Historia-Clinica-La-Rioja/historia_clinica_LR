import { Injectable } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { StudyFileInfoDto } from '@api-rest/api-model';
import { TranslateService } from '@ngx-translate/core';
import { saveAs } from 'file-saver';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { BehaviorSubject, Observable, take, tap } from 'rxjs';
import { DownloadStatusPopupComponent } from '../dialogs/download-status-popup/download-status-popup.component';
import { StudyPACAssociationService } from '@api-rest/services/study-PAC-association';
import { StudyStatusPopupComponent } from '../dialogs/study-status-popup/study-status-popup.component';

const INTERVAL_TIME = 5000;
@Injectable({
    providedIn: 'root'
})
export class DownloadStudyService {
    private titleSubject = new BehaviorSubject<string>('image-network.worklist.details_study.asynchronous_download.REQUESTING_STUDY');
    title$: Observable<string> = this.titleSubject.asObservable();
    private subtitleSubject = new BehaviorSubject<string>('image-network.worklist.details_study.asynchronous_download.REQUEST_WAIT');
    subtitle$: Observable<string> = this.subtitleSubject.asObservable();
    private errorSubject = new BehaviorSubject<boolean>(false);
    error$: Observable<boolean> = this.errorSubject.asObservable();
    private canBeCancelledSubject = new BehaviorSubject<boolean>(true);
    canBeCancelled$: Observable<boolean> = this.canBeCancelledSubject.asObservable();
    interval: NodeJS.Timeout;

    private studyInstanceUID: string;
    private jobId : number;
    private studyInfo: StudyFileInfoDto;
    private dialogRef: MatDialogRef<DownloadStatusPopupComponent>;

    constructor(
        private readonly dialogService: DialogService<any>,
        private readonly translateService: TranslateService,
        private readonly studyPACAssociationService: StudyPACAssociationService,
    ) {}

    downloadStudy(studyInfo: StudyFileInfoDto, studyInstanceUID: string): Observable<any> {
        this.studyInfo = studyInfo;
        this.studyInstanceUID = studyInstanceUID;
        
        return this.studyPACAssociationService.downloadStudy(this.studyInfo, this.studyInstanceUID).pipe(tap((response: any) => {
                this.openDownloadStatusDialog();
                this.canBeCancelledSubject.next(true);
                this.jobId = response.ID;

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
                initialTitle: 'image-network.worklist.details_study.asynchronous_download.REQUESTING_STUDY', 
                initialSubtitle: "image-network.worklist.details_study.asynchronous_download.REQUEST_WAIT"
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
        this.studyPACAssociationService.getJobStatus(this.studyInfo.url, this.studyInstanceUID, this.jobId, this.studyInfo.token).pipe(take(1))
            .subscribe((response: any) => {
                if (response.State === "Success") {
                    this.handleJobStatusSuccess();
                }
                if (response.State === "Failure") {
                    this.handleJobStatusError(response);
                }
            })
    }

    handleJobStatusSuccess() {
        this.canBeCancelledSubject.next(false);
        clearInterval(this.interval);
        this.studyPACAssociationService.getZip(this.studyInfo.url, this.studyInstanceUID, this.jobId, this.studyInfo.token).subscribe((jobOutput: Blob) => {
            this.manageDialogs();
            saveAs(jobOutput, `${this.studyInstanceUID}.zip`);
        })
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
            this.studyPACAssociationService.deleteJob();
        })
    }

    handleJobStatusError(response: any) {
        const studyError = this.translateService.instant('image-network.worklist.details_study.asynchronous_download.STUDY_NOT_AVAILABLE');
        this.errorSubject.next(true);
        this.titleSubject.next(`${studyError} - Error ${response.ErrorCode}`);
        this.subtitleSubject.next('image-network.worklist.details_study.asynchronous_download.DOWNLOAD_FAILED');
        this.studyPACAssociationService.saveError(response, this.studyInfo, this.studyInstanceUID);
    }

    private cancelActiveJob() {
        if(this.jobId){
            this.studyPACAssociationService.cancelActiveJob(this.studyInfo.url, this.studyInstanceUID, this.jobId, this.studyInfo.token).subscribe( () => {
                this.dialogRef.close();
            })
        }
    }

    retryDownload() {
        this.dialogRef.close();
        this.downloadStudy(this.studyInfo, this.studyInstanceUID);
    }
}

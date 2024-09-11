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
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';

const INTERVAL_TIME = 5000;
@Injectable({
    providedIn: 'root'
})
export class DownloadStudyService {
    private titleSubject = new BehaviorSubject<string>('image-network.worklist.details_study.asynchronous_download.SETTING_UP_STUDY');
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
        
        return this.studyPACAssociationService.downloadStudy(this.studyInfo, this.studyInstanceUID).pipe(take(1), tap((response: any) => {
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
            .subscribe({
                next: (response: any) => {
                    if (response.State === "Success") {
                        this.handleJobStatusSuccess();
                    }
                    if (response.State === "Failure") {
                        clearInterval(this.interval);
                        this.handleJobStatusError(response.ErrorCode);
                        this.studyPACAssociationService.saveError(response, this.studyInfo, this.studyInstanceUID).subscribe();
                    }
                },
                error: (e) => {
                    clearInterval(this.interval);
                    const error = {
                        ErrorCode: '001',
                        ErrorDescription: 'Connection failed',
                    }

                    this.studyPACAssociationService.saveError(error, this.studyInfo, this.studyInstanceUID).subscribe();
                    this.handleJobStatusError();
                }
            })
    }

    handleJobStatusSuccess() {
        this.canBeCancelledSubject.next(false);
        this.titleSubject.next('image-network.worklist.details_study.asynchronous_download.REQUESTING_STUDY');
        clearInterval(this.interval);
        this.studyPACAssociationService.getZip(this.studyInfo.url, this.studyInstanceUID, this.jobId, this.studyInfo.token).subscribe((jobOutput: Blob) => {
            this.manageDialogs();
            const blobType = { type: 'application/zip' };
            const file = new Blob([jobOutput], blobType);
            saveAs(file, `${this.studyInstanceUID}.zip`);
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

    handleJobStatusError(error?: string) {
        const studyError = this.translateService.instant('image-network.worklist.details_study.asynchronous_download.STUDY_NOT_AVAILABLE');
        this.errorSubject.next(true);
        const errorMessage = error ? `${studyError} - Error ${error}` : `${studyError}`;
        this.titleSubject.next(`${errorMessage}`);
        this.subtitleSubject.next('image-network.worklist.details_study.asynchronous_download.DOWNLOAD_FAILED');
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
        this.errorSubject.next(false);
        this.titleSubject.next('image-network.worklist.details_study.asynchronous_download.SETTING_UP_STUDY');
        this.subtitleSubject.next('image-network.worklist.details_study.asynchronous_download.REQUEST_WAIT');
        this.downloadStudy(this.studyInfo, this.studyInstanceUID).pipe(take(1)).subscribe({
            error: () => {
                this.dialogService.open(DiscardWarningComponent,
                    { dialogWidth: DialogWidth.SMALL }, {
                    data: this.getErrorDataDialog(),
                    minWidth: '30%'
                });
            }
        });
    }

    private getErrorDataDialog() {
        return {
            title: 'image-network.worklist.details_study.ERROR_DOWNLOAD_STUDY',
            content: '',
            okButtonLabel: 'buttons.ACCEPT',
            errorMode: true,
            color: 'warn'
        };
    }
}

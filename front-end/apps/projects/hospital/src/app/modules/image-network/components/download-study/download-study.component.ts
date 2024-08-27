import { TranslateService } from '@ngx-translate/core';
import { Component, Input } from '@angular/core';
import { PacsDto, StudyFileInfoDto } from '@api-rest/api-model';
import { MatDialog } from '@angular/material/dialog';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { StudyPACAssociationService } from '@api-rest/services/study-PAC-association';
import { catchError, switchMap } from 'rxjs';

@Component({
    selector: 'app-download-study',
    templateUrl: './download-study.component.html',
    styleUrls: ['./download-study.component.scss']
})
export class DownloadStudyComponent {

	@Input() studyAvailable?: boolean;
	@Input() imageId?: string;
    buttonText: String;
    isLoading: boolean = false;
    
    constructor(
        translateService: TranslateService,
		private readonly studyPACAssociationService: StudyPACAssociationService,
        public dialog: MatDialog
    ) {
        this.buttonText = translateService.instant('image-network.worklist.details_study.DOWNLOAD_STUDY').toUpperCase();
    }

    downloadStudy() {
        if (this.imageId){
            this.isLoading = true;
            this.studyPACAssociationService.getPacGlobalURL(this.imageId).pipe(
                switchMap((pacs: PacsDto) =>
                    this.studyPACAssociationService.getStudyInfo(this.imageId, pacs).pipe(
                        switchMap((studyInfo: StudyFileInfoDto) =>
                            this.studyPACAssociationService.downloadStudy(studyInfo, this.imageId).pipe(
                                catchError((error) => {
                                    throw error;
                                })
                            )
                        ),
                        catchError((error) => {
                            throw error;
                        })
                    )
                )
            ).subscribe({
                next: () => {
                    this.isLoading = false;
                },
                error: () => {
                    this.handleDownloadError();
                }
            });
        }
    }
                
    private handleDownloadError() {
        this.isLoading = false;
        this.dialog.open(DiscardWarningComponent, {
            data: this.getErrorDataDialog(),
            minWidth: '30%'
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
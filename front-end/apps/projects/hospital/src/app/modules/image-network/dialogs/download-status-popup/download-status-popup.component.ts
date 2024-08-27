import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { StudyPACAssociationService } from '@api-rest/services/study-PAC-association';
import { ButtonType } from '@presentation/components/button/button.component';

@Component({
    selector: 'app-download-status-popup',
    templateUrl: './download-status-popup.component.html',
    styleUrls: ['./download-status-popup.component.scss']
})
export class DownloadStatusPopupComponent implements OnInit {

    buttonType: ButtonType;
    title: string;
    subtitle: string;
    errorStatus = false;
    canBeCancelled = true;

    constructor(
        @Inject(MAT_DIALOG_DATA) public data: {
            title: string,
            subtitle: string
        },
        private readonly studyPacAssociationService: StudyPACAssociationService,
    ) { }

    ngOnInit(): void {
        this.title = this.data.title;
        this.subtitle = this.data.subtitle;
        this.subscribeToData();
    }

    subscribeToData() {
        this.studyPacAssociationService.title$.subscribe(title => {
            this.title = title;
        })
        this.studyPacAssociationService.subtitle$.subscribe(subtitle => {
            this.subtitle = subtitle;
        })
        this.studyPacAssociationService.error$.subscribe(error => {
            this.errorStatus = error;
        })
        this.studyPacAssociationService.canBeCancelled$.subscribe(canBeCancelled => {
            this.canBeCancelled = canBeCancelled;
        })
    }

    retryDownload() {
        this.studyPacAssociationService.retryDownload();
    }
}

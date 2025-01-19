import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ButtonType } from '@presentation/components/button/button.component';
import { DownloadStudyService } from '../../services/download-study.service';

@Component({
    selector: 'app-download-status-popup',
    templateUrl: './download-status-popup.component.html',
    styleUrls: ['./download-status-popup.component.scss']
})
export class DownloadStatusPopupComponent implements OnInit {

    ButtonType = ButtonType;
    title: string;
    subtitle: string;
    errorStatus = false;
    canBeCancelled = true;

    constructor(
        @Inject(MAT_DIALOG_DATA) public data: {
            initialTitle: string,
            initialSubtitle: string
        },
        private readonly downloadStudyService: DownloadStudyService,
    ) { }

    ngOnInit(): void {
        this.title = this.data.initialTitle;
        this.subtitle = this.data.initialSubtitle;
        this.subscribeToData();
    }

    subscribeToData() {
        this.downloadStudyService.title$.subscribe(title => {
            this.title = title;
        })
        this.downloadStudyService.subtitle$.subscribe(subtitle => {
            this.subtitle = subtitle;
        })
        this.downloadStudyService.error$.subscribe(error => {
            this.errorStatus = error;
        })
        this.downloadStudyService.canBeCancelled$.subscribe(canBeCancelled => {
            this.canBeCancelled = canBeCancelled;
        })
    }

    retryDownload() {
        this.downloadStudyService.retryDownload();
    }
}

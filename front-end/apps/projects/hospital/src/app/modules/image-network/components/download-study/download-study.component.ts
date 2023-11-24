import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { MatDialog } from '@angular/material/dialog';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';

@Component({
    selector: 'app-download-study',
    templateUrl: './download-study.component.html',
    styleUrls: ['./download-study.component.scss']
})
export class DownloadStudyComponent {

    buttonText: String;

    constructor(
        translateService: TranslateService,
        public dialog: MatDialog
    ) {
        this.buttonText = translateService.instant('image-network.worklist.details_study.DOWNLOAD_STUDY').toUpperCase();
    }

    downloadStudy() {
    //     descargarImagen.subscribe({
    //         next: () => ,
    //         error: () => {
                    this.dialog.open(DiscardWarningComponent, {
                        data: getErrorDataDialog(),
                        minWidth: '30%'
                    });

                    function getErrorDataDialog() {
                        return {
                            title: 'image-network.worklist.details_study.ERROR_DOWNLOAD_STUDY',
                            content: '',
                            okButtonLabel: 'buttons.ACCEPT',
                            errorMode: true,
                            color: 'warn'
                        };
                    }
    //         }
    //     });
    }
}

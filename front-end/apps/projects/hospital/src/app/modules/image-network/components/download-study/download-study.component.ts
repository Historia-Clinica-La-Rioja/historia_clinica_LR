import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-download-study',
    templateUrl: './download-study.component.html',
    styleUrls: ['./download-study.component.scss']
})
export class DownloadStudyComponent {

    buttonText: String;

    constructor(translateService: TranslateService) {
        this.buttonText = translateService.instant('image-network.worklist.details_study.DOWNLOAD_STUDY').toUpperCase();
    }

    downloadStudy() { }
}

import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IDENTIFIER_CASES } from 'projects/hospital/src/app/modules/hsi-components/identifier-cases/identifier-cases.component';
import { PersonalHistoryData } from '@historia-clinica/components/personal-histories-summary/personal-histories-summary.component';

@Component({
    selector: 'app-personal-history-view-details',
    templateUrl: './personal-history-view-details.component.html',
    styleUrls: ['./personal-history-view-details.component.scss']
})
export class PersonalHistoryViewDetailsComponent {
    
    identiferCases = IDENTIFIER_CASES;

    constructor(
        @Inject(MAT_DIALOG_DATA) public data: PersonalHistoryData) { }
}

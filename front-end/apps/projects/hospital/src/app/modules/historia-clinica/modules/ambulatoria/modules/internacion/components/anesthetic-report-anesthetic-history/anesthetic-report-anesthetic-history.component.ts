import { Component, Input, OnInit } from '@angular/core';
import { ANESTHESIA_ZONE_ID, AnestheticReportAnestheticHistoryService, PREVIOUS_ANESTHESIA_STATE_ID } from '../../services/anesthetic-report-anesthetic-history.service';
import { FormGroup } from '@angular/forms';
import { MatRadioChange } from '@angular/material/radio';

@Component({
    selector: 'app-anesthetic-report-anesthetic-history',
    templateUrl: './anesthetic-report-anesthetic-history.component.html',
    styleUrls: ['./anesthetic-report-anesthetic-history.component.scss']
})

export class AnestheticReportAnestheticHistoryComponent implements OnInit {

    @Input() service: AnestheticReportAnestheticHistoryService;
    form: FormGroup;
    viewZoneOptions = false;
    previousAnesthesiaStates = PREVIOUS_ANESTHESIA_STATE_ID;
    anesthesiaZone = ANESTHESIA_ZONE_ID;

    constructor() { }

    ngOnInit(): void {
        this.form = this.service.getForm();
    }

    onPreviousAnesthesiaStateSelected($event: MatRadioChange){
        $event.value === PREVIOUS_ANESTHESIA_STATE_ID.YES ? 
            this.viewZoneOptions = true : this.viewZoneOptions = false;
        this.service.setPreviousAnesthesiaData($event.value)
    }

    onAnesthesiaZoneSelected($event: MatRadioChange){
        this.service.setAnesthesiaZoneData($event.value)
    }

}
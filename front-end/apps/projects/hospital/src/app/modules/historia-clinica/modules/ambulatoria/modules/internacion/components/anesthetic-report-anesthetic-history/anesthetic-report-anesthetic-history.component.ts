import { Component, OnInit } from '@angular/core';
import { ANESTHESIA_ZONE_ID, PREVIOUS_ANESTHESIA_STATE_ID } from '../../services/anesthetic-report-anesthetic-history.service';
import { FormGroup } from '@angular/forms';
import { MatRadioChange } from '@angular/material/radio';
import { AnestheticReportService } from '../../services/anesthetic-report.service';

@Component({
    selector: 'app-anesthetic-report-anesthetic-history',
    templateUrl: './anesthetic-report-anesthetic-history.component.html',
    styleUrls: ['./anesthetic-report-anesthetic-history.component.scss']
})

export class AnestheticReportAnestheticHistoryComponent implements OnInit {

    form: FormGroup;
    viewZoneOptions = false;
    previousAnesthesiaStates = PREVIOUS_ANESTHESIA_STATE_ID;
    anesthesiaZone = ANESTHESIA_ZONE_ID;

    constructor(
        private readonly service: AnestheticReportService,
    ) { }

	ngOnInit(): void {
		this.form = this.service.anestheticReportAnestheticHistoryService.getForm();
		this.service.anestheticHistory$.subscribe(anestheticHistory => {
			if (anestheticHistory) {
				if (anestheticHistory.stateId === PREVIOUS_ANESTHESIA_STATE_ID.YES) {
					this.viewZoneOptions = true;
					this.service.anestheticReportAnestheticHistoryService.setData(anestheticHistory);
				} else {
					this.viewZoneOptions = false;
					this.service.anestheticReportAnestheticHistoryService.setData(anestheticHistory);
				}
			}
		});
	}

    onPreviousAnesthesiaStateSelected($event: MatRadioChange){
        $event.value === PREVIOUS_ANESTHESIA_STATE_ID.YES ?
            this.viewZoneOptions = true : this.viewZoneOptions = false;
        this.service.anestheticReportAnestheticHistoryService.setPreviousAnesthesiaData($event.value)
    }

    onAnesthesiaZoneSelected($event: MatRadioChange){
        this.service.anestheticReportAnestheticHistoryService.setAnesthesiaZoneData($event.value)
    }
}

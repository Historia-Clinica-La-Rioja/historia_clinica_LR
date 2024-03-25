import { Component, Input, OnInit } from '@angular/core';
import { AnestheticReportEndOfAnesthesiaStatusService, EndOfAnesthesiaRadioGroups, INTERNMENT_OPTIONS } from '../../services/anesthetic-report-end-of-anesthesia-status.service';
import { FormGroup } from '@angular/forms';
import { RadioGroupData } from '@presentation/components/radio-group/radio-group.component';
import { MatRadioChange } from '@angular/material/radio';

@Component({
    selector: 'app-anesthetic-report-end-of-anesthesia-status',
    templateUrl: './anesthetic-report-end-of-anesthesia-status.component.html',
    styleUrls: ['./anesthetic-report-end-of-anesthesia-status.component.scss']
})
export class AnestheticReportEndOfAnesthesiaStatusComponent implements OnInit {

    @Input() service: AnestheticReportEndOfAnesthesiaStatusService;
    form: FormGroup;
    allowOptions = false;
    internmentOptions = INTERNMENT_OPTIONS;

    endOfAnesthesiaRadioGroups: EndOfAnesthesiaRadioGroups;

    constructor() { }

    ngOnInit(): void {
        this.form = this.service.getForm();
        this.service.getGoesInside().subscribe((goesInsideOptions) => {
            this.allowOptions = goesInsideOptions;
        })

        this.endOfAnesthesiaRadioGroups = this.service.getEndOfAnesthesiaRadioGroups();
    }

    handleOptionSelected(optionSelected: RadioGroupData, attributeName: string) {
        this.service.setFormAttributeValue(attributeName, optionSelected.value);
    }

    onInternmentOptionsChange(event: MatRadioChange) {
        this.service.setGoesInsideOptions(event.value)
    }
}
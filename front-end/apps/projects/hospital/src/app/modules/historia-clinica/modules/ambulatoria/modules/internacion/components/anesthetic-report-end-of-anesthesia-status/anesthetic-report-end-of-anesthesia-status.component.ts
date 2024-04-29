import { Component, OnInit } from '@angular/core';
import { AnestheticProcedureAttribute, EndOfAnesthesiaRadioGroups, EndOfAnesthesiaStatusForm, INTERNMENT_OPTIONS } from '../../services/anesthetic-report-end-of-anesthesia-status.service';
import { FormGroup } from '@angular/forms';
import { RadioGroupData } from '@presentation/components/radio-group/radio-group.component';
import { MatRadioChange } from '@angular/material/radio';
import { AnestheticReportService } from '../../services/anesthetic-report.service';

@Component({
    selector: 'app-anesthetic-report-end-of-anesthesia-status',
    templateUrl: './anesthetic-report-end-of-anesthesia-status.component.html',
    styleUrls: ['./anesthetic-report-end-of-anesthesia-status.component.scss']
})
export class AnestheticReportEndOfAnesthesiaStatusComponent implements OnInit {

    form: FormGroup;
    endOfAnesthesiaStatusForm: FormGroup<EndOfAnesthesiaStatusForm>;
    allowOptions = false;
    internmentOptions = INTERNMENT_OPTIONS;

    endOfAnesthesiaRadioGroups: EndOfAnesthesiaRadioGroups;

    constructor(
        private readonly service: AnestheticReportService,
    ) { }

    ngOnInit(): void {
        this.form = this.service.anestheticReportEndOfAnesthesiaStatusService.getForm();
        this.endOfAnesthesiaStatusForm = this.service.anestheticReportEndOfAnesthesiaStatusService.getEndOfAnesthesiaStatusForm();
        this.service.anestheticReportEndOfAnesthesiaStatusService.getinternment().subscribe((goesInsideOptions) => {
            this.allowOptions = goesInsideOptions;
        })

        this.endOfAnesthesiaRadioGroups = this.service.anestheticReportEndOfAnesthesiaStatusService.getEndOfAnesthesiaRadioGroups();
    }

    handleOptionSelected(optionSelected: RadioGroupData, attributeName: AnestheticProcedureAttribute) {
        this.service.anestheticReportEndOfAnesthesiaStatusService.setFormAttributeValue(attributeName, optionSelected.value);
    }

    onInternmentOptionsChange(event: MatRadioChange) {
        this.service.anestheticReportEndOfAnesthesiaStatusService.setinternmentOptions(event.value)
    }
}
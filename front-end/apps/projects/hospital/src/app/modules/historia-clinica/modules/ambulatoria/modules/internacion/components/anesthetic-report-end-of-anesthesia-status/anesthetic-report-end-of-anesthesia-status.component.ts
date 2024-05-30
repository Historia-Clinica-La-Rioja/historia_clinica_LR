import { Component, OnInit } from '@angular/core';
import { AnestheticProcedureAttribute, EndOfAnesthesiaRadioGroups, EndOfAnesthesiaStatusForm, INTERNMENT_OPTIONS } from '../../services/anesthetic-report-end-of-anesthesia-status.service';
import { FormGroup } from '@angular/forms';
import { RadioGroupData } from '@presentation/components/radio-group/radio-group.component';
import { MatRadioChange } from '@angular/material/radio';
import { AnestheticReportService } from '../../services/anesthetic-report.service';
import { PostAnesthesiaStatusDto } from '@api-rest/api-model';

const RADIO_BUTTON_YES = 1;
const RADIO_BUTTON_NO = 0;

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
	selectedOption: INTERNMENT_OPTIONS;

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

		this.endOfAnesthesiaRadioGroups = JSON.parse(JSON.stringify(this.service.anestheticReportEndOfAnesthesiaStatusService.getEndOfAnesthesiaRadioGroups()));
		this.service.postAnesthesia$.subscribe(data => {
			if (data) {
				this.mapDataToRadioGroups(data)
			}
		})
    }

    handleOptionSelected(optionSelected: RadioGroupData, attributeName: AnestheticProcedureAttribute) {
        this.service.anestheticReportEndOfAnesthesiaStatusService.setFormAttributeValue(attributeName, optionSelected.value);
    }

    onInternmentOptionsChange(event: MatRadioChange) {
        this.service.anestheticReportEndOfAnesthesiaStatusService.setinternmentOptions(event.value)
    }

	private mapDataToRadioGroups(data: PostAnesthesiaStatusDto) {
		this.endOfAnesthesiaRadioGroups.intentionalSensitivity.presentation.previousValueId = data.intentionalSensitivity != null ? (data.intentionalSensitivity ? RADIO_BUTTON_YES : RADIO_BUTTON_NO) : null;
		this.endOfAnesthesiaRadioGroups.cornealReflex.presentation.previousValueId = data.cornealReflex != null ? (data.cornealReflex ? RADIO_BUTTON_YES : RADIO_BUTTON_NO) : null;
		this.endOfAnesthesiaRadioGroups.obeyOrders.presentation.previousValueId = data.obeyOrders != null ? (data.obeyOrders ? RADIO_BUTTON_YES : RADIO_BUTTON_NO) : null;
		this.endOfAnesthesiaRadioGroups.talk.presentation.previousValueId = data.talk != null ? (data.talk ? RADIO_BUTTON_YES : RADIO_BUTTON_NO) : null;
		this.endOfAnesthesiaRadioGroups.respiratoryDepression.presentation.previousValueId = data.respiratoryDepression != null ? (data.respiratoryDepression ? RADIO_BUTTON_YES : RADIO_BUTTON_NO) : null;
		this.endOfAnesthesiaRadioGroups.circulatoryDepression.presentation.previousValueId = data.circulatoryDepression != null ? (data.circulatoryDepression ? RADIO_BUTTON_YES : RADIO_BUTTON_NO) : null;
		this.endOfAnesthesiaRadioGroups.vomiting.presentation.previousValueId = data.vomiting != null ? (data.vomiting ? RADIO_BUTTON_YES : RADIO_BUTTON_NO) : null;
		this.endOfAnesthesiaRadioGroups.curated.presentation.previousValueId = data.curated != null ? (data.curated ? RADIO_BUTTON_YES : RADIO_BUTTON_NO) : null;
		this.endOfAnesthesiaRadioGroups.trachealCannula.presentation.previousValueId = data.trachealCannula != null ? (data.trachealCannula ? RADIO_BUTTON_YES : RADIO_BUTTON_NO) : null;
		this.endOfAnesthesiaRadioGroups.pharyngealCannula.presentation.previousValueId = data.pharyngealCannula != null ? (data.pharyngealCannula ? RADIO_BUTTON_YES : RADIO_BUTTON_NO) : null;
		this.endOfAnesthesiaRadioGroups.internment.presentation.previousValueId = data.internment != null ? (data.internment ? RADIO_BUTTON_YES : RADIO_BUTTON_NO) : null;
		this.endOfAnesthesiaStatusForm.get('observations').setValue(data.note ? data.note : null);
		this.selectedOption = this.service.anestheticReportEndOfAnesthesiaStatusService.mapToInternmentOptions(data.internment ? data.internmentPlace : null)
		this.endOfAnesthesiaRadioGroups = JSON.parse(JSON.stringify(this.endOfAnesthesiaRadioGroups))
	}
}

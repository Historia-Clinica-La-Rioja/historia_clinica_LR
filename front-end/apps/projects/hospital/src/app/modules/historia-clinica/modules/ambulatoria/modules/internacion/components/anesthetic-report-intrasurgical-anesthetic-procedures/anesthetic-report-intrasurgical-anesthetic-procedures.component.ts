import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS, IntresurgicalAnestheticProceduresRadioGroups } from '../../services/anesthetic-report-intrasurgical-anesthetic-procedures.service';
import { MatRadioChange } from '@angular/material/radio';
import { AnestheticReportService } from '../../services/anesthetic-report.service';
import { ProcedureDescriptionDto } from '@api-rest/api-model';

const RADIO_BUTTON_YES = 1;
const RADIO_BUTTON_NO = 0;
@Component({
	selector: 'app-anesthetic-report-intrasurgical-anesthetic-procedures',
	templateUrl: './anesthetic-report-intrasurgical-anesthetic-procedures.component.html',
	styleUrls: ['./anesthetic-report-intrasurgical-anesthetic-procedures.component.scss']
})

export class AnestheticReportIntrasurgicalAnestheticProceduresComponent implements OnInit {

	form: FormGroup;
	intrasurgicalAnestheticProceduresOptions = INTRASURGICAL_ANESTHETIC_PROCEDURES_OPTIONS;

	intrasurgicalAnestheticProceduresRadioGroups: IntresurgicalAnestheticProceduresRadioGroups;

	constructor(
		private readonly service: AnestheticReportService,
	) { }

	ngOnInit(): void {
		this.form = this.service.anestheticReportIntrasurgicalAnestheticProceduresService.getForm();

		this.service.intrasurgicalAnestheticProcedures$.subscribe(data => {
			this.intrasurgicalAnestheticProceduresRadioGroups = this.service.anestheticReportIntrasurgicalAnestheticProceduresService.getIntrasurgicalAnestheticProceduresRadioGroups();
			if (data) {
				this.mapDataToRadioGroups(data);
			}
		})
	}

	onVenousAccessChange($event: MatRadioChange) {
		this.service.anestheticReportIntrasurgicalAnestheticProceduresService.setVenousAccess($event.value)
	}

	onNasogastricTubeChange($event: MatRadioChange) {
		this.service.anestheticReportIntrasurgicalAnestheticProceduresService.setNasogastricTube($event.value)
	}

	onUrinaryCatheterChange($event: MatRadioChange) {
		this.service.anestheticReportIntrasurgicalAnestheticProceduresService.setUrinaryCatheter($event.value)
	}

	private mapDataToRadioGroups(data: ProcedureDescriptionDto): void {
		const fields = ['venousAccess', 'nasogastricTube', 'urinaryCatheter'];
		fields.forEach(field => {
			this.intrasurgicalAnestheticProceduresRadioGroups[field + 'RadioGroupInputData'].presentation.previousValueId =
				data?.[field] !== undefined ? (data[field] ? RADIO_BUTTON_YES : RADIO_BUTTON_NO) : null;
		});
		this.intrasurgicalAnestheticProceduresRadioGroups = JSON.parse(JSON.stringify(this.intrasurgicalAnestheticProceduresRadioGroups));
	}
}

import { Component, Input, OnChanges } from '@angular/core';
import { OTHER_INDICATION, showFrequency, showTimeElapsed } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { Content } from '@presentation/components/indication/indication.component';
import { OtherIndicationDto } from '@api-rest/api-model';

@Component({
	selector: 'app-internment-other-indication-card',
	templateUrl: './internment-other-indication-card.component.html',
	styleUrls: ['./internment-other-indication-card.component.scss']
})
export class InternmentOtherIndicationCardComponent implements OnChanges {

	OTHER_INDICATION = OTHER_INDICATION;

	indicationContent: Content[] = [];

	@Input() otherIndications: OtherIndicationDto[];


	constructor() { }

	ngOnChanges() {
		this.indicationContent = this.mapToIndicationContent();
	}


	mapToIndicationContent(): Content[] {
		return this.otherIndications?.map((otherIndication: OtherIndicationDto) => {
			return {
				status: {
					description: otherIndication.status === "INDICATED" ? 'indicacion.internment-card.sections.label.INDICATED' : 'indicacion.internment-card.sections.label.SUSPENDED',
					cssClass: otherIndication.status === "INDICATED" ? 'blue' : 'red'
				},
				description: otherIndication.description,
				createdBy: otherIndication.createdBy,
				timeElapsed: showTimeElapsed(otherIndication.createdOn),
				extra_info: otherIndication?.dosage ? showFrequency(otherIndication.dosage) : [],
			}
		});
	}
}

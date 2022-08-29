import { Component, Input, OnChanges } from '@angular/core';
import { OTHER_INDICATION, OTHER_INDICATION_ID, showTimeElapsed } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { Content } from '@presentation/components/indication/indication.component';
import { OtherIndicationDto } from '@api-rest/api-model';
import { OtherIndicationTypeDto } from '@api-rest/services/internment-indication.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { showFrequency } from '../../constants/load-information';

@Component({
	selector: 'app-internment-other-indication-card',
	templateUrl: './internment-other-indication-card.component.html',
	styleUrls: ['./internment-other-indication-card.component.scss']
})
export class InternmentOtherIndicationCardComponent implements OnChanges {

	OTHER_INDICATION = OTHER_INDICATION;

	OTHER_INDICATION_ID = OTHER_INDICATION_ID;

	indicationContent: Content[] = [];

	othersIndicatiosType: OtherIndicationTypeDto[] = [];

	@Input() otherIndications: OtherIndicationDto[];


	constructor(
		private readonly internacionMasterdataService: InternacionMasterDataService,

	) { }

	ngOnChanges() {
		this.internacionMasterdataService.getOtherIndicationTypes().subscribe(i => {
			this.othersIndicatiosType = i; this.indicationContent = this.mapToIndicationContent();
		});
	}


	mapToIndicationContent(): Content[] {
		return this.otherIndications?.map((otherIndication: OtherIndicationDto) => {
			return {
				status: {
					description: otherIndication.status === "INDICATED" ? 'indicacion.internment-card.sections.label.INDICATED' : 'indicacion.internment-card.sections.label.SUSPENDED',
					cssClass: otherIndication.status === "INDICATED" ? 'blue' : 'red'
				},
				description: indication(otherIndication, this.othersIndicatiosType),
				createdBy: otherIndication.createdBy,
				timeElapsed: showTimeElapsed(otherIndication.createdOn),
				extra_info: otherIndication?.dosage ? showFrequency(otherIndication.dosage) : [],
			}

		});

		function indication(otherIndication: OtherIndicationDto, othersIndicatiosType: OtherIndicationTypeDto[]): string {
			const result = othersIndicatiosType.find(i => i.id === otherIndication.otherIndicationTypeId);
			return (result.id === OTHER_INDICATION_ID) ? otherIndication.otherType : result.description;
		}

	}
}

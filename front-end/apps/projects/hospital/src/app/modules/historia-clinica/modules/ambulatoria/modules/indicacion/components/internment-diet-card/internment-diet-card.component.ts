import { Component, Input, OnChanges } from "@angular/core";
import { DIET, IndicationStatus, IndicationStatusScss, showTimeElapsed } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
import { DietDto } from "@api-rest/api-model";
import { Content } from '@presentation/components/indication/indication.component';

@Component({
	selector: 'app-internment-diet-card',
	templateUrl: './internment-diet-card.component.html',
	styleUrls: ['./internment-diet-card.component.scss']
})
export class InternmentDietCardComponent implements OnChanges {

	DIET = DIET;
	indicationContent: Content[] = [];
	@Input() diets: DietDto[]

	constructor() { }

	ngOnChanges() {
		this.indicationContent = this.mapToIndicationContent();
	}


	mapToIndicationContent(): Content[] {
		return this.diets?.map((diet: DietDto) => {
			return {
				status: {
					description: IndicationStatus[diet.status],
					cssClass: IndicationStatusScss[diet.status],
					type: diet.type
				},
				description: diet.description,
				createdBy: diet.createdBy,
				timeElapsed: showTimeElapsed(diet.createdOn),
			}
		});
	}
}

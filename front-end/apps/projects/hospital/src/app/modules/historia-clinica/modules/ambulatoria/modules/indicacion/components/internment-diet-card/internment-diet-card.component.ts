import { Component, Input, OnChanges } from "@angular/core";
import { DIET, showTimeElapsed } from "@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications";
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
					description: diet.status === "INDICATED" ? 'indicacion.internment-card.sections.label.INDICATED' : 'indicacion.internment-card.sections.label.SUSPENDED',
					cssClass: diet.status === "INDICATED" ? 'blue' : 'red'
				},
				description: diet.description,
				createdBy: diet.createdBy,
				timeElapsed: showTimeElapsed(diet.createdOn),
			}
		});
	}
}

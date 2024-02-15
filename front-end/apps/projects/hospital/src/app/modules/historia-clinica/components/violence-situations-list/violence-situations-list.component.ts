import { Component } from '@angular/core';
import { SnomedDto } from '@api-rest/api-model';
import { ViolenceSituationsNewConsultationService } from '@historia-clinica/modules/ambulatoria/services/violence-situations-new-consultation.service';

@Component({
	selector: 'app-violence-situations-list',
	templateUrl: './violence-situations-list.component.html',
	styleUrls: ['./violence-situations-list.component.scss']
})
export class ViolenceSituationsListComponent {

	violenceSituations: SnomedDto[] = [];

	constructor(private readonly violenceSituationService: ViolenceSituationsNewConsultationService) {
		this.setViolenceSituations();
	}

	removeViolenceSituation(index: number) {
		this.violenceSituationService.removeViolenceSituation(index);
	}

	private setViolenceSituations() {
		this.violenceSituationService.violenceSituations$
			.subscribe((concepts: SnomedDto[]) => this.violenceSituations = concepts);
	}
}

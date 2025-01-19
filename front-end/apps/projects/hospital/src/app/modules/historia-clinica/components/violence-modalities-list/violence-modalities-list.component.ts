import { Component } from '@angular/core';
import { SnomedDto } from '@api-rest/api-model';
import { ViolenceModalityNewConsultationService } from '@historia-clinica/modules/ambulatoria/services/violence-modality-new-consultation.service';

@Component({
	selector: 'app-violence-modalities-list',
	templateUrl: './violence-modalities-list.component.html',
	styleUrls: ['./violence-modalities-list.component.scss']
})
export class ViolenceModalitiesListComponent {

	violenceModalities: SnomedDto[] = [];

	constructor(private readonly violenceModalityService: ViolenceModalityNewConsultationService) {
		this.setViolenceModalities();
	}

	removeViolenceModality(index: number) {
		this.violenceModalityService.removeViolenceModality(index);
	}

	private setViolenceModalities() {
		this.violenceModalityService.violenceModalities$
			.subscribe((concepts: SnomedDto[]) => this.violenceModalities = concepts);
	}

}

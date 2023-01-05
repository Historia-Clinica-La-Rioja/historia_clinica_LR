import { Component } from '@angular/core';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { Redirect } from "@historia-clinica/modules/ambulatoria/modules/internacion/components/internment-patient-table/internment-patient-table.component";

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent {

	redirect = Redirect.patientCard;
	ffOfCardsIsOn: boolean;
	constructor(
		private readonly featureFlagService: FeatureFlagService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_VISUALIZACION_DE_CARDS).subscribe(isEnabled => this.ffOfCardsIsOn = isEnabled);
	}

}


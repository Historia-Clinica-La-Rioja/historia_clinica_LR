import { Component } from '@angular/core';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { Redirect } from "@historia-clinica/modules/ambulatoria/modules/internacion/components/internment-patient-table/internment-patient-table.component";

@Component({
	selector: 'app-internaciones-home',
	templateUrl: './internaciones-home.component.html',
	styleUrls: ['./internaciones-home.component.scss']
})
export class InternacionesHomeComponent {
	
	redirect = Redirect.HC;
	ffOfCardsIsOn: boolean;

	constructor(private readonly featureFlagService: FeatureFlagService) { 
		this.featureFlagService.isActive(AppFeature.HABILITAR_VISUALIZACION_DE_CARDS).subscribe(isEnabled => this.ffOfCardsIsOn = isEnabled);
	}

}
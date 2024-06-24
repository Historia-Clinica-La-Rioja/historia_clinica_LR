import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { Redirect } from "@historia-clinica/modules/ambulatoria/modules/internacion/components/internment-patient-table/internment-patient-table.component";
import { ParamsToSearchPerson } from '@pacientes/component/search-create/search-create.component';
import { PatientSearchNagivationService } from '@pacientes/services/patient-search-nagivation.service';
import { toParamsToSearchPerson } from '@pacientes/utils/search.utils';
import { map, take } from 'rxjs';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	redirect = Redirect.patientCard;
	ffOfCardsIsOn: boolean;
	paramsToSearchPerson: ParamsToSearchPerson;

	constructor(
		private readonly featureFlagService: FeatureFlagService,
		private activatedRoute: ActivatedRoute,
		readonly patientSearchNavigateService: PatientSearchNagivationService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_VISUALIZACION_DE_CARDS).subscribe(isEnabled => this.ffOfCardsIsOn = isEnabled);
	}

	ngOnInit(): void {
		this.patientSearchNavigateService.setSpinner(true);
		this.activatedRoute.queryParams.pipe(take(1), map(params => toParamsToSearchPerson(params))).subscribe(
			paramsToSearchPerson => {
				this.paramsToSearchPerson = paramsToSearchPerson;
				this.patientSearchNavigateService.setSpinner(false);
			}
		);
	}

}


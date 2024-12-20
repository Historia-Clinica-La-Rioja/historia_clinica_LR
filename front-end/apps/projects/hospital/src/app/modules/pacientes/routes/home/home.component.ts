import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { Redirect } from '@historia-clinica/modules/ambulatoria/modules/internacion/components/internment-patient-card/internment-patient-card.component';
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
	paramsToSearchPerson: ParamsToSearchPerson;
	isHabilitarInternacionOn = false

	constructor(
		private activatedRoute: ActivatedRoute,
		private readonly featureFlagService: FeatureFlagService,
		readonly patientSearchNavigateService: PatientSearchNagivationService,
	) {	}

	ngOnInit(): void {
		this.featureFlagService.isActive(AppFeature.HABILITAR_MODULO_INTERNACION).subscribe(isEnable => this.isHabilitarInternacionOn = isEnable);
		this.patientSearchNavigateService.setSpinner(true);
		this.activatedRoute.queryParams.pipe(take(1), map(params => toParamsToSearchPerson(params))).subscribe(
			paramsToSearchPerson => {
				this.paramsToSearchPerson = paramsToSearchPerson;
				this.patientSearchNavigateService.setSpinner(false);
			}
		);
	}

}


import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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

	constructor(
		private activatedRoute: ActivatedRoute,
		readonly patientSearchNavigateService: PatientSearchNagivationService,
	) {	}

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


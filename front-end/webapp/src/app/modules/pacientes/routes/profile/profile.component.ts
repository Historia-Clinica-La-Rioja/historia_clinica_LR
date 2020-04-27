import { Component, OnInit } from '@angular/core';
import { Observable } from "rxjs";
import { PatientBasicData } from "../../../presentation/components/patient-card/patient-card.component";
import { BasicPatientDto } from "@api-rest/api-model";
import { map } from "rxjs/operators";
import { PatientService } from "@api-rest/services/patient.service";
import { MapperService } from "../../../presentation/services/mapper.service";
import { ActivatedRoute } from "@angular/router";

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

	public patient$: Observable<PatientBasicData>;

	constructor(
		private patientService: PatientService,
		private mapperService: MapperService,
		private route: ActivatedRoute) {
	}

	ngOnInit(): void {

		this.route.paramMap.subscribe(
			(params) => {
				let id = Number(params.get('id'));
				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(id).pipe(
					map(patient => this.mapperService.toPatientBasicData(patient))
				);

			}
		);
	}

}

import { Component, OnInit } from '@angular/core';
import { Observable } from "rxjs";
import { BasicPatientDto, PersonalInformationDto } from "@api-rest/api-model";
import { map } from "rxjs/operators";
import { PatientService } from "@api-rest/services/patient.service";
import { MapperService } from "../../../presentation/services/mapper.service";
import { ActivatedRoute } from "@angular/router";
import { PersonService } from "@api-rest/services/person.service";
import { PatientBasicData } from 'src/app/modules/presentation/components/patient-card/patient-card.component';

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

	public patient$: Observable<PatientBasicData>;
	public person: PersonalInformationDto;

	constructor(
		private patientService: PatientService,
		private mapperService: MapperService,
		private route: ActivatedRoute,
		private personService: PersonService) {
	}

	ngOnInit(): void {

		this.route.paramMap.subscribe(
			(params) => {
				let id = Number(params.get('id'));
				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(id).pipe(
					map(patient => {
						this.personService.getPersonalInformation<PersonalInformationDto>(patient.person.id).subscribe(
							data => this.person = data);
						return this.mapperService.toPatientBasicData(patient);
					})
				);
			}
		);
	}

}

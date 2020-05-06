import { Component, OnInit } from '@angular/core';
import { BasicPatientDto, PersonalInformationDto, CompletePatientDto } from "@api-rest/api-model";
import { PatientService } from "@api-rest/services/patient.service";
import { MapperService } from "../../../presentation/services/mapper.service";
import { ActivatedRoute } from "@angular/router";
import { PersonService } from "@api-rest/services/person.service";
import { PatientBasicData } from 'src/app/modules/presentation/components/patient-card/patient-card.component';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public patientTypeData: PatientTypeData;
	public person: PersonalInformationDto;
	public codigoColor: string;
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
				this.patientService.getPatientCompleteData<CompletePatientDto>(id)
					.subscribe(completeData => {
							this.patientTypeData = this.mapperService.toPatientTypeData(completeData.patientType);
							this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
							this.personService.getPersonalInformation<PersonalInformationDto>(completeData.person.id)
								.subscribe( personInformationData => {
									this.personalInformation = this.mapperService.toPersonalInformationData(completeData,personInformationData);
								});
					});
			});

	}
}


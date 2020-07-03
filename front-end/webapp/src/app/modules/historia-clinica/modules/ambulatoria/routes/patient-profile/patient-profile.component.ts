import { Component, OnInit } from '@angular/core';
import { PersonalInformationDto, CompletePatientDto } from "@api-rest/api-model";
import { PatientService } from "@api-rest/services/patient.service";
import { MapperService } from "@presentation/services/mapper.service";
import { ActivatedRoute, Router } from "@angular/router";
import { PersonService } from "@api-rest/services/person.service";
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';
import { ContextService } from "@core/services/context.service";
import { InternmentPatientService } from '@api-rest/services/internment-patient.service';


@Component({
  selector: 'app-patient-profile',
  templateUrl: './patient-profile.component.html',
  styleUrls: ['./patient-profile.component.scss']
})
export class PatientProfileComponent implements OnInit {

	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public patientTypeData: PatientTypeData;
	public person: PersonalInformationDto;
	public codigoColor: string;
	private patientId: number;
	private readonly routePrefix;
	public internmentEpisode;

	constructor(private patientService: PatientService,
		private mapperService: MapperService,
		private route: ActivatedRoute,
		private router: Router,
		private personService: PersonService,
		private contextService: ContextService,
		private internmentPatientService: InternmentPatientService) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {

		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('id'));
				this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
					.subscribe(completeData => {
						this.patientTypeData = this.mapperService.toPatientTypeData(completeData.patientType);
						this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
						this.personService.getPersonalInformation<PersonalInformationDto>(completeData.person.id)
							.subscribe(personInformationData => {
								this.personalInformation = this.mapperService.toPersonalInformationData(completeData, personInformationData);
							});
					});

				this.internmentPatientService.internmentEpisodeIdInProcess(this.patientId)
					.subscribe(internmentEpisodeProcessDto => {
						if (internmentEpisodeProcessDto) {
							this.internmentEpisode = internmentEpisodeProcessDto;
						}
					});
			});

	}
}

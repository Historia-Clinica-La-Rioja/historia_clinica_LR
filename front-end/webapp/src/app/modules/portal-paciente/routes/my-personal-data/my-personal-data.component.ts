import { Component, OnInit } from '@angular/core';
import {PersonalInformation} from '@presentation/components/personal-information/personal-information.component';
import {CompletePatientDto, PatientMedicalCoverageDto, PersonalInformationDto, PersonPhotoDto} from '@api-rest/api-model';
import {PatientBasicData} from '@presentation/components/patient-card/patient-card.component';
import {PatientService} from '@api-rest/services/patient.service';
import {PersonService} from '@api-rest/services/person.service';
import {MapperService} from '@presentation/services/mapper.service';
import {PatientMedicalCoverageService} from '@api-rest/services/patient-medical-coverage.service';

@Component({
  selector: 'app-my-personal-data',
  templateUrl: './my-personal-data.component.html',
  styleUrls: ['./my-personal-data.component.scss']
})
export class MyPersonalDataComponent implements OnInit {

	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public patientMedicalCoverage: PatientMedicalCoverageDto[];
	public personPhoto: PersonPhotoDto;

  	constructor(
		private readonly patientService: PatientService,
		private readonly personService: PersonService,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
	) { }

  	ngOnInit(): void {

		const patientId = 1;
		this.patientService.getPatientCompleteData<CompletePatientDto>(patientId)
			.subscribe(completeData => {
				this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
				this.personService.getPersonalInformation<PersonalInformationDto>(completeData.person.id)
					.subscribe(personInformationData => {
						this.personalInformation =
							this.mapperService.toPersonalInformationData(completeData, personInformationData);
					});
			});

		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(patientId)
			.subscribe(patientMedicalCoverageDto => this.patientMedicalCoverage = patientMedicalCoverageDto);

		this.patientService.getPatientPhoto(patientId)
			.subscribe((personPhotoDto: PersonPhotoDto) => {this.personPhoto = personPhotoDto; });
  	}

}

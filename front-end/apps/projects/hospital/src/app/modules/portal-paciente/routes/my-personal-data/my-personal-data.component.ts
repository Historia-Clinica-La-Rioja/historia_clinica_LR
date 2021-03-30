import { Component, OnInit } from '@angular/core';
import {PersonalInformation} from '@presentation/components/personal-information/personal-information.component';
import {PatientMedicalCoverageDto, PersonPhotoDto} from '@api-rest/api-model';
import {PatientBasicData} from '@presentation/components/patient-card/patient-card.component';
import {MapperService} from '@presentation/services/mapper.service';
import {PatientPortalService} from '@api-rest/services/patient-portal.service';

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
		private readonly patientPortalService: PatientPortalService,
		private readonly mapperService: MapperService,
	) { }

  	ngOnInit(): void {
		this.patientPortalService.getCompleteDataPatient().subscribe(completeData => {
				this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
				this.patientPortalService.getPersonalInformation()
					.subscribe(personInformationData => {
						this.personalInformation =
							this.mapperService.toPersonalInformationData(completeData, personInformationData);
					});
			});

		this.patientPortalService.getActivePatientMedicalCoverages()
			.subscribe(patientMedicalCoverageDto => this.patientMedicalCoverage = patientMedicalCoverageDto);

		this.patientPortalService.getPatientPhoto()
			.subscribe((personPhotoDto: PersonPhotoDto) => {this.personPhoto = personPhotoDto; });
  	}

}

import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CompletePatientDto, EmergencyCareEpisodeInProgressDto, InternmentEpisodeProcessDto, PatientMedicalCoverageDto, PersonalInformationDto, PersonPhotoDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeSummaryService } from '@api-rest/services/emergency-care-episode-summary.service';
import { InternmentPatientService } from '@api-rest/services/internment-patient.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientService } from '@api-rest/services/patient.service';
import { PersonService } from '@api-rest/services/person.service';
import { MapperService } from '@presentation/services/mapper.service';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';

@Component({
	selector: 'app-patient-profile-popup',
	templateUrl: './patient-profile-popup.component.html',
	styleUrls: ['./patient-profile-popup.component.scss']
})
export class PatientProfilePopupComponent implements OnInit {

	patientId: number;
	showButtonGoToMedicalHistory = false;
	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public patientTypeData: PatientTypeData;
	public person: PersonalInformationDto;
	public personPhoto: PersonPhotoDto;
	public internmentEpisode: InternmentEpisodeProcessDto;
	public patientMedicalCoverage: PatientMedicalCoverageDto[];
	public emergencyCareEpisodeInProgress: EmergencyCareEpisodeInProgressDto;

	constructor(private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly personService: PersonService,
		private readonly internmentPatientService: InternmentPatientService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly emergencyCareEpisodeSummaryService: EmergencyCareEpisodeSummaryService, @Inject(MAT_DIALOG_DATA) public data: {
			patientId: number,
		}) { this.patientId = this.data.patientId; }

	ngOnInit(): void {

		this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
			.subscribe(completeData => {
				this.patientTypeData = this.mapperService.toPatientTypeData(completeData.patientType);
				this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
				this.personService.getPersonalInformation<PersonalInformationDto>(completeData.person.id)
					.subscribe(personInformationData => {
						this.personalInformation = this.mapperService.toPersonalInformationData(completeData, personInformationData);
					});
			});

		this.patientService.getPatientPhoto(this.patientId)
			.subscribe((personPhotoDto: PersonPhotoDto) => {
				this.personPhoto = personPhotoDto;
			});


		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.patientId)
			.subscribe(patientMedicalCoverageDto => this.patientMedicalCoverage = patientMedicalCoverageDto);

		this.internmentPatientService.internmentEpisodeIdInProcess(this.patientId)
			.subscribe(internmentEpisodeProcessDto => {
				this.internmentEpisode = internmentEpisodeProcessDto;
			});

		this.emergencyCareEpisodeSummaryService.getEmergencyCareEpisodeInProgress(this.patientId)
			.subscribe(emergencyCareEpisodeInProgressDto => {
				this.emergencyCareEpisodeInProgress = emergencyCareEpisodeInProgressDto;
			});

	}



}

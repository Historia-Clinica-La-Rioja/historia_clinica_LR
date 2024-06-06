import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CompletePatientDto, EmergencyCareEpisodeInProgressDto, InternmentEpisodeProcessDto, PatientMedicalCoverageDto, PersonalInformationDto, PersonPhotoDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeSummaryService } from '@api-rest/services/emergency-care-episode-summary.service';
import { InternmentPatientService } from '@api-rest/services/internment-patient.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientService } from '@api-rest/services/patient.service';
import { PersonService } from '@api-rest/services/person.service';
import { MapperService } from '@presentation/services/mapper.service';
import { PatientBasicData } from '@presentation/utils/patient.utils';
import { PatientTypeData } from '@presentation/components/patient-type-logo/patient-type-logo.component';
import { PersonalInformation } from '@presentation/components/personal-information/personal-information.component';
import { AuditablePatientInfo } from '@pacientes/routes/edit-patient/edit-patient.component';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';

const ROUTE_EDIT_PATIENT = 'pacientes/edit';
@Component({
	selector: 'app-patient-profile-popup',
	templateUrl: './patient-profile-popup.component.html',
	styleUrls: ['./patient-profile-popup.component.scss']
})
export class PatientProfilePopupComponent implements OnInit {

	patientId: number;
	showButtonGoToMedicalHistory = false;
	private readonly routePrefix;
	public patientBasicData: PatientBasicData;
	public personalInformation: PersonalInformation;
	public patientTypeData: PatientTypeData;
	public person: PersonalInformationDto;
	public personPhoto: PersonPhotoDto;
	public internmentEpisode: InternmentEpisodeProcessDto;
	public patientMedicalCoverage: PatientMedicalCoverageDto[];
	public emergencyCareEpisodeInProgress: EmergencyCareEpisodeInProgressDto;
	public auditablePatientInfo: AuditablePatientInfo;
	private auditableFullDate: Date;

	constructor(private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly personService: PersonService,
		private readonly internmentPatientService: InternmentPatientService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly emergencyCareEpisodeSummaryService: EmergencyCareEpisodeSummaryService,
		private readonly datePipe: DatePipe,
		private router: Router,
		private contextService: ContextService,
		@Inject(MAT_DIALOG_DATA) public data: {
			patientId: number,
			viewCardToAudit: boolean
		}) {
			this.patientId = this.data.patientId;
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {
		this.patientService.getPatientCompleteData<CompletePatientDto>(this.patientId)
			.subscribe(completeData => {
				if (completeData?.auditablePatientInfo) {
					this.auditableFullDate = dateTimeDtotoLocalDate(
						{
							date: completeData.auditablePatientInfo.createdOn.date,
							time: completeData.auditablePatientInfo.createdOn.time
						}
					);
					this.auditablePatientInfo = {
						message: completeData.auditablePatientInfo.message,
						createdBy: completeData.auditablePatientInfo.createdBy,
						createdOn: this.datePipe.transform(this.auditableFullDate, DatePipeFormat.SHORT),
						institutionName: completeData.auditablePatientInfo.institutionName
					};
				}
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

	goToEditPatient() {
		const route: string = this.data.viewCardToAudit ? '/home/auditoria/pacientes/edit' : this.routePrefix + ROUTE_EDIT_PATIENT;
		this.router.navigate([route], {
			queryParams: { id: this.patientId }
		});
	}
}

import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { CompletePatientDto, PersonalInformationDto, PersonPhotoDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeService, ResponseEmergencyCareDto } from '@api-rest/services/emergency-care-episode.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientService } from '@api-rest/services/patient.service';
import { PersonService } from '@api-rest/services/person.service';
import { ConfirmDialogComponent } from '@core/dialogs/confirm-dialog/confirm-dialog.component';
import { MapperService } from '@presentation/services/mapper.service';
import { Observable } from 'rxjs';
import { SearchPatientComponent } from 'src/app/modules/pacientes/component/search-patient/search-patient.component';

@Component({
	selector: 'app-episode-details',
	templateUrl: './episode-details.component.html',
	styleUrls: ['./episode-details.component.scss']
})
export class EpisodeDetailsComponent implements OnInit {

	personPhoto$: Observable<PersonPhotoDto>;
	patientBasicData;
	personalInformation;
	patientMedicalCoverage;
	episodeId: number;
	responseEmergencyCare: ResponseEmergencyCareDto;

	constructor(
		private readonly route: ActivatedRoute,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly personService: PersonService,
		//No se puede hacer un llamado y que traiga todo de una? En un monton de lados debemos estar haciendo esto...
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly dialog: MatDialog,

	) { }

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.episodeId = Number(params.get('id'));

				this.emergencyCareEpisodeService.getAdministrative()
					.subscribe((responseEmergencyCare: ResponseEmergencyCareDto) => {
						this.responseEmergencyCare = responseEmergencyCare;

						if (responseEmergencyCare.patientId) {
							this.loadPatient(responseEmergencyCare.patientId);
						}
					});
			});

	}

	searchPatient(): void {
		const dialogRef = this.dialog.open(SearchPatientComponent);
		dialogRef.afterClosed().subscribe(
			patient => {
				if (patient) {
					const confirmRef = this.dialog.open(ConfirmDialogComponent, {
						data: {
							title: 'guardia.episode.search_patient.CONFIRM',
							content: `¿Está seguro que desea asociar el paciente con ID ${patient.basicData.id} al episodio de guardia?`,
							okButtonLabel: 'buttons.CONFIRM'
						}
					});

					confirmRef.afterClosed().subscribe(confirmed => {
						if (confirmed) {
							//persistir el nuevo paciente
							this.loadPatient(patient.basicData.id);
							this.responseEmergencyCare.patientId = patient.basicData.id;
						}
					})
				}
			}
		);
	}

	//TODO Crear un servicio que englobe estos llamados (tg-2735)
	private loadPatient(patientId: number): void {
		this.patientService.getPatientCompleteData<CompletePatientDto>(patientId)
			.subscribe(completeData => {
				this.patientBasicData = this.mapperService.toPatientBasicData(completeData);
				this.personService.getPersonalInformation<PersonalInformationDto>(completeData.person.id)
					.subscribe(personInformationData => {
						this.personalInformation = this.mapperService.toPersonalInformationData(completeData, personInformationData);
					});
				this.patientMedicalCoverageService.getActivePatientMedicalCoverages(patientId)
					.subscribe(patientMedicalCoverageDto => this.patientMedicalCoverage = patientMedicalCoverageDto);
			});

		this.personPhoto$ = this.patientService.getPatientPhoto(patientId);
	}

}

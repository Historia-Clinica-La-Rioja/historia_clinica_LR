import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { CompletePatientDto, PersonalInformationDto, PersonPhotoDto, TriageListDto, ResponseEmergencyCareDto, MasterDataDto } from '@api-rest/api-model';
import {
	EmergencyCareEpisodeService,
} from '@api-rest/services/emergency-care-episode.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientService } from '@api-rest/services/patient.service';
import { PersonService } from '@api-rest/services/person.service';
import { ConfirmDialogComponent } from '@core/dialogs/confirm-dialog/confirm-dialog.component';
import { MapperService } from '@presentation/services/mapper.service';
import { Observable } from 'rxjs';
import { SearchPatientComponent } from 'src/app/modules/pacientes/component/search-patient/search-patient.component';
import { TriageService } from '@api-rest/services/triage.service';
import { Triage } from '../../components/triage-details/triage-details.component';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import { EmergencyCareTypes, Triages } from '../../constants/masterdata';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { TriageCategory } from '../../components/triage-chip/triage-chip.component';
import { TriageDefinitionsService } from '../../services/triage-definitions.service';

@Component({
	selector: 'app-episode-details',
	templateUrl: './episode-details.component.html',
	styleUrls: ['./episode-details.component.scss']
})
export class EpisodeDetailsComponent implements OnInit {

	readonly triages = Triages;

	personPhoto$: Observable<PersonPhotoDto>;
	patientBasicData;
	personalInformation;
	patientMedicalCoverage;
	episodeId: number;
	responseEmergencyCare: ResponseEmergencyCareDto;

	emergencyCareType: EmergencyCareTypes;
	lastTriage: Triage;
	triagesHistory: TriageReduced[];

	constructor(
		private readonly route: ActivatedRoute,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly personService: PersonService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly dialog: MatDialog,
		private readonly triageService: TriageService,
		private readonly guardiaMapperService: GuardiaMapperService,
		private snackBarService: SnackBarService,
		private readonly triageDefinitionsService: TriageDefinitionsService
	) { }

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.episodeId = Number(params.get('id'));

				this.emergencyCareEpisodeService.getAdministrative(this.episodeId)
					.subscribe((responseEmergencyCare: ResponseEmergencyCareDto) => {
						this.responseEmergencyCare = responseEmergencyCare;
						this.emergencyCareType = responseEmergencyCare.emergencyCareType?.id;

						if (responseEmergencyCare.patient?.id) {
							this.loadPatient(responseEmergencyCare.patient.id);
						}
					});

				this.loadTriages();
			});

	}

	private loadTriages(): void {
		this.triageService.getAll(this.episodeId).subscribe((triages: TriageListDto[]) => {
			this.lastTriage = this.guardiaMapperService.triageListDtoToTriage(triages[0]);
			if (hasHistory(triages)) {
				this.triagesHistory = triages.map(this.guardiaMapperService.triageListDtoToTriageReduced);
				this.triagesHistory.shift();
			}
		});

		function hasHistory(triages: TriageListDto[]) {
			return triages?.length > 1;
		}
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
							this.emergencyCareEpisodeService.setPatient(this.episodeId, patient.basicData.id).subscribe(
								saved => {
									if (saved) {
										this.snackBarService.showSuccess('guardia.episode.search_patient.update.SUCCESS');
										this.loadPatient(patient.basicData.id);
										this.responseEmergencyCare.patient = { // TODO La unica info necesaria es el id en realidad.
											id: patient.basicData.id,
											patientMedicalCoverageId: null,
											person: null,
											typeId: null
										}
									}
								}, _ => this.snackBarService.showError('guardia.episode.search_patient.update.ERROR'));
						}
					});
				}
			})
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

	newTriage(): void {
		this.triageDefinitionsService.getTriagePath(this.emergencyCareType)
			.subscribe( ({ component }) => {
				const dialogRef = this.dialog.open(component, {data: this.episodeId});
				dialogRef.afterClosed().subscribe(idReturned => {
					if (idReturned) {
						this.loadTriages();
					}
				});
			});
	}

}

export interface TriageReduced {
	creationDate: Date;
	category: TriageCategory;
	professional: {
		firstName: string,
		lastName: string
	};
	doctorsOfficeDescription: string;
}

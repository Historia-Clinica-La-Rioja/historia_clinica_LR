import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';

import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PermissionsService } from '@core/services/permissions.service';

import { InternmentEpisodeSummary } from '@presentation/components/internment-episode-summary/internment-episode-summary.component';
import { MapperService } from '@presentation/services/mapper.service';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';

import { PatientService } from '@api-rest/services/patient.service';
import {
	BasicPatientDto,
	InternmentSummaryDto,
	AnamnesisSummaryDto,
	EpicrisisSummaryDto,
	EvaluationNoteSummaryDto,
	PatientDischargeDto,
	AllergyConditionDto,
	HealthHistoryConditionDto,
	ImmunizationDto,
	MedicationDto,
	Last2VitalSignsDto,
	AnthropometricDataDto,

} from '@api-rest/api-model';

import {
	AppFeature,
} from '@api-rest/api-model';

import { InternacionService } from '@api-rest/services/internacion.service';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';

import { INTERNACION, ANTECEDENTES_FAMILIARES, ANTECEDENTES_PERSONALES } from '../../../../constants/summaries';
import { ROLES_FOR_EDIT_DIAGNOSIS } from '../../constants/permissions';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { ProbableDischargeDialogComponent } from '../../../../dialogs/probable-discharge-dialog/probable-discharge-dialog.component';
import { momentParseDateTime } from '@core/utils/moment.utils';
import { Moment } from 'moment';

@Component({
	selector: 'app-internacion-paciente',
	templateUrl: './internacion-paciente.component.html',
	styleUrls: ['./internacion-paciente.component.scss']
})
export class InternacionPacienteComponent implements OnInit {

	public patient$: Observable<PatientBasicData>;

	public internacionSummary = INTERNACION;
	public anamnesisDoc: AnamnesisSummaryDto;
	public epicrisisDoc: EpicrisisSummaryDto;
	public lastEvolutionNoteDoc: EvaluationNoteSummaryDto;
	public lastProbableDischargeDate: Moment;
	public internmentEpisodeId: number;
	public internmentEpisodeSummary$: Observable<InternmentEpisodeSummary>;
	public showDischarge: boolean;
	public editDiagnosisSummary$: boolean;
	public hasMedicalDischarge: boolean;
	public canLoadProbableDischargeDate: boolean;
	public allergies$: Observable<AllergyConditionDto[]>;
	public familyHistories$: Observable<HealthHistoryConditionDto[]>;
	public personalHistory$: Observable<HealthHistoryConditionDto[]>;
	public immunizations$: Observable<ImmunizationDto[]>;
	public medications$: Observable<MedicationDto[]>;
	public vitalSigns$: Observable<Last2VitalSignsDto>;
	public anthropometricData$: Observable<AnthropometricDataDto>;
	public readonly familyHistoriesHeader = ANTECEDENTES_FAMILIARES;
	public readonly personalHistoriesHeader = ANTECEDENTES_PERSONALES;

	constructor(
		private patientService: PatientService,
		private internmentService: InternacionService,
		private mapperService: MapperService,
		private route: ActivatedRoute,
		private router: Router,
		private featureFlagService: FeatureFlagService,
		private readonly permissionService: PermissionsService,
		private internmentEpisodeService: InternmentEpisodeService,
		private readonly internmentStateService: InternmentStateService,
		public dialog: MatDialog
	) { }

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				const patientId = Number(params.get('idPaciente'));
				this.internmentEpisodeId = Number(params.get('idInternacion'));

				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(patientId).pipe(
					map(patient => this.mapperService.toPatientBasicData(patient))
				);

				this.internmentEpisodeSummary$ = this.internmentService.getInternmentEpisodeSummary(this.internmentEpisodeId).pipe(
					tap((internmentEpisode: InternmentSummaryDto) => {
						this.anamnesisDoc = internmentEpisode.documents?.anamnesis;
						this.epicrisisDoc = internmentEpisode.documents?.epicrisis;
						this.lastEvolutionNoteDoc = internmentEpisode.documents?.lastEvaluationNote;
						this.lastProbableDischargeDate = internmentEpisode.probableDischargeDate ? momentParseDateTime(internmentEpisode.probableDischargeDate) : undefined;
						//La alta administrativa está disponible cuando existe el alta medica
						//o el flag de alta sin epicrisis está activa
						this.featureFlagService.isActive(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS).subscribe(isOn => {
							this.showDischarge = isOn || (this.hasMedicalDischarge === true);
						});

					}),
					map((internmentEpisode: InternmentSummaryDto) => this.mapperService.toInternmentEpisodeSummary(internmentEpisode))
				);

				this.internmentEpisodeService.getPatientDischarge(this.internmentEpisodeId)
					.subscribe((patientDischarge: PatientDischargeDto) => {
						this.hasMedicalDischarge = patientDischarge.dischargeTypeId !== 0;
					});

				this.featureFlagService.isActive(AppFeature.HABILITAR_CARGA_FECHA_PROBABLE_ALTA).subscribe(isOn => {
					this.canLoadProbableDischargeDate = isOn;
				});
				this.initSummaries();
			}
		);
		this.permissionService.hasContextAssignments$(ROLES_FOR_EDIT_DIAGNOSIS).subscribe(
			hasRole => this.editDiagnosisSummary$ = !hasRole
		);
	}

	goToAnamnesis(): void {
		if (this.anamnesisDoc?.id)
			this.router.navigate([`${this.router.url}/anamnesis/${this.anamnesisDoc?.id}`]);
		else
			this.router.navigate([`${this.router.url}/anamnesis`]);
	}

	goToNotaEvolucion(): void {
		this.router.navigate([`${this.router.url}/nota-evolucion`]);
	}

	goToEpicrisis(): void {
		this.router.navigate([`${this.router.url}/epicrisis`]);
	}

	goToAdministrativeDischarge(): void {
		this.router.navigate([`${this.router.url}/alta`]);
	}

	goToMedicalDischarge(): void {
		this.router.navigate([`${this.router.url}/alta-medica`]);
	}

	goToPaseCama(): void {
		this.router.navigate([`${this.router.url}/pase-cama`]);
	}

	initSummaries() {
		this.allergies$ = this.internmentStateService.getAllergies(this.internmentEpisodeId);
		this.familyHistories$ = this.internmentStateService.getFamilyHistories(this.internmentEpisodeId);
		this.personalHistory$ = this.internmentStateService.getPersonalHistories(this.internmentEpisodeId);
		this.immunizations$ = this.internmentStateService.getImmunizations(this.internmentEpisodeId);
		this.medications$ = this.internmentStateService.getMedications(this.internmentEpisodeId);
		this.vitalSigns$ = this.internmentStateService.getVitalSigns(this.internmentEpisodeId);
		this.anthropometricData$ = this.internmentStateService.getAnthropometricData(this.internmentEpisodeId);
	}

	openDialog() {
		const dialogRef = this.dialog.open(ProbableDischargeDialogComponent, {
			disableClose: true,
			width: '35%',
			data: {
				internmentEpisodeId: this.internmentEpisodeId,
				lastProbableDischargeDate: this.lastProbableDischargeDate
			}
		});

		dialogRef.afterClosed().subscribe(submitted => {
			if (submitted) {
				this.internmentEpisodeSummary$ = this.internmentService.getInternmentEpisodeSummary(this.internmentEpisodeId).pipe(
					map((internmentEpisode: InternmentSummaryDto) => this.mapperService.toInternmentEpisodeSummary(internmentEpisode))
				);
			}
		}
		);
	}

}

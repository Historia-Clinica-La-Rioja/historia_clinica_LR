import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
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
	PersonPhotoDto,
	InternmentEpisodeProcessDto
} from '@api-rest/api-model';

import { AppFeature } from '@api-rest/api-model';

import { InternacionService } from '@api-rest/services/internacion.service';
import { INTERNACION, ANTECEDENTES_FAMILIARES, ANTECEDENTES_PERSONALES, MEDICACION } from '../../../../../../constants/summaries';
import { ROLES_FOR_EDIT_DIAGNOSIS } from '../../../internacion/constants/permissions';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { ProbableDischargeDialogComponent } from '../../../../../../dialogs/probable-discharge-dialog/probable-discharge-dialog.component';
import { Moment } from 'moment';
import { ContextService } from '@core/services/context.service';
import { InternmentFields, InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { EvolutionNoteDockPopupComponent } from '../../dialogs/evolution-note-dock-popup/evolution-note-dock-popup.component';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { AnamnesisDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/anamnesis-dock-popup/anamnesis-dock-popup.component";
import {
	EpicrisisDockPopupComponent
} from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/epicrisis-dock-popup/epicrisis-dock-popup.component";
import { MedicalDischargeComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/medical-discharge/medical-discharge.component";
import { PatientAllergiesService } from "@historia-clinica/modules/ambulatoria/services/patient-allergies.service";

@Component({
	selector: 'app-internacion-paciente',
	templateUrl: './internacion-paciente.component.html',
	styleUrls: ['./internacion-paciente.component.scss']
})
export class InternacionPacienteComponent implements OnInit {

	public patient$: Observable<PatientBasicData>;

	public personPhoto: PersonPhotoDto;
	public internacionSummary = INTERNACION;
	public anamnesisDoc: AnamnesisSummaryDto;
	public epicrisisDoc: EpicrisisSummaryDto;
	public lastProbableDischargeDate: Moment;
	public internmentEpisodeSummary$: Observable<InternmentEpisodeSummary>;
	public showDischarge: boolean;
	public editDiagnosisSummary$: boolean;
	public hasMedicalDischarge: boolean;
	public canLoadProbableDischargeDate: boolean;
	public showPatientCard = false;
	patientId: number;
	public readonly familyHistoriesHeader = ANTECEDENTES_FAMILIARES;
	public readonly personalHistoriesHeader = ANTECEDENTES_PERSONALES;
	public readonly medicationsHeader = MEDICACION;
	dialogRef: DockPopupRef;
	private routePrefix;
	@Input() internmentEpisodeInfo: InternmentEpisodeProcessDto;
	internmentEpisodeId: number;

	constructor(
		private patientService: PatientService,
		private internmentService: InternacionService,
		private mapperService: MapperService,
		private route: ActivatedRoute,
		private router: Router,
		private featureFlagService: FeatureFlagService,
		private readonly permissionService: PermissionsService,
		private contextService: ContextService,
		public readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly internmentStateService: InternmentStateService,
		private readonly dockPopupService: DockPopupService,
		private readonly dialog: MatDialog,
		private readonly patientAllergies: PatientAllergiesService,
	) {
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				if (params.get('idInternacion')) {
					this.internmentEpisodeId = Number(params.get('idInternacion'));
					this.showPatientCard = true;
				} else {
					this.internmentEpisodeId = this.internmentEpisodeInfo?.id;
				}
				this.routePrefix = 'institucion/' + this.contextService.institutionId + '/internaciones/internacion/' + this.internmentEpisodeId + '/paciente/' + this.patientId;

				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).pipe(
					map(patient => this.mapperService.toPatientBasicData(patient))
				);

				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => { this.personPhoto = personPhotoDto; });
			});

		this.internmentSummaryFacadeService.anamnesis$.subscribe(a => this.anamnesisDoc = a);
		this.internmentSummaryFacadeService.epicrisis$.subscribe(e => this.epicrisisDoc = e);
		this.internmentSummaryFacadeService.hasMedicalDischarge$.subscribe(h => {
			this.hasMedicalDischarge = h
			// La alta administrativa está disponible cuando existe el alta medica
			// o el flag de alta sin epicrisis está activa
			this.featureFlagService.isActive(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS).subscribe(isOn => {
				this.showDischarge = isOn || (h === true);
			});
		});
		this.internmentSummaryFacadeService.lastProbableDischargeDate$.subscribe(l => this.lastProbableDischargeDate = l);

		this.internmentEpisodeSummary$ = this.internmentService.getInternmentEpisodeSummary(this.internmentEpisodeId).pipe(
			map((internmentEpisode: InternmentSummaryDto) => this.mapperService.toInternmentEpisodeSummary(internmentEpisode))
		);
		this.permissionService.hasContextAssignments$(ROLES_FOR_EDIT_DIAGNOSIS).subscribe(
			hasRole => this.editDiagnosisSummary$ = hasRole
		);
		this.featureFlagService.isActive(AppFeature.HABILITAR_CARGA_FECHA_PROBABLE_ALTA).subscribe(isOn => {
			this.canLoadProbableDischargeDate = isOn;
		});

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

	openAnamnesis() {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(AnamnesisDockPopupComponent, {
				patientInfo: {
					patientId: this.patientId,
					internmentEpisodeId: this.internmentEpisodeId,
					anamnesisId: this.anamnesisDoc?.id
				},
				autoFocus: false,
				disableClose: true,
			});
			this.dialogRef.afterClosed().subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				if (fieldsToUpdate)
					this.updateInternmentSummary(fieldsToUpdate);
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

	openEvolutionNote() {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(EvolutionNoteDockPopupComponent, {
				internmentEpisodeId: this.internmentEpisodeId,
				autoFocus: false,
				disableClose: true,
			});
			this.dialogRef.afterClosed().subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				if (fieldsToUpdate)
					this.updateInternmentSummary(fieldsToUpdate);
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

	openEpicrisis() {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(EpicrisisDockPopupComponent, {
				patientInfo: {
					patientId: this.patientId,
					internmentEpisodeId: this.internmentEpisodeId,
				},
				autoFocus: false,
				disableClose: true,
			});
			this.dialogRef.afterClosed().subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				if (fieldsToUpdate)
					this.updateInternmentSummary(fieldsToUpdate);
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

	updateInternmentSummary(fieldsToUpdate: InternmentFields) {
		const fields = {
			personalHistories: fieldsToUpdate?.personalHistories,
			riskFactors: fieldsToUpdate?.riskFactors,
			medications: fieldsToUpdate?.medications,
			anthropometricData: fieldsToUpdate?.anthropometricData,
			immunizations: fieldsToUpdate?.immunizations,
			mainDiagnosis: fieldsToUpdate?.mainDiagnosis,
			diagnosis: fieldsToUpdate?.diagnosis,
			evolutionClinical: fieldsToUpdate?.evolutionClinical
		}
		this.internmentSummaryFacadeService.setFieldsToUpdate(fields);
		if (fieldsToUpdate?.familyHistories)
			this.internmentSummaryFacadeService.unifyFamilyHistories(this.patientId);
		if (fieldsToUpdate?.allergies) {
			this.patientAllergies.updateCriticalAllergies(this.patientId);
			this.internmentSummaryFacadeService.unifyAllergies(this.patientId);
		}
		this.internmentSummaryFacadeService.updateInternmentEpisode();
	}

	openMedicalDischarge() {
		const dialogRef = this.dialog.open(MedicalDischargeComponent, {
			data: {
				patientId: this.patientId,
				internmentEpisodeId: this.internmentEpisodeId,
			},
			autoFocus: false,
			disableClose: true,
		});
		dialogRef.afterClosed().subscribe(medicalDischarge => {
			if (medicalDischarge) {
				this.internmentSummaryFacadeService.updateInternmentEpisode();
			}
		});
	}
}

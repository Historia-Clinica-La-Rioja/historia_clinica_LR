import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';

import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PermissionsService } from '@core/services/permissions.service';

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
import { ProbableDischargeDialogComponent } from '../../../../../../dialogs/probable-discharge-dialog/probable-discharge-dialog.component';
import { Moment } from 'moment';
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { InternmentPatientService } from '@api-rest/services/internment-patient.service';
import { InternmentEpisodeSummary } from "@historia-clinica/modules/ambulatoria/modules/internacion/components/internment-episode-summary/internment-episode-summary.component";

@Component({
	selector: 'app-internacion-paciente',
	templateUrl: './internacion-paciente.component.html',
	styleUrls: ['./internacion-paciente.component.scss']
})
export class InternacionPacienteComponent implements OnInit {

	patient$: Observable<PatientBasicData>;
	personPhoto: PersonPhotoDto;
	anamnesisDoc: AnamnesisSummaryDto;
	epicrisisDoc: EpicrisisSummaryDto;
	lastProbableDischargeDate: Moment;
	internmentEpisodeSummary$: Observable<InternmentEpisodeSummary>;
	showDischarge: boolean;
	editDiagnosisSummary$: boolean;
	patientDischarge: PatientDischarge = { medicalDischarge: null, physicalDischarge: null };
	canLoadProbableDischargeDate: boolean;
	showPatientCard = false;
	patientId: number;
	dialogRef: DockPopupRef;
	internmentEpisodeId: number;
	readonly internacionSummary = INTERNACION;
	readonly familyHistoriesHeader = ANTECEDENTES_FAMILIARES;
	readonly personalHistoriesHeader = ANTECEDENTES_PERSONALES;
	readonly medicationsHeader = MEDICACION;

	@Input() internmentEpisodeInfo: InternmentEpisodeProcessDto;

	constructor(
		private patientService: PatientService,
		private internmentService: InternacionService,
		private mapperService: MapperService,
		private route: ActivatedRoute,
		private featureFlagService: FeatureFlagService,
		private readonly permissionService: PermissionsService,
		private readonly internmentPatientService: InternmentPatientService,
		private readonly dialog: MatDialog,
		readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
	) { }

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

				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).pipe(
					map(patient => this.mapperService.toPatientBasicData(patient))
				);

				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => this.personPhoto = personPhotoDto);
			});

		this.internmentSummaryFacadeService.anamnesis$.subscribe(a => this.anamnesisDoc = a);
		this.internmentSummaryFacadeService.epicrisis$.subscribe(e => this.epicrisisDoc = e);
		this.internmentPatientService.internmentEpisodeIdInProcess(this.patientId).subscribe(internmentEpisode => {
			this.internmentSummaryFacadeService.hasMedicalDischarge$.subscribe(h => {
				this.patientDischarge.medicalDischarge = h;
				// La alta administrativa está disponible cuando existe el alta medica
				// o el flag de alta sin epicrisis está activa
				this.featureFlagService.isActive(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS).subscribe(isOn => {
					this.showDischarge = (isOn || (h === true)) && internmentEpisode?.inProgress;
				});
			})
		});
		this.internmentSummaryFacadeService.lastProbableDischargeDate$.subscribe(l => this.lastProbableDischargeDate = l);
		this.internmentSummaryFacadeService.hasPhysicalDischarge$.subscribe(p => this.patientDischarge.physicalDischarge = p);

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

	openDialog(): void {
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
export interface PatientDischarge {
	medicalDischarge?: Date;
	physicalDischarge?: Date;
}

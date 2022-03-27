import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';

import { MasterDataInterface, HealthConditionDto, DiagnosisDto } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { TableModel, ActionDisplays } from '@presentation/components/table/table.component';

import { DIAGNOSTICOS } from '../../../../constants/summaries';
import { RemoveDiagnosisComponent } from '../../../../dialogs/remove-diagnosis/remove-diagnosis.component';
import { ContextService } from '@core/services/context.service';

import { HEALTH_CLINICAL_STATUS } from "@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids";
import { InternmentFields, InternmentSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service';
import { DiagnosisClinicalEvaluationDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/diagnosis-clinical-evaluation-dock-popup/diagnosis-clinical-evaluation-dock-popup.component";
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { DockPopupService } from "@presentation/services/dock-popup.service";

export const COVID_SNOMED = { sctid: '186747009', pt: 'infección por coronavirus' };

@Component({
	selector: 'app-diagnosis-summary',
	templateUrl: './diagnosis-summary.component.html',
	styleUrls: ['./diagnosis-summary.component.scss']
})
export class DiagnosisSummaryComponent implements OnInit, OnChanges {

	@Input() internmentEpisodeId: number;
	@Input() editable = true;
	@Input() diagnosis: DiagnosisDto[];

	diagnosticosSummary = DIAGNOSTICOS;
	verifications: MasterDataInterface<string>[];
	clinicalStatus: MasterDataInterface<string>[];
	tableModel: TableModel<HealthConditionDto>;
	patientId: number;
	dialogRef: DockPopupRef;

	constructor(
		private readonly internmentStateService: InternmentStateService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		public dialog: MatDialog,
		private readonly route: ActivatedRoute,
		private readonly contextService: ContextService,
		private readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly dockPopupService: DockPopupService,
	) { }

	ngOnInit(): void {
		this.loadClinicalStatus();
		this.loadVerifications();
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.loadDiagnosesGeneral();
			});
	}

	ngOnChanges() {
		const healthConditioDto: HealthConditionDto[] = this.diagnosis.map((diagnosis: DiagnosisDto) => {
			return {
				id: diagnosis.id,
				statusId: diagnosis.statusId,
				snomed: diagnosis.snomed,
				verificationId: diagnosis.verificationId
			}
		});

		this.tableModel = this.buildTable(healthConditioDto);
	}

	private loadClinicalStatus(): void {
		this.internacionMasterDataService.getHealthClinical().subscribe(healthClinical => {
			this.clinicalStatus = healthClinical;
		});
	}

	private loadVerifications(): void {
		this.internacionMasterDataService.getHealthVerification().subscribe(healthVerification => {
			this.verifications = healthVerification;
		});
	}

	private loadDiagnosesGeneral(): void {
		this.internmentStateService.getAlternativeDiagnosesGeneralState(this.internmentEpisodeId).subscribe(
			data => {
				this.tableModel = this.buildTable(data);
			}
		);
	}

	private buildTable(data: HealthConditionDto[]): TableModel<HealthConditionDto> {
		const model: TableModel<HealthConditionDto> = {
			columns: [
				{
					columnDef: 'diagnosis',
					header: 'internaciones.anamnesis.diagnosticos.DIAGNOSIS',
					text: (row) => row.snomed.pt
				},
				{
					columnDef: 'status',
					header: 'Estado',
					text: (row) => this.clinicalStatus?.find(status => status.id === row.statusId)?.description
				},
				{
					columnDef: 'verification',
					header: 'Verificación',
					text: (row) => this.verifications?.find(verification => verification.id === row.verificationId)?.description
				},
			],
			data
		};
		if (this.editable && data.some(d => d.statusId === HEALTH_CLINICAL_STATUS.ACTIVO)) {
			model.columns.push({
				columnDef: 'clinicalEval',
				header: 'Evaluación clínica',
				action: {
					displayType: ActionDisplays.ICON,
					display: 'note_add',
					matColor: 'primary',
					do: row => {
						this.openClinicalEvaluation(row.id);
					},
					hide: row => row.statusId !== HEALTH_CLINICAL_STATUS.ACTIVO
				},
			});
			model.columns.push({
				columnDef: 'remove',
				action: {
					displayType: ActionDisplays.ICON,
					display: 'delete',
					matColor: 'warn',
					do: row => {
						const diagnosis = { ...row };
						const dialogRef = this.dialog.open(RemoveDiagnosisComponent, {
							disableClose: true,
							data: {
								diagnosis,
								internmentEpisodeId: this.internmentEpisodeId
							}
						});
						dialogRef.afterClosed().subscribe(
							() => {
								this.loadDiagnosesGeneral();
								this.internmentSummaryFacadeService.loadEvolutionNotes();
							}
						);
					},
					hide: row => row.statusId !== HEALTH_CLINICAL_STATUS.ACTIVO
				},
			});
		}
		return model;
	}

	openClinicalEvaluation(id: number): void {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(DiagnosisClinicalEvaluationDockPopupComponent, {
				diagnosisInfo: {
					internmentEpisodeId: this.internmentEpisodeId,
					diagnosisId: id,
				},
				autoFocus: false,
				disableClose: true,
			});
			this.dialogRef.afterClosed().subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				if (fieldsToUpdate) {
					this.internmentSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
				}
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

}

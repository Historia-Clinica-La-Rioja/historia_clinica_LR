import { Component, OnInit, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';

import { MasterDataInterface, HealthConditionDto } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { TableModel, ActionDisplays } from '@presentation/components/table/table.component';

import { DIAGNOSTICOS } from '../../constants/summaries';
import { RemoveDiagnosisComponent } from '../../dialogs/remove-diagnosis/remove-diagnosis.component';
import { ContextService } from '@core/services/context.service';

import { HEALTH_CLINICAL_STATUS } from "@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids";
import { InternmentSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service';

export const COVID_SNOMED = { sctid: '186747009', pt: 'infección por coronavirus' };

@Component({
	selector: 'app-diagnosis-summary',
	templateUrl: './diagnosis-summary.component.html',
	styleUrls: ['./diagnosis-summary.component.scss']
})
export class DiagnosisSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() editable = true;

	diagnosticosSummary = DIAGNOSTICOS;
	verifications: MasterDataInterface<string>[];
	clinicalStatus: MasterDataInterface<string>[];
	tableModel: TableModel<HealthConditionDto>;
	patientId: number;

	constructor(
		private readonly internmentStateService: InternmentStateService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly router: Router,
		public dialog: MatDialog,
		private readonly route: ActivatedRoute,
		private readonly contextService: ContextService,
		private readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
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
						const url_prefix = `institucion/${this.contextService.institutionId}/internaciones/internacion/${this.internmentEpisodeId}/paciente/${this.patientId}/`;
						this.router.navigate([url_prefix + `eval-clinica-diagnosticos/${row.id}`]);
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

}

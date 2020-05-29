import { Component, OnInit, Input } from '@angular/core';
import { DIAGNOSTICOS } from '../../constants/summaries';
import { MasterDataInterface, HealthConditionDto } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { TableModel, ActionDisplays } from 'src/app/modules/presentation/components/table/table.component';
import { MatDialog } from '@angular/material/dialog';
import { RemoveDiagnosisComponent } from '../../dialogs/remove-diagnosis/remove-diagnosis.component';
import { HEALTH_CLINICAL_STATUS } from '../../constants/ids';
import { Router } from '@angular/router';

@Component({
	selector: 'app-diagnosis-summary',
	templateUrl: './diagnosis-summary.component.html',
	styleUrls: ['./diagnosis-summary.component.scss']
})
export class DiagnosisSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() editable: boolean = true;

	diagnosticosSummary = DIAGNOSTICOS;
	verifications: MasterDataInterface<string>[];
	clinicalStatus: MasterDataInterface<string>[];
	tableModel: TableModel<HealthConditionDto>;

	constructor(
		private readonly internmentStateService: InternmentStateService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly router: Router,
		public dialog: MatDialog,
	) { }

	ngOnInit(): void {
		this.internacionMasterDataService.getHealthClinical().subscribe(healthClinical => {
			this.clinicalStatus = healthClinical;
		});

		this.internacionMasterDataService.getHealthVerification().subscribe(healthVerification => {
			this.verifications = healthVerification;
		});

		this.internmentStateService.getDiagnosis(this.internmentEpisodeId).subscribe(
			data => this.tableModel = this.buildTable(data)
		);
	}

	private buildTable(data: HealthConditionDto[]): TableModel<HealthConditionDto> {
		let model: TableModel<HealthConditionDto> = {
			columns: [
				{
					columnDef: 'diagnosis',
					header: 'Diagnóstico',
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
						this.router.navigate([`${this.router.url}/eval-clinica-diagnosticos/${row.id}`]);
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
							() => this.internmentStateService.getDiagnosis(this.internmentEpisodeId).subscribe(
								data => this.tableModel = this.buildTable(data)
							)
						);
					},
					hide: row => row.statusId !== HEALTH_CLINICAL_STATUS.ACTIVO
				},
			});
		}
		return model;
	}

}

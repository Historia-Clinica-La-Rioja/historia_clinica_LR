import { Component, OnInit, Input } from '@angular/core';
import { DIAGNOSTICOS } from '../../constants/summaries';
import { MasterDataInterface, HealthConditionDto } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { TableModel } from 'src/app/modules/presentation/components/table/table.component';
import { MatDialog } from '@angular/material/dialog';
import { RemoveDiagnosisComponent } from '../../dialogs/remove-diagnosis/remove-diagnosis.component';

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
		private internmentStateService: InternmentStateService,
		private internacionMasterDataService: InternacionMasterDataService,
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
		if (this.editable) {
			model.columns.push({
				columnDef: 'remove',
				action: {
					isDelete: true,
					do: (row) => {
						const diagnosis = {...row};
						const dialogRef = this.dialog.open(RemoveDiagnosisComponent, {
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
					}
				}
			})
		}
		return model;
	}

}

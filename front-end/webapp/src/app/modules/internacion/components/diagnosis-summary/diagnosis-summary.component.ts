import { Component, OnInit, Input } from '@angular/core';
import { DIAGNOSTICOS } from '../../constants/summaries';
import { MasterDataInterface, HealthConditionDto } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { map } from 'rxjs/operators';
import { TableModel } from 'src/app/modules/presentation/components/table/table.component';

@Component({
	selector: 'app-diagnosis-summary',
	templateUrl: './diagnosis-summary.component.html',
	styleUrls: ['./diagnosis-summary.component.scss']
})
export class DiagnosisSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;

	diagnosticosSummary = DIAGNOSTICOS;
	verifications: MasterDataInterface<string>[];
	clinicalStatus: MasterDataInterface<string>[];
	tableModel: TableModel<HealthConditionSummary>;

	constructor(
		private internmentStateService: InternmentStateService,
		private internacionMasterDataService: InternacionMasterDataService,
	) { }

	ngOnInit(): void {
		this.internacionMasterDataService.getHealthClinical().subscribe(healthClinical => {
			this.clinicalStatus = healthClinical;
		});

		this.internacionMasterDataService.getHealthVerification().subscribe(healthVerification => {
			this.verifications = healthVerification;
		});

		this.internmentStateService.getDiagnosis(this.internmentEpisodeId).pipe(
			map(diagnosis => diagnosis.map((hc): HealthConditionSummary => {
				return {
					statusId: hc.statusId,
					verificationId: hc.verificationId,
					description: hc.snomed.pt,
				}
			}))
		).subscribe(
			data => this.tableModel = this.buildTable(data)
		);
	}

	private buildTable(data: HealthConditionSummary[]): TableModel<HealthConditionSummary> {
		return {
			columns: [
				{
					columnDef: 'diagnosis',
					header: 'Diagnóstico',
					text: (row) => row.description
				},
				{
					columnDef: 'status',
					header: 'Estado',
					text: (row) => this.clinicalStatus?.find(status => status.id === row.statusId).description
				},
				{
					columnDef: 'verification',
					header: 'Verificación',
					text: (row) => this.verifications?.find(verification => verification.id === row.verificationId).description
				},
			],
			data
		};
	}

}

export interface HealthConditionSummary {
	statusId: string,
	verificationId: string,
	description: string
}

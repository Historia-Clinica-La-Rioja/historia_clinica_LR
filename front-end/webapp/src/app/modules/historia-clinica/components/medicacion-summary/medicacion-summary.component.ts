import { Component, Input, OnInit } from '@angular/core';
import { MEDICACION } from '../../constants/summaries';
import { TableModel } from '@presentation/components/table/table.component';
import { HealthConditionDto, MasterDataInterface, MedicationDto } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';

@Component({
  selector: 'app-medicacion-summary',
  templateUrl: './medicacion-summary.component.html',
  styleUrls: ['./medicacion-summary.component.scss']
})
export class MedicacionSummaryComponent implements OnInit {
	@Input() internmentEpisodeId: number;

	public readonly medicacionSummary = MEDICACION;

	tableModel: TableModel<HealthConditionDto>;

	clinicalStatus: MasterDataInterface<string>[];

	constructor(
		private internmentStateService: InternmentStateService,
		private internacionMasterDataService: InternacionMasterDataService
	) {
	}

	ngOnInit(): void {
		this.internacionMasterDataService.getMedicationClinical().subscribe(medicationClinical => {
			this.clinicalStatus = medicationClinical;
		});

		this.internmentStateService.getMedications(this.internmentEpisodeId).subscribe(
			data => this.tableModel = this.buildTable(data)
		);
	}

	private buildTable(data: MedicationDto[]): TableModel<MedicationDto> {
		return {
			columns: [
				{
					columnDef: 'medication',
					header: 'Medicación',
					text: (row) => row.snomed.pt
				},
				{
					columnDef: 'status',
					header: 'Estado',
					text: (row) => this.clinicalStatus?.find(status => status.id === row.statusId)?.description
				},
			],
			data
		};
	}
}

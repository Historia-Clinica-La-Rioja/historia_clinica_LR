import { Component, Input, OnChanges } from '@angular/core';
import { TableModel } from '@presentation/components/table/table.component';
import { HealthConditionDto, MasterDataInterface, MedicationDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { SummaryHeader } from "@presentation/components/summary-card/summary-card.component";

@Component({
	selector: 'app-medicacion-summary',
	templateUrl: './medicacion-summary.component.html',
	styleUrls: ['./medicacion-summary.component.scss']
})

export class MedicacionSummaryComponent implements OnChanges {
	@Input() medications: MedicationDto[];
	@Input() medicationsHeader: SummaryHeader;

	tableModel: TableModel<HealthConditionDto>;

	clinicalStatus: MasterDataInterface<string>[];

	constructor(
		private internacionMasterDataService: InternacionMasterDataService
	) {
	}

	ngOnChanges(): void {
		this.internacionMasterDataService.getMedicationClinical().subscribe(medicationClinical => {
			this.clinicalStatus = medicationClinical;
		});

		this.tableModel = this.buildTable(this.medications);
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

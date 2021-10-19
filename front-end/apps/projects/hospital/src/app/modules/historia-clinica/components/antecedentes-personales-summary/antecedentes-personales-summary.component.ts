import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {TableModel} from '@presentation/components/table/table.component';
import {HCEPersonalHistoryDto, HealthConditionDto} from '@api-rest/api-model';
import {SummaryHeader} from '@presentation/components/summary-card/summary-card.component';
import {InternacionMasterDataService} from '@api-rest/services/internacion-master-data.service';
import {DateFormat, momentFormat, momentParseDate} from '@core/utils/moment.utils';

@Component({
	selector: 'app-antecedentes-personales-summary',
	templateUrl: './antecedentes-personales-summary.component.html',
	styleUrls: ['./antecedentes-personales-summary.component.scss']
})
export class AntecedentesPersonalesSummaryComponent implements OnInit, OnChanges {

	@Input() personalHistory: HCEPersonalHistoryDto[];
	@Input() personalHistoriesHeader: SummaryHeader;

	tableModel: TableModel<HealthConditionDto>;
	severityTypesMasterData: any[];

	constructor(
		private readonly internacionMasterDataService: InternacionMasterDataService
	) {}

	ngOnInit(): void {
		this.internacionMasterDataService.getHealthSeverity().subscribe(
			severityTypes => this.severityTypesMasterData = severityTypes
		);
	}

	ngOnChanges(): void {
		this.tableModel = this.buildTable(this.personalHistory);
	}

	private buildTable(data: HCEPersonalHistoryDto[]): TableModel<HCEPersonalHistoryDto> {
		return {
			columns: [
				{
					columnDef: 'tipo',
					header: 'Problema',
					text: (row) => row.snomed.pt
				},
				{
					columnDef: 'severidad',
					header: 'Severidad',
					text: (row) => row.severity ? this.getSeverityDisplayName(row.severity) : ''
				},
				{
					columnDef: 'fechaInicio',
					header: 'Desde',
					text: (row) => row.startDate ? momentFormat(momentParseDate(row.startDate), DateFormat.VIEW_DATE) : ''
				}
				],
			data
		};
	}

	getSeverityDisplayName(severityCode): string {
		return (severityCode && this.severityTypesMasterData) ?
			this.severityTypesMasterData.find(severityType => severityType.code === severityCode).display : '';
	}
}

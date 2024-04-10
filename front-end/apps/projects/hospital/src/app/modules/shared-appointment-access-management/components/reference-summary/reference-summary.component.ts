import { Component, Input, OnInit } from '@angular/core';
import { ReferenceReportDto } from '@api-rest/api-model';
import { Color } from '@presentation/colored-label/colored-label.component';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';
import { Size } from '@presentation/components/item-summary/item-summary.component';
import { PatientSummary } from '../../../hsi-components/patient-summary/patient-summary.component';
import { ContextService } from '@core/services/context.service';
import { NO_INSTITUTION } from '../../../home/home.component';

@Component({
	selector: 'app-reference-summary',
	templateUrl: './reference-summary.component.html',
	styleUrls: ['./reference-summary.component.scss']
})
export class ReferenceSummaryComponent implements OnInit {

	identiferCases = IDENTIFIER_CASES;
	size = Size.SMALL;
	destinationClinicalSpecialtiesName: string;
	isInDashboardManagerRole = false;
	@Input() referenceReport: ReferenceReport;

	constructor(
		private readonly contextService: ContextService,
	) {
		this.isInDashboardManagerRole = this.contextService.institutionId === NO_INSTITUTION;
	}
	ngOnInit(): void {
		this.destinationClinicalSpecialtiesName = this.referenceReport.dto.destinationClinicalSpecialties.join(', ');
	}
}

export interface ReferenceReport {
	dto: ReferenceReportDto;
	priority: string;
	state: ReferenceState;
	patient: PatientSummary;
}

export interface ReferenceState {
	description: string;
	color: Color;
}

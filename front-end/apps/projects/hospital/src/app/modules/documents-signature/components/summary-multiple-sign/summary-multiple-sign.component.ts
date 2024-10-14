import { Component, Input } from '@angular/core';
import { EElectronicSignatureStatus } from '@api-rest/api-model';
import { IDENTIFIER_CASES } from '@hsi-components/identifier-cases/identifier-cases.component';
import { Position } from '@presentation/components/identifier/identifier.component';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';

@Component({
	selector: 'app-summary-multiple-sign',
	templateUrl: './summary-multiple-sign.component.html',
	styleUrls: ['./summary-multiple-sign.component.scss']
})

export class SummaryMultipleSignComponent {

	@Input() data: SummaryMultipleSignData;

	identiferCases = IDENTIFIER_CASES;
	REGISTER_EDITOR_CASES_DATE_HOUR = REGISTER_EDITOR_CASES.DATE_HOUR;
	Position = Position;

	constructor() { }
}

export interface SummaryMultipleSignData {
	title: string,
	patient?: string,
	problem?: string,
	registerEditor: RegisterEditor,
	signStatus?: EElectronicSignatureStatus
}

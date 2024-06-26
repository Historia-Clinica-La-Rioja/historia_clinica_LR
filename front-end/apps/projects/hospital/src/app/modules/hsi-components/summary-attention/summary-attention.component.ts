import { Component, Input } from '@angular/core';
import { PresentationModule } from '@presentation/presentation.module';
import { IDENTIFIER_CASES, IdentifierCasesComponent } from '../identifier-cases/identifier-cases.component';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { Position } from '@presentation/components/identifier/identifier.component';

@Component({
	selector: 'app-summary-attention',
	templateUrl: './summary-attention.component.html',
	styleUrls: ['./summary-attention.component.scss'],
	standalone: true,
	imports: [PresentationModule, IdentifierCasesComponent]
})
export class SummaryAttentionComponent {

	@Input() data: SummaryAttentionData;
	identiferCases = IDENTIFIER_CASES;
	REGISTER_EDITOR_CASES_DATE_HOUR = REGISTER_EDITOR_CASES.DATE_HOUR;

	Position = Position;

	constructor() {}
}

export interface SummaryAttentionData {
	title: string,
	patient?: string,
	problem?: string,
	registerEditor: RegisterEditor
}


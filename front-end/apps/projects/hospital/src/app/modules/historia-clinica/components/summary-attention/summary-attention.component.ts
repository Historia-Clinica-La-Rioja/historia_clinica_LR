import { Component, Input } from '@angular/core';
import { IDENTIFIER_CASES } from '@hsi-components/identifier-cases/identifier-cases.component';
import { Position } from '@presentation/components/identifier/identifier.component';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';

@Component({
	selector: 'app-summary-attention',
	templateUrl: './summary-attention.component.html',
	styleUrls: ['./summary-attention.component.scss']
  })
  export class SummaryAttentionComponent {

	@Input() title: string;
	@Input() specialty: string;
	@Input() problem: string;
	@Input() registerEditor: RegisterEditor;

	identiferCases = IDENTIFIER_CASES;
	REGISTER_EDITOR_CASES_DATE_HOUR = REGISTER_EDITOR_CASES.DATE_HOUR;
	Position = Position;

	constructor() { }
}

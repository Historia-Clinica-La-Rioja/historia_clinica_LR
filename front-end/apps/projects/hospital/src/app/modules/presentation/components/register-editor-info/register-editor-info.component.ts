import { Component, Input } from '@angular/core';
import { EReferenceForwardingType } from '@api-rest/api-model';
import { DatePipeFormat } from '@core/utils/date.utils';

@Component({
	selector: 'app-register-editor-info',
	templateUrl: './register-editor-info.component.html',
	styleUrls: ['./register-editor-info.component.scss']
})
export class RegisterEditorInfoComponent {
	readonly registerEditorCasesDate = REGISTER_EDITOR_CASES.DATE;
	readonly dateFormats = DatePipeFormat;
	@Input() registerEditor: RegisterEditor;
	@Input() registerEditorCase: REGISTER_EDITOR_CASES = REGISTER_EDITOR_CASES.DATE;
}

export enum REGISTER_EDITOR_CASES {
	DATE = "date",
	DATE_HOUR = "dateHour"
}

export interface RegisterEditor {
	createdBy: string;
	institution?: string;
	date: Date;
}

export interface RegisterDerivationEditor extends RegisterEditor {
	type: EReferenceForwardingType;
	derivationType?: string;
}

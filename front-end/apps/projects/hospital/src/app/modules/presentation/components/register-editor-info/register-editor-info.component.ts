import { Component, Input } from '@angular/core';
import { Position } from '../identifier/identifier.component';

@Component({
	selector: 'app-register-editor-info',
	templateUrl: './register-editor-info.component.html',
	styleUrls: ['./register-editor-info.component.scss']
})
export class RegisterEditorInfoComponent {
	readonly registerEditorCasesDate = REGISTER_EDITOR_CASES.DATE;
	@Input() registerEditor: RegisterEditor;
	@Input() registerEditorCase: REGISTER_EDITOR_CASES;
	@Input() position: Position = Position.ROW;
}

export enum REGISTER_EDITOR_CASES {
	DATE = "date",
	DATE_HOUR = "dateHour"
}

export interface RegisterEditor {
	createdBy: string;
	institution?: string;
	date?: Date;
}

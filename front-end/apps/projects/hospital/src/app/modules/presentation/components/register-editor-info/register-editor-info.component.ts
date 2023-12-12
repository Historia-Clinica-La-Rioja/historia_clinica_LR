import { Component, Input } from '@angular/core';
import { DateDto, DateTimeDto } from '@api-rest/api-model';

@Component({
	selector: 'app-register-editor-info',
	templateUrl: './register-editor-info.component.html',
	styleUrls: ['./register-editor-info.component.scss']
})
export class RegisterEditorInfoComponent {
	registerEditorCasesDate = REGISTER_EDITOR_CASES.DATE;
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
	date: DateTimeDto | DateDto;
}

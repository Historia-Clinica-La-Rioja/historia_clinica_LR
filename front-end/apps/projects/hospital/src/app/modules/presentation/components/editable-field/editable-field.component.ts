import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';

@Component({
	selector: 'app-editable-field',
	templateUrl: './editable-field.component.html',
	styleUrls: ['./editable-field.component.scss']
})
export class EditableFieldComponent implements OnInit {

	editMode = false;
	form: UntypedFormGroup;

	@Output() newValueEmitted: EventEmitter<string> = new EventEmitter<string>();
	@Input() fieldName: string;
	@Input() value: string;

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			field: [this.value]
		});
	}

	updateFieldValue() {
		this.newValueEmitted.emit(this.form.value.field);
		this.editMode = false;
	}
}

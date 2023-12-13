import { Component, EventEmitter, OnInit, Output } from '@angular/core';

import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GenerateApiKeyDto } from '@api-rest/api-model';

@Component({
  selector: 'app-user-keys-form',
  templateUrl: './user-keys-form.component.html',
  styleUrls: ['./user-keys-form.component.scss']
})
export class UserKeysFormComponent implements OnInit {
	@Output() values = new EventEmitter<GenerateApiKeyDto | undefined>();


	form: FormGroup;
	loading = false;

	constructor(
		private formBuilder: FormBuilder,
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			name: ['', Validators.required],
		});

		this.form.valueChanges.subscribe(({name}) =>
			this.values.emit(this.form.invalid ? undefined: { name })
		);
	}

	hasError(type: string, control: string): boolean {
		return this.form.get(control).hasError(type);
	}

}

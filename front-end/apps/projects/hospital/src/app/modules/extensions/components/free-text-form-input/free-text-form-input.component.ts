import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-free-text-form-input',
	templateUrl: './free-text-form-input.component.html',
	styleUrls: ['./free-text-form-input.component.scss']
})
export class FreeTextFormInputComponent implements OnInit {

	@Input() label: string;
	@Output() textChange = new EventEmitter<string[]>();

	textForm: FormGroup;

	constructor(private formBuilder: FormBuilder) { }

	ngOnInit(): void {
		this.textForm = this.formBuilder.group({
			text: ['']
		});
		this.textForm.get('text').valueChanges.subscribe(value => this.textChange.emit([value]));
	}

}

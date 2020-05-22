import { Component, OnInit, Input } from '@angular/core';

@Component({
	selector: 'app-form-input',
	templateUrl: './form-input.component.html',
	styleUrls: ['./form-input.component.scss']
})
export class FormInputComponent implements OnInit {
	@Input('label') label: string;

	constructor() { }

	ngOnInit(): void {
	}

}

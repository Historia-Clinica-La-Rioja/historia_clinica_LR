import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
	selector: 'app-loinc-input-dropdown',
	templateUrl: './loinc-input-dropdown.component.html',
	styleUrls: ['./loinc-input-dropdown.component.scss']
})
export class LoincInputDropdownComponent {

	@Input() options;
	@Input() title: string;
	@Output() valueSelected: EventEmitter<any> = new EventEmitter<any>();
}

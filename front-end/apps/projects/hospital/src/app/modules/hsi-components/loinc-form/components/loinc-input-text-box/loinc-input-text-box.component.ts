import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
	selector: 'app-loinc-input-text-box',
	templateUrl: './loinc-input-text-box.component.html',
	styleUrls: ['./loinc-input-text-box.component.scss']
})
export class LoincInputTextBoxComponent {

	@Input() title: string;
	@Output() valueSelected: EventEmitter<string> = new EventEmitter<string>();
}

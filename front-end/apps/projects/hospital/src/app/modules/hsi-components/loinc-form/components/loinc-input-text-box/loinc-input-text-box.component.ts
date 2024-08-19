import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
	selector: 'app-loinc-input-text-box',
	templateUrl: './loinc-input-text-box.component.html',
	styleUrls: ['./loinc-input-text-box.component.scss']
})
export class LoincInputTextBoxComponent {
	_preload: string;
	@Input() set preload(preload: string) {
		this._preload = preload;
		this.valueSelected.emit(preload);
	};
	@Input() title: string;
	@Output() valueSelected: EventEmitter<string> = new EventEmitter<string>();
}

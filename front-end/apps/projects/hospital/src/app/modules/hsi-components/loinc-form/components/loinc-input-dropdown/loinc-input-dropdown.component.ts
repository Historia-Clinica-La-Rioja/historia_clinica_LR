import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
	selector: 'app-loinc-input-dropdown',
	templateUrl: './loinc-input-dropdown.component.html',
	styleUrls: ['./loinc-input-dropdown.component.scss']
})
export class LoincInputDropdownComponent {
	_preload: string;

	@Input() options: preloadKey;
	@Input() title: string;
	@Input() set preload(preload: string) {
		this._preload = preload;
		this.valueSelected.emit(preload);
	};
	@Output() valueSelected: EventEmitter<string> = new EventEmitter<string>();
}
export interface preloadKey {
	key: number,
	value: string
}

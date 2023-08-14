import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
	selector: 'app-toggle-avaiability',
	templateUrl: './toggle-avaiability.component.html',
	styleUrls: ['./toggle-avaiability.component.scss']
})
export class ToggleAvaiabilityComponent {


	@Input() label: string;
	@Output() availabilityChanged = new EventEmitter<boolean>;
	isChecked = false;
	constructor() { }

	onChanged() {
		this.availabilityChanged.next(this.isChecked)
	}
}

import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
	selector: 'app-toggle-availability',
	templateUrl: './toggle-availability.component.html',
	styleUrls: ['./toggle-availability.component.scss']
})
export class ToggleAvailabilityComponent {

	@Input() set initialValue(value: boolean) {
		this.isChecked = value;
	}

	@Input() label: string;
	@Output() availabilityChanged = new EventEmitter<boolean>;
	isChecked = false;
	constructor() { }

	onChanged() {
		this.availabilityChanged.next(this.isChecked)
	}
}

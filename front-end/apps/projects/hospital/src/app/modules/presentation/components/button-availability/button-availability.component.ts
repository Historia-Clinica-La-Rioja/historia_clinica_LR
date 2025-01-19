import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
	selector: 'app-button-availability',
	templateUrl: './button-availability.component.html',
	styleUrls: ['./button-availability.component.scss']
})
export class ButtonAvailabilityComponent {

	AvailableButtonWidth = AvailableButtonWidth;
	AvailableButtonType = AvailableButtonType;

	@Input() description: string;
	@Input() type? = AvailableButtonType.AVAILABLE;
	@Input() size? = AvailableButtonWidth.LARGE;
	@Input() isSelected? = false;

	@Output() clicked = new EventEmitter<boolean>();

	constructor() { }

	onSelection() {
		this.clicked.emit();
	}
}

export enum AvailableButtonType{
	AVAILABLE = 'available',
	NOT_AVAILABLE = 'not-available',
	DISABLE = 'disable',
	BLOCKED = 'blocked'
}

export enum AvailableButtonWidth {
    SMALL = 'small',
    LARGE = 'large'
}

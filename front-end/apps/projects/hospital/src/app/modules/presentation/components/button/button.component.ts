import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { ThemePalette } from '@angular/material/core';


@Component({
	selector: 'app-button',
	templateUrl: './button.component.html',
	styleUrls: ['./button.component.scss']
})
export class ButtonComponent implements OnChanges {

	@Input() color?: ThemePalette = 'primary';
	@Input() isLoading?= false;
	@Input() text = '';
	@Input() buttonType?= ButtonType.STOKED
	@Input() disabled?= false;

	@Output() clicked = new EventEmitter();

	width: number;
	ngOnChanges(changes: SimpleChanges): void {
		this.width = changes.isLoading?.currentValue ? changes.text?.currentValue.length * 11 : null
	}
	constructor() { }

	onClicked() {
		this.clicked.emit('Clicked!')
	}
}

export enum ButtonType {
	RAISED = 'mat-raised-button',
	FLAT = 'mat-flat-button',
	STOKED = 'mat-stroked-button',
	ICON = 'mat-icon-button'
}

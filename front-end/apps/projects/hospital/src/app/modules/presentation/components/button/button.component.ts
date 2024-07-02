import { DOCUMENT } from '@angular/common';
import { AfterViewInit, Component, EventEmitter, Inject, Input, Output } from '@angular/core';
import { ThemePalette } from '@angular/material/core';


@Component({
	selector: 'app-button',
	templateUrl: './button.component.html',
	styleUrls: ['./button.component.scss']
})
export class ButtonComponent implements AfterViewInit {

	@Input() color?: ThemePalette = 'primary';
	@Input() isLoading? = false;
	@Input() text = '';
	@Input() buttonType? = ButtonType.STROKED
	@Input() disabled? = false;
	@Input() matIcon?: string;

	@Output() clicked = new EventEmitter();

	ButtonType = ButtonType;
	constructor(
		@Inject(DOCUMENT) private document: Document
	) { }

	ngAfterViewInit() {
		this.getButtonDimensions();
	}

	onClicked() {
		this.clicked.emit('Clicked!')
	}

	private getButtonDimensions() {
		const button: HTMLElement = this.document.getElementById("button");
		if (button) {
			button.style.width = `${button.offsetWidth.toString()} px`;
			button.style.height = `${button.offsetHeight.toString()} px`;
		}
	}
}

export enum ButtonType {
	RAISED = 'mat-raised-button',
	FLAT = 'mat-flat-button',
	STROKED = 'mat-stroked-button',
	ICON = 'mat-icon-button',
	BASIC = 'mat-button',
}
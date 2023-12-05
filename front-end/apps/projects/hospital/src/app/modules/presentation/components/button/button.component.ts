import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ThemePalette } from '@angular/material/core';


@Component({
	selector: 'app-button',
	templateUrl: './button.component.html',
	styleUrls: ['./button.component.scss']
})
export class ButtonComponent {

	@Input() color?: ThemePalette = 'primary';
	@Input() isLoading?= false;
	@Input() text = '';
	@Input() buttonType?= ButtonType.STOKED
	@Input() disabled?= false;

	@Output() clicked = new EventEmitter();

	ngAfterViewInit() {
		this.getButtonDimensions();
	}

	onClicked() {
		this.clicked.emit('Clicked!')
	}

	private getButtonDimensions(){
		document.getElementById("button").style.width = document.getElementById("button").offsetWidth.toString() + 'px';
		document.getElementById("button").style.height = document.getElementById("button").offsetHeight.toString() + 'px';
	}
}

export enum ButtonType {
	RAISED = 'mat-raised-button',
	FLAT = 'mat-flat-button',
	STOKED = 'mat-stroked-button',
	ICON = 'mat-icon-button'
}
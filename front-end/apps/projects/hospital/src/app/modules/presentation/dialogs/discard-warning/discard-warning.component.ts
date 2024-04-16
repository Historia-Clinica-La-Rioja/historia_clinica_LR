import { Component, Inject, OnInit } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
	selector: 'app-discard-warning',
	templateUrl: './discard-warning.component.html',
	styleUrls: ['./discard-warning.component.scss']
})
export class DiscardWarningComponent implements OnInit {

	okBottonColor: ThemePalette;
	cancelButtonColor: ThemePalette;
	iconColor: ThemePalette;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {
			title: string,
			content: string,
			contentBold: string,
			okButtonLabel: string,
			cancelButtonLabel: string,
			okBottonColor?: ThemePalette,
			buttonClose?: boolean,
			errorMode?: boolean,
			color?: string,
			paramTranslate?: any, 
		}
	) {
	}

	ngOnInit(): void {
		this.okBottonColor = this.data.okBottonColor ? this.data.okBottonColor : 'primary';
		this.data.color = this.data.color ? this.data.color : 'primary';
	}
}


export interface DiscardWarningModel {
	title: string,
	content: string,
	contentBold: string,
	okButtonLabel: string,
	cancelButtonLabel: string,
	okBottonColor?: ThemePalette,
	buttonClose?: boolean,
	errorMode?: boolean,
	color?: string,
}
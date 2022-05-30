import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ThemePalette } from '@angular/material/core';


/**
 * Basic confirm dialog with simple text message
 * input data example :
 * data {
 *   title: 'Dialog title',
 *   content: 'This is the content dialog',
 *   okButtonLabel: 'Continue'
 * }
 */
@Component({
	selector: 'app-confirm-dialog',
	templateUrl: './confirm-dialog.component.html',
	styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent implements OnInit {

	public okBottonColor: ThemePalette;

	constructor(
		public dialogRef: MatDialogRef<ConfirmDialogComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {
			title: string,
			content: string,
			okButtonLabel: string,
			cancelButtonLabel?: string,
			okBottonColor?: ThemePalette,
			showMatIconError: boolean,
		}
	) {
	}

	ngOnInit(): void {
		this.okBottonColor = this.data.okBottonColor ? this.data.okBottonColor : 'primary';
	}

}

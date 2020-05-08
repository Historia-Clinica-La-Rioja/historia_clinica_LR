import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";


/**
 * Basic confirm dialog with simple text message
 * input data example :
 * data {
 *     title: 'Dialog title',
 *     content: 'This is the content dialog',
 *     okButtonLabel: 'Continue'
 * }
 */
@Component({
	selector: 'app-confirm-dialog',
	templateUrl: './confirm-dialog.component.html',
	styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent implements OnInit {

	constructor(public dialogRef: MatDialogRef<ConfirmDialogComponent>,
				@Inject(MAT_DIALOG_DATA) public data: any) {
	}

	ngOnInit(): void {
	}

}

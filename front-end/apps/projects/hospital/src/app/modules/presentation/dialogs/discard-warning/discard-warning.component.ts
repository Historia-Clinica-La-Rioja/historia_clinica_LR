import { Component, Inject, OnInit } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-discard-warning',
  templateUrl: './discard-warning.component.html',
  styleUrls: ['./discard-warning.component.scss']
})
export class DiscardWarningComponent implements OnInit {

	public okBottonColor: ThemePalette;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {
			title: string,
			content: string,
			contentBold: string,
			okButtonLabel: string,
			cancelButtonLabel: string,
			okBottonColor?: ThemePalette
		}
	) {
	}

	ngOnInit(): void {
		this.okBottonColor = this.data.okBottonColor ? this.data.okBottonColor : 'primary';
	}
  }



import { Component, Inject, OnInit } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';

@Component({
  selector: 'app-message-snackbar',
  templateUrl: './message-snackbar.component.html',
  styleUrls: ['./message-snackbar.component.scss']
})
export class MessageSnackbarComponent implements OnInit {

	constructor(
		private _snackRef: MatSnackBarRef<MessageSnackbarComponent>,
		@Inject(MAT_SNACK_BAR_DATA) public data: any
	) { }

	ngOnInit() {
	}

	close() {
		this._snackRef.dismiss();
	}

}

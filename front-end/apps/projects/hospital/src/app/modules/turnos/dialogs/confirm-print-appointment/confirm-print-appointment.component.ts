import { Component, Inject, OnInit } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirm-print-appointment',
  templateUrl: './confirm-print-appointment.component.html',
  styleUrls: ['./confirm-print-appointment.component.scss']
})
export class ConfirmPrintAppointmentComponent implements OnInit {


	constructor(
		public dialogRef: MatDialogRef<ConfirmPrintAppointmentComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {
			title: string,
			content: string,
		}
	) {
	}

  ngOnInit(): void {
  }

}

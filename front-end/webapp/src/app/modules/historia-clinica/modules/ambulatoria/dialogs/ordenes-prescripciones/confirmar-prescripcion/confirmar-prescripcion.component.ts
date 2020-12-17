import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmar-prescripcion',
  templateUrl: './confirmar-prescripcion.component.html',
  styleUrls: ['./confirmar-prescripcion.component.scss']
})
export class ConfirmarPrescripcionComponent implements OnInit {

	constructor(
		public dialogRef: MatDialogRef<ConfirmarPrescripcionComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {
			titleLabel: string,
		}) { }

	ngOnInit(): void {
	}

	downloadPrescription() {
		this.dialogRef.close();
	}

	closeModal() {
		this.dialogRef.close();
	}

}

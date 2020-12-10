import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmar-preinscripcion',
  templateUrl: './confirmar-preinscripcion.component.html',
  styleUrls: ['./confirmar-preinscripcion.component.scss']
})
export class ConfirmarPreinscripcionComponent implements OnInit {

	constructor(
		public dialogRef: MatDialogRef<ConfirmarPreinscripcionComponent>,
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

import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
	selector: 'app-pop-up',
	templateUrl: './estudios-popup.component.html',
	styleUrls: ['./estudios-popup.component.scss'],
})
export class EstudiosPopupComponent {
	selectedOption: string = '';
	patientId: number;

	constructor(private dialogRef: MatDialogRef<EstudiosPopupComponent>) {}

	closeModal(simpleClose: boolean, completed?: boolean): void {
		if (simpleClose) {
			this.dialogRef.close();
		} else {
		}
	}
}

import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Patient } from '@pacientes/component/search-patient/search-patient.component';

@Component({
	selector: 'app-search-patient-dialog',
	templateUrl: './search-patient-dialog.component.html',
	styleUrls: ['./search-patient-dialog.component.scss'],
})
export class SearchPatientDialogComponent {

	constructor(
		private dialogRef: MatDialogRef<SearchPatientDialogComponent>,
	) { }

	onSelectedPatient(selectedPatient: Patient) {
		this.dialogRef.close(selectedPatient);
	}
}

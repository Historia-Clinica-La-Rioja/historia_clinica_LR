import { Component, EventEmitter, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Patient } from '@pacientes/component/search-patient/search-patient.component';
import { SearchPatientDialogComponent } from '@pacientes/dialogs/search-patient-dialog/search-patient-dialog.component';

@Component({
	selector: 'app-asign-patient-button',
	templateUrl: './asign-patient-button.component.html',
	styleUrls: ['./asign-patient-button.component.scss']
})
export class AsignPatientButtonComponent {

	@Output() onSelectedPatient = new EventEmitter<Patient>();

	constructor(
		private readonly dialog: MatDialog,
	) { }

	asignPatient() {
		const dialogRef = this.dialog.open(SearchPatientDialogComponent);
		dialogRef.afterClosed()
			.subscribe((foundPatient: Patient) => {
				if (foundPatient)
					this.onSelectedPatient.next(foundPatient);
			});
	}
}

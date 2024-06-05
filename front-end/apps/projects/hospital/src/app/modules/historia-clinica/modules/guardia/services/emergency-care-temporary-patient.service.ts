import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ReasonPopUpComponent } from '@presentation/dialogs/reason-pop-up/reason-pop-up.component';
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareTemporaryPatientService {

	private patientDescriptionSubject = new Subject<string>();
	patientDescription$ = this.patientDescriptionSubject.asObservable();

	constructor(
		private readonly dialog: MatDialog,
	) { }

	openTemporaryPatient() {
		const dialogRef = this.dialog.open(ReasonPopUpComponent, {
			data: REASON_POPUP_DATA,
			width: '514px',
			autoFocus: false,
			disableClose: true,
		});

		dialogRef.afterClosed()
			.subscribe(patientDescription => 	this.patientDescriptionSubject.next(patientDescription));
	}
}

const REASON_POPUP_DATA = {
	title: "guardia.administrative_admision.form.temporary_patient_popup.TITLE",
	subtitle: "guardia.administrative_admision.form.temporary_patient_popup.SUBTITLE",
	placeholder: "guardia.administrative_admision.form.temporary_patient_popup.PLACEHOLDER"
}
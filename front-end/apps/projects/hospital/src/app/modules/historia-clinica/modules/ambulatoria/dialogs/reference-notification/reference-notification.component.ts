import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReferenceDataDto } from '@api-rest/api-model';

@Component({
	selector: 'app-reference-notification',
	templateUrl: './reference-notification.component.html',
	styleUrls: ['./reference-notification.component.scss']
})
export class ReferenceNotificationComponent {

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: ReferenceDataDto[],
		private readonly dialogRef: MatDialogRef<ReferenceNotificationComponent>,
	) { }

	goToNewConsultation(): void {
		this.dialogRef.close(false);
	}

	goBack(): void {
		this.dialogRef.close(null);
	}

	goToCounterreference(reference): void {
		const counterreference = { isACountisACounterrefer: true, reference: reference }
		this.dialogRef.close(counterreference);
	}

}

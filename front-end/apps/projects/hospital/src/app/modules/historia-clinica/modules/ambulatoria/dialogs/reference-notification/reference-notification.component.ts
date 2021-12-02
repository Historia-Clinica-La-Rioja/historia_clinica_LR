import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ReferenceDto } from '@api-rest/api-model';

@Component({
	selector: 'app-reference-notification',
	templateUrl: './reference-notification.component.html',
	styleUrls: ['./reference-notification.component.scss']
})
export class ReferenceNotificationComponent implements OnInit {

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: ReferenceDto[],
		private readonly dialogRef: MatDialogRef<ReferenceNotificationComponent>,
	) { }

	ngOnInit(): void {

	}

	goToNewConsultation(): void {
		this.dialogRef.close(false);
	}

	goBack(): void {
		this.dialogRef.close(null);
	}

}

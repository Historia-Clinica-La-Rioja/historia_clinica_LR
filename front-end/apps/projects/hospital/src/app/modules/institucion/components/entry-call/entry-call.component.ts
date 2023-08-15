import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { timeDifference } from '@core/utils/date.utils';
import { Priority } from '@presentation/components/priority/priority.component';

@Component({
	selector: 'app-entry-call',
	templateUrl: './entry-call.component.html',
	styleUrls: ['./entry-call.component.scss']
})
export class EntryCallComponent {

	timeDifference = timeDifference;
	constructor(
		@Inject(MAT_DIALOG_DATA) public entryCall: EntryCall,
		private dialogRef: MatDialogRef<EntryCallComponent>,
	) { }

	joinMeet() {
		this.dialogRef.close(true);
	}

}

export interface EntryCall {
	callId: string;
	patient: {
		id: number;
		firstName: string;
		lastName: string;
		gender?: string
	},
	professionalFullName: string
	priority: Priority;
	createdOn: Date;
	institutionName: string;
	clinicalSpecialty: string;
}

import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { timeDifference } from '@core/utils/date.utils';
import { Priority } from '@presentation/components/priority/priority.component';
import { JitsiCallService } from 'projects/hospital/src/app/modules/jitsi/jitsi-call.service';

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
		private readonly jitsiCallService: JitsiCallService,
	) { }

	joinMeet() {
		this.jitsiCallService.open(this.entryCall.callId);
		this.dialogRef.close();
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

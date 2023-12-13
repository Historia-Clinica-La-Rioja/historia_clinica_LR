import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CallDetails } from '@presentation/components/call-details/call-details.component';
import { EntryCallStompService } from '../../../api-web-socket/entry-call-stomp.service';

@Component({
	selector: 'app-entry-call',
	templateUrl: './entry-call.component.html',
	styleUrls: ['./entry-call.component.scss']
})
export class EntryCallComponent {

	constructor(
		@Inject(MAT_DIALOG_DATA) public entryCall: CallDetails,
		private dialogRef: MatDialogRef<EntryCallComponent>,
		private entryCallStompService: EntryCallStompService
	) {
		this.entryCallStompService.cancelledCall$.subscribe(
			_ => {
				this.dialogRef.close()
			}
		)
	}


	joinMeet() {
		this.dialogRef.close(true);
	}

}


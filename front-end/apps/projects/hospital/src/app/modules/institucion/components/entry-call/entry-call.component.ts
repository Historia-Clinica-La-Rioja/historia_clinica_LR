import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { JitsiCallService } from 'projects/hospital/src/app/modules/jitsi/jitsi-call.service';

@Component({
	selector: 'app-entry-call',
	templateUrl: './entry-call.component.html',
	styleUrls: ['./entry-call.component.scss']
})
export class EntryCallComponent implements OnInit {

	constructor(
		@Inject(MAT_DIALOG_DATA) public roomId,
		private dialogRef: MatDialogRef<EntryCallComponent>,
		private readonly jitsiCallService: JitsiCallService,
	) { }

	ngOnInit(): void {	}

	joinMeet() {
		this.jitsiCallService.open(this.roomId);
		this.dialogRef.close();
	}

}

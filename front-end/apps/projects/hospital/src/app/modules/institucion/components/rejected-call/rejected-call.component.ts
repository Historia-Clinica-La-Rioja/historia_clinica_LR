import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CallDetails } from '../../../telemedicina/components/call-details/call-details.component';

@Component({
	selector: 'app-rejected-call',
	templateUrl: './rejected-call.component.html',
	styleUrls: ['./rejected-call.component.scss']
})
export class RejectedCallComponent {

	constructor(
		@Inject(MAT_DIALOG_DATA) public rejectedCall: CallDetails,
	) { }

}


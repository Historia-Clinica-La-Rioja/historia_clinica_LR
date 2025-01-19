import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CallDetails } from '../call-details/call-details.component';

@Component({
	selector: 'app-in-progress-call',
	templateUrl: './in-progress-call.component.html',
	styleUrls: ['./in-progress-call.component.scss']
})
export class InProgressCallComponent {

	constructor(
		@Inject(MAT_DIALOG_DATA) public call: CallDetails,
	) { }

}

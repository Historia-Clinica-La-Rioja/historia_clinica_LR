import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-specific-nursing-record',
	templateUrl: './specific-nursing-record.component.html',
	styleUrls: ['./specific-nursing-record.component.scss']
})
export class SpecificNursingRecordComponent {

	@Input() nursingRecords: any[];

	constructor() { }

}


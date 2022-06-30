import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-general-nursing-record',
	templateUrl: './general-nursing-record.component.html',
	styleUrls: ['./general-nursing-record.component.scss']
})
export class GeneralNursingRecordComponent {

	@Input() nursingRecords: any[];

	constructor() { }

}

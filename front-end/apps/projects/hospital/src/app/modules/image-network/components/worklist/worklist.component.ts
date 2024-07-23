import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-worklist',
	templateUrl: './worklist.component.html',
	styleUrls: ['./worklist.component.scss']
})
export class WorklistComponent {

	@Input() worklist: Worklist;

	constructor() { }

}

export interface Worklist {
	patientInformation: PatientInformation;
	state: State;
	date: Date;
	appointmentId: number;
	institutionName: string
}

interface PatientInformation {
	fullName: string;
	identification: string;
}

export interface State {
	id?: number;
	description: string;
	color: string;
}

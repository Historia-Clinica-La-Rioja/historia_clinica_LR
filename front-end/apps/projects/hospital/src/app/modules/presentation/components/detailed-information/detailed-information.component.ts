import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-detailed-information',
  templateUrl: './detailed-information.component.html',
  styleUrls: ['./detailed-information.component.scss']
})
export class DetailedInformationComponent {

	@Input() detailedInformation: DetailedInformation;

  	constructor() { }

}

export interface DetailedInformation {
	id: number;
	title?: string,
	oneColumn?: DetailedInformationValue[],
	twoColumns?: DetailedInformationValue[],
	threeColumns?: DetailedInformationValue[]
}

export interface DetailedInformationValue {
	icon?: string,
	title: string,
	value: string[],
}

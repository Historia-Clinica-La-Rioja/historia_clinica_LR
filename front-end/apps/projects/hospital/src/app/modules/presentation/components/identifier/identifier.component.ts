import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-identifier',
	templateUrl: './identifier.component.html',
	styleUrls: ['./identifier.component.scss']
})
export class IdentifierComponent {

	@Input() identifier: Identifier;
	@Input() position = Position.ROW;
	@Input() showLegend = false;

	constructor() { }

}

export interface Identifier {
	iconLegend: IconLegend;
	description: string;
}

export interface IconLegend {
	icon: string;
	legend: string;
}

export enum Position {
	ROW = 'row',
	COLUMN = 'column',
	ROW_WRAP = 'row wrap',
}

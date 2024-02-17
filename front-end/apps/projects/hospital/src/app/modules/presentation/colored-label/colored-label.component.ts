import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-colored-label',
	templateUrl: './colored-label.component.html',
	styleUrls: ['./colored-label.component.scss']
})
export class ColoredLabelComponent {

	@Input() description: string;
	@Input() color: Color;
	@Input() icon?: string;

	constructor() { }

}

export enum Color {
	RED = 'red', GREEN = 'green', YELLOW = 'yellow', GREY = 'grey', BLUE = 'blue', PURPLE = 'purple'
}

export interface ColoredLabel {
	description: string; 
	color: Color;
	icon?: string;
}
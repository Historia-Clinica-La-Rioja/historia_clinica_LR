import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-iconed-text',
	templateUrl: './iconed-text.component.html',
	styleUrls: ['./iconed-text.component.scss']
})
export class IconedTextComponent {

	@Input() icon: string;
	@Input() text: string;

	constructor() { }

}

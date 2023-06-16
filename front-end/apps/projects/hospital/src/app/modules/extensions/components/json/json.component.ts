import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-json',
	templateUrl: './json.component.html',
	styleUrls: ['./json.component.scss']
})
export class JsonComponent {
	@Input() content: any;

	constructor() { }

}

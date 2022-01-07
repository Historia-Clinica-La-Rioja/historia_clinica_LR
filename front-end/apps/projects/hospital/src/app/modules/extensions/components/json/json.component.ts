import { Component, Input } from '@angular/core';
import { UIComponentDto } from '@extensions/extensions-model';

@Component({
	selector: 'app-json',
	templateUrl: './json.component.html',
	styleUrls: ['./json.component.scss']
})
export class JsonComponent {
	@Input() content: any;

	constructor() { }

}

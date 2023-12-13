import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-available-label',
	templateUrl: './available-label.component.html',
	styleUrls: ['./available-label.component.scss']
})
export class AvailableLabelComponent {
	@Input() available: boolean;
	constructor() { }

}

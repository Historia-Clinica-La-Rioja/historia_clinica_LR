import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-bar',
	templateUrl: './bar.component.html',
	styleUrls: ['./bar.component.scss']
})
export class BarComponent {
	@Input() position = 'static';

	constructor() { }

}

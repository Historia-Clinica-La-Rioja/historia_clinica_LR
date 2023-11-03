import { Component, Output } from '@angular/core';
import { Subject } from 'rxjs';

@Component({
	selector: 'app-floating-div',
	templateUrl: './floating-div.component.html',
	styleUrls: ['./floating-div.component.scss']
})
export class FloatingDivComponent {

	@Output() afterClosed = new Subject();

	constructor() { }

	close() {
		this.afterClosed.next(true);
	}

}

import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-detail-box',
	templateUrl: './detail-box.component.html',
	styleUrls: ['./detail-box.component.scss']
})
export class DetailBoxComponent {

	@Input() detail: DetailBox;

	constructor() { }

}

export interface DetailBox {
	description: string;
	value: string | number;
}

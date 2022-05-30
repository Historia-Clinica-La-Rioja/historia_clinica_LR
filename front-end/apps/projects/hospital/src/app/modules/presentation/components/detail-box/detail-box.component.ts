import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-detail-box',
	templateUrl: './detail-box.component.html',
	styleUrls: ['./detail-box.component.scss']
})
export class DetailBoxComponent {

	@Input() detail: DetailBox;

}

export interface DetailBox {
	description: string;
	registeredValues: RegisteredValue[];
}

interface RegisteredValue {
	value: string | number;
	date: string;
}

import { Component, OnInit, Input } from '@angular/core';

@Component({
	selector: 'app-detail-box',
	templateUrl: './detail-box.component.html',
	styleUrls: ['./detail-box.component.scss']
})
export class DetailBoxComponent implements OnInit {

	@Input('data')
	detail: DetailBox;

	constructor() { }

	ngOnInit(): void { }

}

export interface DetailBox {
	description: string;
	value: string | number;
}

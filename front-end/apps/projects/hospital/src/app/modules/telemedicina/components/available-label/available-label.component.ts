import { Component, Input, OnInit } from '@angular/core';

@Component({
	selector: 'app-available-label',
	templateUrl: './available-label.component.html',
	styleUrls: ['./available-label.component.scss']
})
export class AvailableLabelComponent implements OnInit {
	@Input() available: boolean;
	constructor() { }

	ngOnInit(): void {
	}

}

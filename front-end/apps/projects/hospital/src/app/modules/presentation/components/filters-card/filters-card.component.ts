import { Component, Input, OnInit } from '@angular/core';

@Component({
	selector: 'app-filters-card',
	templateUrl: './filters-card.component.html',
	styleUrls: ['./filters-card.component.scss']
})
export class FiltersCardComponent implements OnInit {

	@Input() filtering = false;
	@Input() cardTitle: string;

	constructor() {
	}

	ngOnInit(): void {
	}

}

import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-filters-card',
	templateUrl: './filters-card.component.html',
	styleUrls: ['./filters-card.component.scss']
})
export class FiltersCardComponent {

	@Input() filtering = false;
	@Input() cardTitle: string;

	constructor() {
	}

}

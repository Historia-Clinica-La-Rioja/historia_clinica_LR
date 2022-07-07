import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-location-badge',
	templateUrl: './location-badge.component.html',
	styleUrls: ['./location-badge.component.scss']
})
export class LocationBadgeComponent {
	@Input() location: LocationInfo;
	@Input() roles = [];
	@Input() hideName: boolean;

	constructor() { }

}


export class LocationInfo {
	name: string;
	address: string;
}

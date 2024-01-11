import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-violence-situation-list',
	templateUrl: './violence-situation-list.component.html',
	styleUrls: ['./violence-situation-list.component.scss']
})
export class ViolenceSituationListComponent {

	@Input() violenceSituations = [];
}

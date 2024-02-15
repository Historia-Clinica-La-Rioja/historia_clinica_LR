import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-expansion-section',
	templateUrl: './expansion-section.component.html',
	styleUrls: ['./expansion-section.component.scss']
})
export class ExpansionSectionComponent {

	@Input() title: string;
	@Input() collapsed = false;

	toggle() {
		this.collapsed = !this.collapsed;
	}

}

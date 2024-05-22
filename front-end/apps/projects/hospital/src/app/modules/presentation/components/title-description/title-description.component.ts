import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-title-description',
	templateUrl: './title-description.component.html',
	styleUrls: ['./title-description.component.scss']
})

export class TitleDescriptionComponent {

	@Input() title: string;
	@Input() text: string;
	@Input() limit: number;
}

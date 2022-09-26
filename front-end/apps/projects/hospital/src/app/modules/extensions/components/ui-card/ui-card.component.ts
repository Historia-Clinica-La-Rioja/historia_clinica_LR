import { Component, Input } from '@angular/core';
import { UIComponentDto, UILabelDto } from '@extensions/extensions-model';

@Component({
	selector: 'app-ui-card',
	templateUrl: './ui-card.component.html',
	styleUrls: ['./ui-card.component.scss']
})
export class UiCardComponent {
	@Input() title: UILabelDto;
	@Input() content: UIComponentDto[];

	constructor() { }

}

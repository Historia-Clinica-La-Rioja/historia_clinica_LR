import { Component, Input } from '@angular/core';
import { UIComponentDto, UILabelDto } from '@extensions/extensions-model';

@Component({
	selector: 'app-card',
	templateUrl: './card.component.html',
	styleUrls: ['./card.component.scss']
})
export class CardComponent {
	@Input() title: UILabelDto;
	@Input() content: UIComponentDto[];

	constructor() { }

}

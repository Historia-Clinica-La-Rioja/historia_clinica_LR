import { Component, Input } from '@angular/core';
import { UIComponentDto } from '@extensions/extensions-model';

@Component({
	selector: 'app-ui-component-list',
	templateUrl: './ui-component-list.component.html',
	styleUrls: ['./ui-component-list.component.scss']
})
export class UiComponentListComponent {
	@Input() list: UIComponentDto[];
	@Input() listOnTab: string = null;

	constructor() { }

}

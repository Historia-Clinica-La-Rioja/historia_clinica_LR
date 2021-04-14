import { Component, Input } from '@angular/core';
import { UIComponent } from '../ui-component/ui-component.component';

@Component({
	selector: 'app-page-components',
	templateUrl: './page-components.component.html',
	styleUrls: ['./page-components.component.scss']
})
export class PageComponentsComponent {
	@Input() content: UIComponent[];

	constructor() { }

}

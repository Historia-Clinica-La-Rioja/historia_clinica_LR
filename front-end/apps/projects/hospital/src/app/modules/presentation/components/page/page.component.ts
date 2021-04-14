import { Component, Input } from '@angular/core';
import { UIComponent } from '../ui-component/ui-component.component';


export class Page {
	type: string;
	content: UIComponent[];
}

@Component({
	selector: 'app-page',
	templateUrl: './page.component.html',
	styleUrls: ['./page.component.scss']
})
export class PageComponent {
	@Input() page: Page = {type: 'loading', content: []};

	constructor() { }

}

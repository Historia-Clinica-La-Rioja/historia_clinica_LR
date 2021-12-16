import { Component, Input } from '@angular/core';
import { UIPageDto } from '@extensions/extensions-model';

@Component({
	selector: 'app-page-layout',
	templateUrl: './page-layout.component.html',
	styleUrls: ['./page-layout.component.scss']
})
export class PageLayoutComponent {

	@Input() page: UIPageDto = {layout: 'loading', content: []};

	constructor() { }

}

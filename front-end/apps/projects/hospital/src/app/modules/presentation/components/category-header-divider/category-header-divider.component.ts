import { Component, Input, OnInit } from '@angular/core';
import { Title } from '../indication/indication.component';

@Component({
	selector: 'app-category-header-divider',
	templateUrl: './category-header-divider.component.html',
	styleUrls: ['./category-header-divider.component.scss']
})
export class CategoryHeaderDividerComponent implements OnInit {

	@Input() header: Title;

	constructor() { }

	ngOnInit(): void {
	}

}

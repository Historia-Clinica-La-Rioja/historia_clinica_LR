import { Component, OnInit, Input } from '@angular/core';
import { MenuItem } from '@core/core-model';

@Component({
	selector: 'app-menu',
	templateUrl: './menu.component.html',
	styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {
	@Input() menuItems: MenuItem[];

	constructor() { }

	ngOnInit(): void {
	}

}

import { Component, OnInit, Input } from '@angular/core';
import { MenuItem } from '@core/core-model';

@Component({
	selector: 'app-sidenav',
	templateUrl: './sidenav.component.html',
	styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent implements OnInit {

	@Input() menuItems: MenuItem[];

	constructor() {
	}

	ngOnInit(): void {
	}
}

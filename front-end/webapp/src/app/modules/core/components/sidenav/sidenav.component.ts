import { Component, OnInit, Input } from '@angular/core';
import { MenuItem } from '@core/core-model';
import { Router } from '@angular/router';

@Component({
	selector: 'app-sidenav',
	templateUrl: './sidenav.component.html',
	styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent implements OnInit {

	@Input() menuItems: MenuItem[];

	public activeIndex = 0;

	constructor(private router: Router) {
	}

	ngOnInit(): void {
	}

	clickMenu(menuItem: MenuItem, menuIndex: number) {
		this.activeIndex = menuIndex;
		console.log(menuItem);
		this.router.navigate([menuItem.actionURL]);
	}

}

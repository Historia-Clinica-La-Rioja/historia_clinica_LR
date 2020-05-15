import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { MenuItem } from '@core/core-model';
import { SIDEBAR_MENU } from './constants/menu';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
	menuItems$: Observable<MenuItem[]>;

	constructor() { }


	ngOnInit(): void {
		this.menuItems$ = of(SIDEBAR_MENU);
	}
}

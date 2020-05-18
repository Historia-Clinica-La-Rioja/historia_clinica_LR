import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { MenuItem } from '@core/core-model';
import { SIDEBAR_MENU } from './constants/menu';
import { PermissionsService } from '../core/services/permissions.service';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
	menuItems$: Observable<MenuItem[]>;

	constructor(
		private permissionsService: PermissionsService,
	) { }


	ngOnInit(): void {
		this.menuItems$ = this.permissionsService.filterItems$(SIDEBAR_MENU);
	}
}

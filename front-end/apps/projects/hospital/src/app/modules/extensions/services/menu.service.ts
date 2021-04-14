import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { MenuItem } from '@presentation/components/menu/menu.component';
import { of } from 'rxjs';
import { environment } from '@environments/environment';
import { SYSTEM_MENU, INSTITUTION_MENU } from '../constants/demo.mocks';
import { map } from 'rxjs/operators';


@Injectable({
	providedIn: 'root'
})
export class MenuService {

	constructor(
	) { }

	getSystemMenuItems(): Observable<MenuItem[]> {
		return environment.production? of([]) : of(SYSTEM_MENU);
	}
	getSystemMenuItem(menuId: string): Observable<MenuItem> {
		return this.getSystemMenuItems().pipe(
			map(menuItems => menuItems.find(menuItem => menuItem.id === menuId)),
		);
	}

	getInstitutionMenu(institutionId: number): Observable<MenuItem[]> {
		return environment.production? of([]) : of(INSTITUTION_MENU);
	}
	getInstitutionMenuItem(institutionId: number, menuId: string): Observable<MenuItem> {
		return this.getInstitutionMenu(institutionId).pipe(
			map(menuItems => menuItems.find(menuItem => menuItem.id === menuId)),
		);
	}
}

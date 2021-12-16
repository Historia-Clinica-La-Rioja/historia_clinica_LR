import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { MenuItem } from '@presentation/components/menu/menu.component';

import { ExtensionsService } from './extensions.service';
import { mapMenuItem, mapMenuItems } from '../mappers.utils';

@Injectable({
  providedIn: 'root'
})
export class MenuService {

  constructor(
	private extensionsService: ExtensionsService,
  ) { }

  getSystemMenuItems(): Observable<MenuItem[]> {
	return this.extensionsService.getSystemMenu().pipe(
		map(mapMenuItems),
	);
  }
  getSystemMenuItem(menuId: string): Observable<MenuItem> {
	return this.extensionsService.getSystemMenu().pipe(
		map(menuItems => menuItems.find(menuItem => menuItem.id === menuId)),
		map(mapMenuItem),
	);
  }


  getInstitutionMenu(institutionId: number): Observable<MenuItem[]> {
	return this.extensionsService.getInstitutionMenu(institutionId).pipe(
		map(mapMenuItems),
	);
  }
  getInstitutionMenuItem(institutionId: number, menuId: string): Observable<MenuItem> {
	return this.getInstitutionMenu(institutionId).pipe(
		map(menuItems => menuItems.find(menuItem => menuItem.id === menuId)),
	);
  }
}

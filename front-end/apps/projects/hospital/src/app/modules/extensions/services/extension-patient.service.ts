import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { MenuItem } from '@presentation/components/menu/menu.component';


import { mapMenuItem } from '../mappers.utils';
import { ExtensionsService } from './extensions.service';
import { UIMenuItemDto, UIPageDto } from '@extensions/extensions-model';

@Injectable({
	providedIn: 'root'
})
export class ExtensionPatientService {

	constructor(
		private extensionsService: ExtensionsService,
	) { }

	getTabs(patientId: number): Observable<{ head: MenuItem, body$: Observable<UIPageDto> }[]> {
		return this.extensionsService.getPatientMenu(patientId).pipe(
			map((menuItems: UIMenuItemDto[]) => menuItems.map(
				menuItem => ({
					head: mapMenuItem(menuItem),
					body$: this.extensionsService.getPatientPage(patientId, menuItem.id),
				})
			)),
		);
	}


}

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { MenuItem } from '@presentation/components/menu/menu.component';
import { Page } from '@presentation/components/page/page.component';

import { UIMenuItemDto } from '@api-rest/api-model';
import { ExtensionsService } from '@api-rest/services/extensions.service';

import { mapMenuItem, mapPage } from '../mappers.utils';

@Injectable({
	providedIn: 'root'
})
export class ExtensionPatientService {

	constructor(
		private extensionsService: ExtensionsService,
	) { }

	getTabs(patientId: number): Observable<{ head: MenuItem, body$: Observable<Page> }[]> {
		return this.extensionsService.getPatientMenu(patientId).pipe(
			map((menuItems: UIMenuItemDto[]) => menuItems.map(
				menuItem => ({
					head: mapMenuItem(menuItem),
					body$: this.extensionsService.getPatientPage(patientId, menuItem.id).pipe(
						map(mapPage)
					),
				})
			)),
		);
	}


}

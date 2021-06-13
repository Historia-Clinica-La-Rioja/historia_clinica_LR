import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { Page } from '@presentation/components/page/page.component';

import { ExtensionsService } from '@api-rest/services/extensions.service';
import { mapPage } from '../mappers.utils';

@Injectable({
	providedIn: 'root'
})
export class PageService {

	constructor(
		private extensionsService: ExtensionsService,
	) { }

	getSystemPage(menuId: string): Observable<Page> {
		return this.extensionsService.getSystemPage(menuId)
			.pipe(
				map(mapPage)
			);
	}

	getInstitutionPage(institutionId: number, menuId: string): Observable<Page> {
		return this.extensionsService.getInstitutionPage(institutionId, menuId)
			.pipe(
				map(mapPage)
			);
	}
}

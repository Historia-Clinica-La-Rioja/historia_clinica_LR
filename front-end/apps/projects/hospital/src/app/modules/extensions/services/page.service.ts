import { Injectable } from '@angular/core';
import { Page } from '@presentation/components/page/page.component';
import { of } from 'rxjs';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { SYSTEM_MANUALES_PAGE, INSTITUTION_MANUALES_PAGE } from '../constants/demo.mocks';

@Injectable({
	providedIn: 'root'
})
export class PageService {

	constructor(
	) { }

	getSystemPage(menuId: string): Observable<Page> {
		if (!environment.production && menuId === 'manuales') {
			return of(SYSTEM_MANUALES_PAGE);
		}
		return of(undefined);
	}

	getInstitutionPage(institutionId: number, menuId: string): Observable<Page> {
		if (!environment.production && menuId === 'informacion') {
			return of(INSTITUTION_MANUALES_PAGE);
		}
		return of(undefined);
	}
}

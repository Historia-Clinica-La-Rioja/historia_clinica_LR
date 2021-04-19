import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

import { MenuItem } from '@presentation/components/menu/menu.component';
import { Page } from '@presentation/components/page/page.component';

import { PATIENT_TABS } from '../constants/demo.mocks';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class ExtensionPatientService {

	constructor(
	) { }

	getTabs(patientId: number): Observable<{ head: MenuItem, body$: Observable<Page> }[]> {
		return environment.production? of([]) : of(PATIENT_TABS);
	}

}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MasterDataInterface } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

const PREFIX = '/emergency-care/masterdata';
@Injectable({
	providedIn: 'root'
})
export class EmergencyCareMasterDataService {

	constructor(
		private http: HttpClient,
	) { }

	getType(): Observable<MasterDataInterface<number>[]> {
		const url = `${environment.apiBase}${PREFIX}/type`;
		return this.http.get<MasterDataInterface<number>[]>(url);
	}


	getEntranceType(): Observable<MasterDataInterface<number>[]> {
		const url = `${environment.apiBase}${PREFIX}/entranceType`;
		return this.http.get<MasterDataInterface<number>[]>(url);
	}

	getDischargeType(): Observable<MasterDataInterface<number>[]> {
		const url = `${environment.apiBase}${PREFIX}/dischargeType`;
		return this.http.get<MasterDataInterface<number>[]>(url);
	}

}

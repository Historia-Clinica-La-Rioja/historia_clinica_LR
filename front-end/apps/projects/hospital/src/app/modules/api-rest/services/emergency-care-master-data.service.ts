import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MasterDataDto, MasterDataInterface } from '@api-rest/api-model';
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

	getEmergencyEpisodeSectorType(): Observable<MasterDataInterface<number>[]> {
		const url = `${environment.apiBase}${PREFIX}/emergency-episode-sector-type`;
		return this.http.get<MasterDataInterface<number>[]>(url);
	}

	getNursingDischargeType(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}${PREFIX}/dischargeType/nursing`;
		return this.http.get<MasterDataDto[]>(url);
	}

	getEmergencyCareStates(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}${PREFIX}/emergency-episode-states`;
		return this.http.get<MasterDataDto[]>(url);
	}

	getAttentionPlaceBlockReasons(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}${PREFIX}/attention-place-block-reasons`;
		return this.http.get<MasterDataDto[]>(url);
	}

}

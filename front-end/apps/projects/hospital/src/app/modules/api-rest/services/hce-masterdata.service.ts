import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MasterDataDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class HceMasterdataService {

    private readonly URL_SUFFIX = `${environment.apiBase}/hce/masterdata/`;

    constructor(
        private http: HttpClient,
    ) { }

    getMotives(): Observable<MasterDataDto[]> {
		const url = this.URL_SUFFIX + `health/error-reasons`;
		return this.http.get<MasterDataDto[]>(url);
	}

    getPersonalHistoryTypes(): Observable<MasterDataDto[]> {
        const url = this.URL_SUFFIX + `health/personal-history`;
		return this.http.get<MasterDataDto[]>(url);
    }
}

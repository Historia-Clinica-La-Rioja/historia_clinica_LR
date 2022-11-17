import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {  HolidayDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root',
})
export class HolidaysService {

	constructor(
		private readonly http: HttpClient
	) {	}

	getHolidays(startDate: string, endDate: string): Observable<HolidayDto[]> {
		const url = `${environment.apiBase}/holidays`;
		return this.http.get<HolidayDto[]>(url, {
			params: {
				startDate,
				endDate
			}
		});
	}

}

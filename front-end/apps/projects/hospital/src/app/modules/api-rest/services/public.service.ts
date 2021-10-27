import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, ReplaySubject } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { LocalStorageService } from '@core/services/local-storage.service';
import { ApplicationVersionDto, PublicInfoDto, RecaptchaPublicConfigDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';

const BASIC_URL_PREFIX = '/public';
const PUBLIC_INFO_KEY = 'public-info';
@Injectable({
	providedIn: 'root'
})
export class PublicService {
	private publicInfoEmitter = new ReplaySubject<PublicInfoDto>(1);
	private publicInfo$: Observable<PublicInfoDto>;

	constructor(
		private http: HttpClient,
		localStorageService: LocalStorageService,
	) {
		this.publicInfo$ = this.publicInfoEmitter.asObservable();

		localStorageService.getItem<PublicInfoDto>(PUBLIC_INFO_KEY).subscribe(
			publicInfoFromCache => {
				if (publicInfoFromCache && publicInfoFromCache.flavor) {
					this.publicInfoEmitter.next(publicInfoFromCache);
				}
			}
		);

		http.get<PublicInfoDto>(`${environment.apiBase}${BASIC_URL_PREFIX}/info`).pipe(
			switchMap(publicInfoFromApi => localStorageService.updateItem(PUBLIC_INFO_KEY, publicInfoFromApi)),
		).subscribe(
			publicInfoUpdated => this.publicInfoEmitter.next(publicInfoUpdated)
		);

	}

	public getInfo(): Observable<PublicInfoDto> {
		return this.publicInfo$;
	}

	public getRecaptchaPublicConfig(): Observable<RecaptchaPublicConfigDto> {
		return this.http.get<RecaptchaPublicConfigDto>(`${environment.apiBase}${BASIC_URL_PREFIX}/recaptcha`);
	}

	getApplicationCurrentVersion(): Observable<ApplicationVersionDto> {
		const url = `${environment.apiBase}${BASIC_URL_PREFIX}/version`;
		return this.http.get<ApplicationVersionDto>(url);
	}

}

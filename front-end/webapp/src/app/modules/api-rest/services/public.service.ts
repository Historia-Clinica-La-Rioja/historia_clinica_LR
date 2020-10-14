import { Injectable } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';
import { PublicInfoDto, RecaptchaPublicConfigDto } from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { switchMap } from 'rxjs/operators';
import { LocalStorageService } from '../../core/services/local-storage.service';

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
					this.publicInfoEmitter.next(publicInfoFromCache)
				}
			}
		);

		http.get<PublicInfoDto>(`${environment.apiBase}` + BASIC_URL_PREFIX + '/info').pipe(
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

}

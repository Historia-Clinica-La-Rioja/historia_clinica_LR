import { Injectable } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';
import { PublicInfoDto } from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { switchMap } from 'rxjs/operators';
import { LocalStorageService } from '../../core/services/local-storage.service';

const PUBLIC_INFO_KEY = 'public-info';
@Injectable({
	providedIn: 'root'
})
export class PublicService {
	private publicInfoEmitter = new ReplaySubject<PublicInfoDto>(1);
	private publicInfo$: Observable<PublicInfoDto>;

	constructor(
		http: HttpClient,
		private localStorageService: LocalStorageService,
	) {
		this.publicInfo$ = this.publicInfoEmitter.asObservable();

		this.localStorageService.getItem<PublicInfoDto>(PUBLIC_INFO_KEY).subscribe(
			publicInfoFromCache => {
				if (publicInfoFromCache && publicInfoFromCache.flavor) {
					this.publicInfoEmitter.next(publicInfoFromCache)
				}
			}
		);

		http.get<PublicInfoDto>(`${environment.apiBase}/public/info`).pipe(
			switchMap(publicInfoFromApi => localStorageService.updateItem(PUBLIC_INFO_KEY, publicInfoFromApi)),
		).subscribe(
			publicInfoUpdated => this.publicInfoEmitter.next(publicInfoUpdated)
		);

	}

	public getInfo(): Observable<PublicInfoDto> {
		return this.publicInfo$;
	}

}

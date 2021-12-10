import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { EXTENSION_URL } from './extensions.service';
import { UIPageDto } from '@extensions/extensions-model';
import { HttpClient } from '@angular/common/http';

@Injectable({
	providedIn: 'root'
})
export class FeaturesService {

	constructor(
		private http: HttpClient,
	) { }

	status(): Observable<UIPageDto> {
		const featureStatusUrl = `${EXTENSION_URL}/features`;
		return this.http.get<UIPageDto>(featureStatusUrl);
	}

}

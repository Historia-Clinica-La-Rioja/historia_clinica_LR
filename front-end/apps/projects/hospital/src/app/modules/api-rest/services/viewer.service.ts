import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ViewerUrlDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ViewerService {

	private readonly BASE_URL: string;

	constructor(
		private readonly contextService: ContextService,
		private http: HttpClient,
	) {
		this.BASE_URL = `${environment.apiBase}/imagenetwork/viewer`;
	}

	getUrl(): Observable<ViewerUrlDto> {
		const url = `${this.BASE_URL}/url`;
		return this.http.get<ViewerUrlDto>(url);
	}
}

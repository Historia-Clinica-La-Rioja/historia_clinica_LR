import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TokenDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class StudyPermissionService {

	private readonly BASE_URL: string;

	constructor(
		private readonly contextService: ContextService,
		private http: HttpClient,
	) {
		this.BASE_URL = `${environment.apiBase}/institutions/${this.contextService.institutionId}/imagenetwork`
	}

	getPermissionsJWT(studyInstanceUID: string): Observable<TokenDto> {
		const url = `${this.BASE_URL}/${studyInstanceUID}/permission/generate/jwt`;
		return this.http.get<TokenDto>(url);
	}
}

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GenerateApiKeyDto, GeneratedApiKeyDto, UserApiKeyDto } from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
@Injectable({
  providedIn: 'root'
})
export class ApiKeyService {

	constructor(
		private readonly http: HttpClient,
	) { }

	public list(): Observable<UserApiKeyDto[]> {
		return this.http.get<UserApiKeyDto[]>(`${environment.apiBase}/auth/api-keys`);
	}

	public generateApiKey(newApiKey: GenerateApiKeyDto): Observable<GeneratedApiKeyDto> {
		return this.http.post<GeneratedApiKeyDto>(`${environment.apiBase}/auth/api-keys`, newApiKey);
	}

	public deleteApiKey(apiKeyName: string): Observable<void> {
		return this.http.delete<void>(`${environment.apiBase}/auth/api-keys/${apiKeyName}`);
	}

}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { InstitutionalGroupDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class InstitutionalGroupsService {

	private readonly BASE_URL = `${environment.apiBase}/institutional-group`;


	constructor(
		private http: HttpClient,
	) { }

	getCurrentUserGroups(): Observable<InstitutionalGroupDto[]> {
		return this.http.get<InstitutionalGroupDto[]>(`${this.BASE_URL}/current-user`);

	}

}

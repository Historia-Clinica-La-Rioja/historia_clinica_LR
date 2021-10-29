import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";
import { UserDataDto } from "@api-rest/api-model";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private contextService: ContextService) { }

	getUserData(personId: number): Observable<UserDataDto> {
		const url = `${environment.apiBase}/users/institution/${this.contextService.institutionId}/person/${personId}`;
		return this.http.get<UserDataDto>(url);
	}

	addUser(personId: number): Observable<number> {
		const url = `${environment.apiBase}/users/institution/${this.contextService.institutionId}/person/${personId}`;
		return this.http.post<number>(url, {});
	}

	enableUser(userId: number, enable: boolean): Observable<boolean> {
		const url = `${environment.apiBase}/users/institution/${this.contextService.institutionId}/user/${userId}`;
		return this.http.put<boolean>(url, enable);
	}

}


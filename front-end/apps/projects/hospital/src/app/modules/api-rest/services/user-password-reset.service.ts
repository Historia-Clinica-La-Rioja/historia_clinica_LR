import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ContextService} from "@core/services/context.service";
import {Observable} from "rxjs";
import {environment} from "@environments/environment";

@Injectable({
	providedIn: 'root'
})
export class UserPasswordResetService {

	constructor(private http: HttpClient, private contextService: ContextService) {
	}

	createTokenPasswordReset(userId: number): Observable<string> {
		const url = `${environment.apiBase}/users/institution/${this.contextService.institutionId}/user/${userId}/password-reset`;
		return this.http.post<string>(url, {}, {responseType: 'text' as 'json'});
	}
}

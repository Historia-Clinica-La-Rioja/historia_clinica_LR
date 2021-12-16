import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { PersonDataDto } from "@api-rest/api-model";
import { environment } from "@environments/environment";

@Injectable({
  providedIn: 'root'
})
export class PublicUserService {

  constructor(private http: HttpClient) { }

	getUserPersonData(token: string): Observable<PersonDataDto> {
		const url = `${environment.apiBase}/auth/public-user`;
		return this.http.get<PersonDataDto>(url,{params:{token: token}});
	}
}

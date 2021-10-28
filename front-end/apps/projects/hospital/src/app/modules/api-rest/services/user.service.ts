import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";
import { UserDataDto } from "@api-rest/api-model";
import { Observable, of } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private contextService: ContextService) { }

	getUserData(personId: number): Observable<UserDataDto> {
		const userDataDto = {id: 111, username: "user@example.com", enable: true, lastLogin: new Date(2021, 10, 26, 10, 3) }
		return of(userDataDto);
	}

	addUser(personId: number): Observable<number> {
  		return of(100);
	}

}


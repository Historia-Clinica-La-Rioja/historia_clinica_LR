import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ShockroomDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ShockroomService {

	constructor(private http: HttpClient,
				private contextService: ContextService,) { }

	getShockrooms(): Observable<ShockroomDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/shockroom`;
		return this.http.get<ShockroomDto[]>(url);
	}
}

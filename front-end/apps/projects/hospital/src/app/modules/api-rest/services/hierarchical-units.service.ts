import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HierarchicalUnitDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class HierarchicalUnitsService {

	private readonly URL_BASE = `${environment.apiBase}/institutions/${this.contextService.institutionId}/hierarchicalunit`;

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService
	) { }

	getByInstitution(): Observable<HierarchicalUnitDto[]> {
		return this.http.get<HierarchicalUnitDto[]>(this.URL_BASE);
	}

}

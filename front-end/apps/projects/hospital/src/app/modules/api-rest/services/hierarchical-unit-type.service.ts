import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";
import { Observable } from "rxjs";
import { HierarchicalUnitTypeDto } from "@api-rest/api-model";
import { environment } from "@environments/environment";

@Injectable({
  providedIn: 'root'
})
export class HierarchicalUnitTypeService {

	private readonly URL_BASE = `${environment.apiBase}/institutions`;

	constructor(
	  private http: HttpClient,
	  private readonly contextService: ContextService
  ) { }

	getByInstitution(): Observable<HierarchicalUnitTypeDto[]> {
		const url = `${this.URL_BASE}/${this.contextService.institutionId}/hierarchical-unit-type`;
		return this.http.get<HierarchicalUnitTypeDto[]>(url);
	}
}

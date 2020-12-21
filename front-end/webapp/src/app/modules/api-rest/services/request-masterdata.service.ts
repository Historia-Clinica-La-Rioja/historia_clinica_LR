import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { ContextService } from '@core/services/context.service';
import { RequestCategoryDto } from '@api-rest/api-model';

@Injectable({
  providedIn: 'root'
})
export class RequestMasterDataService {

	constructor(
		private http: HttpClient
	  ) { }
	

	categories(): Observable<any[]> {
		const url = `${environment.apiBase}/requests/masterdata/categories`;
		return this.http.get<any[]>(url);
	}
	
}

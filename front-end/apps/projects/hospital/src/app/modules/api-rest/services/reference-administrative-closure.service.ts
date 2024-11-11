import { Injectable } from '@angular/core';
import { ReferenceAdministrativeClosureDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class ReferenceAdministrativeClosureService {

    private readonly BASE_URL: string;

    constructor(
        private readonly http: HttpClient,
		private readonly contextService: ContextService,
    ) {
        this.BASE_URL = `${environment.apiBase}/institutions`;
    }

    administrativeReferenceCloure(closureData: ReferenceAdministrativeClosureDto): Observable<boolean> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/administrative-closure`;
		return this.http.post<boolean>(url, closureData);
	}
}

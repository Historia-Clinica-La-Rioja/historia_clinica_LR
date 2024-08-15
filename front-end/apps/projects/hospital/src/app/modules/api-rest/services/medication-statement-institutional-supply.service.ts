import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SaveMedicationStatementInstitutionalSupplyDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  	providedIn: 'root'
})
export class MedicationStatementInstitutionalSupplyService {

	private BASE_URL = `${environment.apiBase}/institution/${this.contextService.institutionId}/medication-statement-institutional-supply`;

  	constructor(private http: HttpClient,
				private readonly contextService: ContextService) {}

	save(saveMedicationStatementInstitutionalSupplyDto: SaveMedicationStatementInstitutionalSupplyDto): Observable<number> {
		const url = `${this.BASE_URL}/save`;
		return this.http.post<number>(url, saveMedicationStatementInstitutionalSupplyDto);
	}
}

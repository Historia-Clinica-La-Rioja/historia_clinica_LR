import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MedicineGroupAuditRequiredDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class MedicineGroupService {

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getMedicineGroupAuditRequired(medicineId: string, problemId: string): Observable<MedicineGroupAuditRequiredDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicine-group/medicine/${medicineId}/problem/${problemId}`;
		return this.http.get<MedicineGroupAuditRequiredDto[]>(url);
	}

}

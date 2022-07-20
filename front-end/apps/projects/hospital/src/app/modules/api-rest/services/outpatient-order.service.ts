import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";
import { PrescriptionDto } from "@api-rest/api-model";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";

@Injectable({
  providedIn: 'root'
})
export class OutpatientOrderService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) {
	}

	create(patientId: number, prescriptionDto: PrescriptionDto): Observable<number[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/outpatient/service-request`;
		return this.http.post<number[]>(url, prescriptionDto);
	}
}

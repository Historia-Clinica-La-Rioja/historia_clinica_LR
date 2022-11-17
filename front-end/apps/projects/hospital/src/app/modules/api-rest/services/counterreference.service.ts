import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CounterReferenceDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class CounterreferenceService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	createCounterReference(patientId: any, counterreference: CounterReferenceDto): Observable<any> {
		const body = {
			referenceId: counterreference.referenceId,
			allergies: counterreference.allergies,
			clinicalSpecialtyId: counterreference.clinicalSpecialtyId,
			counterReferenceNote: counterreference.counterReferenceNote,
			medications: counterreference.medications,
			procedures: counterreference.procedures,
			fileIds: counterreference.fileIds,
			closureTypeId: counterreference.closureTypeId
		};
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/counterreference`;
		return this.http.post(url, body);
	}
}


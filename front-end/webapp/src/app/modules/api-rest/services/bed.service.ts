import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { BedDto, PatientBedRelocationDto } from "@api-rest/api-model";
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class BedService {

	constructor(private readonly http: HttpClient, private readonly contextService: ContextService) {
	}

	getAllBedsByCategory(clinicalSpecialtyId): Observable<BedDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/bed/clinicalspecialty/${clinicalSpecialtyId}`;
		 return this.http.get<BedDto[]>(url);
	}

	getLastPatientBedRelocation(internmentEpisodeId): Observable<PatientBedRelocationDto> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/internment/${internmentEpisodeId}/bed/relocation/last`;
		return this.http.get<PatientBedRelocationDto>(url);
	}

	relocatePatientBed(patientBedRelocationDto : PatientBedRelocationDto): Observable<PatientBedRelocationDto> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/bed/relocation`;
		 return this.http.post<PatientBedRelocationDto>(url,patientBedRelocationDto);
	}

}

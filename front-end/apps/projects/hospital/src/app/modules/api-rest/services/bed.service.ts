import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { BedDto, PatientBedRelocationDto, BedSummaryDto, BedInfoDto } from '@api-rest/api-model';
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

	getBedsSummary(sectorsType?: number[]): Observable<BedSummaryDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/bed/summary-list`;
		let params = new HttpParams();
		params = params.append('sectorsType', sectorsType.join(', '));
		return this.http.get<BedSummaryDto[]>(url, {params});
	}

	getBedInfo(bedId): Observable<BedInfoDto> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/bed/${bedId}/info`;
		return this.http.get<BedInfoDto>(url);
	}

	getLastPatientBedRelocation(internmentEpisodeId): Observable<PatientBedRelocationDto> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/internment/${internmentEpisodeId}/bed/relocation/last`;
		return this.http.get<PatientBedRelocationDto>(url);
	}

	relocatePatientBed(patientBedRelocationDto: PatientBedRelocationDto): Observable<PatientBedRelocationDto> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/bed/relocation`;
		return this.http.post<PatientBedRelocationDto>(url, patientBedRelocationDto);
	}

	updateBedNurse(bedId: number, userId?: number): Observable<void> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/bed/${bedId}/update-bed-nurse`;
		return this.http.put<void>(url, userId);
	}

}

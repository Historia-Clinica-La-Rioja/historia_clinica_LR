import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import {
	ECAdministrativeDto,
	ECPediatricDto,
	ECAdultGynecologicalDto,
	EmergencyCareListDto
} from '@api-rest/api-model';
import { environment } from "@environments/environment";
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";


const BASIC_URL_PREFIX = '/institution';
const BASIC_URL_SUFIX = '/emergency-care';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareEpisodeService {

	constructor(private http: HttpClient,
		private contextService: ContextService) {
	}

	getAll(): Observable<EmergencyCareListDto[]> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
		BASIC_URL_SUFIX}/episodes`;
		return this.http.get<EmergencyCareListDto[]>(url);
	}

	createAdministrative(newEpisode: ECAdministrativeDto): Observable<number> {
		let url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/episodes`;
		return this.http.post<number>(url, newEpisode);
	}

	getAdministrative(): Observable<ResponseEmergencyCareDto> {
		return of({
			id: 1,
			patientId: null,
			emergencyCareType: {
				id: 1,
				description: 'Guardia adultos'
			},
			creationDate: {
				date: { day: 15, month: 12, year: 2020 },
				time: { hours: 10, minutes: 30 }
			},
			reasons: [
				{
					id: 1,
					description: 'motivo de consulta'
				}
			],
			entrance: {
				type: {
					id: 1,
					description: 'Caminando'
				},
				ambulanceCompanyId: '121212'
			},
			policeIntervention: {
				id: 1,
				callDate: {
					date: { day: 1, month: 1, year: 2020 },
					time: { hours: 9, minutes: 30 }
				},
				plateNumber: 'ABC 111',
				firstName: 'Ricardo',
				lastName: 'Gutierrez'
			}
		});
		/* return of({
			id: 1,
			emergencyCareType: null,
			creationDate: {
				date: { day: 15, month: 12, year: 2020 },
				time: { hours: 10, minutes: 30 }
			},
			reasons: null,
			entrance: null,
			policeIntervention: null
		}); */
	}

	createAdult(newEpisode: ECAdultGynecologicalDto): Observable<number> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/episodes/adult-gynecological`;
		return this.http.post<number>(url, newEpisode);
	}

	createPediatric(newEpisode: ECPediatricDto): Observable<number> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/episodes/pediatric`;
		return this.http.post<number>(url, newEpisode);
	}

	setPatient(episodeId: number, patientId: number): Observable<boolean> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/episodes/${episodeId}/administrative/updatePatient`;
		return this.http.put<boolean>(url, patientId);
	}

}

export interface ResponseEmergencyCareDto {
	id: number;
	patientId: number;
	emergencyCareType: {
		id: number;
		description: string;
	},
	creationDate: {
		date: { day: number, month: number, year: number };
		time: { hours: number, minutes: number };
	},
	reasons: [
		{
			id: number;
			description: string;
		}
	],
	entrance: {
		type: {
			id: number;
			description: string;
		},
		ambulanceCompanyId: string;
	},
	policeIntervention: {
		id: number;
		callDate: {
			date: { day: number, month: number, year: number };
			time: { hours: number, minutes: number };
		},
		plateNumber: string;
		firstName: string;
		lastName: string;
	}
}

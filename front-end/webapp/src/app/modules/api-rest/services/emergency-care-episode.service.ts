import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { DateDto, DateTimeDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareEpisodeService {

	constructor() {
	}

	getAll(): Observable<EmergencyCareEpisodeDto[]> {
		return of([
			{
				id: 66,
				creationDate: {
					date: { day: 1, month: 1, year: 2020 },
					time: { hours: 10, minutes: 30 }
				},
				patient: {
					id: 9,
					person: {
						firstName: 'Silvia',
						lastName: 'Martinez',
					},
					typeId: 1
				},
				triage: {
					id: 1,
					description: 'Nivel 1',
					color: '#D34444',
				},
				type: {
					id: 1,
					description: 'Guardia adulto'
				},
				state: {
					id: 1,
					description: 'En atención'
				},
				doctorsOffice: {
					id: 1,
					description: 'Enfermeria',
				},
			},
			{
				id: 67,
				creationDate: {
					date: { day: 1, month: 3, year: 2020 },
					time: { hours: 15, minutes: 10 }
				},
				patient: {
					id: 10,
					typeId: 3
				},
				triage: {
					id: 5,
					description: 'Nivel 5',
					color: '#2687C5',
				},
				type: {
					id: 1,
					description: 'Guardia adulto'
				},
				state: {
					id: 1,
					description: 'En atención'
				},
				doctorsOffice: {
					id: 1,
					description: 'Enfermeria',
				},
			},
			{
				id: 68,
				creationDate: {
					date: { day: 1, month: 3, year: 2020 },
					time: { hours: 12, minutes: 29 }
				},
				triage: {
					id: 5,
					description: 'Nivel 5',
					color: '#2687C5',
				},
				type: {
					id: 1,
					description: 'Guardia adulto'
				},
				state: {
					id: 2,
					description: 'En espera'
				},
				doctorsOffice: {
					id: 1,
					description: 'Enfermeria',
				},
			},
			{
				id: 69,
				creationDate: {
					date: { day: 1, month: 3, year: 2020 },
					time: { hours: 12, minutes: 29 }
				},
				triage: {
					id: 1,
					description: 'Nivel 1',
					color: '#D34444',
				},
				type: {
					id: 1,
					description: 'Guardia adulto'
				},
				state: {
					id: 2,
					description: 'En espera'
				},
				doctorsOffice: {
					id: 1,
					description: 'Enfermeria',
				},
			},
			{
				id: 70,
				creationDate: {
					date: { day: 1, month: 3, year: 2020 },
					time: { hours: 12, minutes: 29 }
				},
				triage: {
					id: 2,
					description: 'Nivel 2',
					color: '#FF5C00',
				},
				type: {
					id: 1,
					description: 'Guardia adulto'
				},
				state: {
					id: 2,
					description: 'En espera'
				},
				doctorsOffice: {
					id: 1,
					description: 'Enfermeria',
				},
			},
			{
				id: 71,
				creationDate: {
					date: { day: 1, month: 3, year: 2020 },
					time: { hours: 12, minutes: 29 }
				},
				triage: {
					id: 3,
					description: 'Nivel 3',
					color: '#EDBE00',
				},
				type: {
					id: 1,
					description: 'Guardia adulto'
				},
				state: {
					id: 2,
					description: 'En espera'
				},
				doctorsOffice: {
					id: 1,
					description: 'Enfermeria',
				},
			},
			{
				id: 72,
				creationDate: {
					date: { day: 1, month: 3, year: 2020 },
					time: { hours: 12, minutes: 29 }
				},
				triage: {
					id: 4,
					description: 'Nivel 4',
					color: '#009B68',
				},
				type: {
					id: 1,
					description: 'Guardia adulto'
				},
				state: {
					id: 2,
					description: 'En espera'
				},
				doctorsOffice: {
					id: 1,
					description: 'Enfermeria',
				},
			}
			]
		);
	}
}

export interface EmergencyCareEpisodeDto {
	id: number;
	creationDate: DateTimeDto;
	patient?: {
		id?: number,
		person?: {
			firstName?: string,
			lastName?: string,
		},
		typeId: number;
	};
	triage: {
		id: number,
		description: string,
		color: string,
	};
	type: {
		id: number,
		description: string
	};
	state: {
		id: number,
		description: string
	};
	doctorsOffice: {
		id: number,
		description: string,
	};
}

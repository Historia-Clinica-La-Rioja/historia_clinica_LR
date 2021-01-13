import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { TriageAdministrativeDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable, of } from 'rxjs';

const URL_PREFIX = '/emergency-care/episodes';
const URL_SUFIX = '/triage';

@Injectable({
	providedIn: 'root'
})
export class TriageService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService
	) { }

	createAdministrative(episodeId: number, triage: TriageAdministrativeDto): Observable<number> {
		let url = `${environment.apiBase}/institution/${this.contextService.institutionId +
		URL_PREFIX}/${episodeId}${URL_SUFIX}`;
		return this.http.post<number>(url, triage);
	}

	getAll(episodeId: number): Observable<any[]> {
		return of(MOCK_PEDIATRIC);
		return of ( [
			{
				id: 1,
				creationDate: {
					date: { day: 11, month: 1, year: 2021 },
					time: { hours: 10, minutes: 30 }
				},
				category: {
					id: 1,
					name: 'Nivel I',
					description: 'Resucitación',
					color: {
						name: 'Rojo',
						code: '#FF0000'
					}
				},
				professional: {
					id: 1,
					firstName: 'Lucas',
					lastName: 'Bergottini'
				},
				doctorsOffice: {
					id: 1,
					description: 'Sala N'
				}
			},
			{
				id: 2,
				creationDate: {
					date: { day: 11, month: 1, year: 2021 },
					time: { hours: 9, minutes: 30 }
				},
				category: {
					id: 1,
					name: 'Nivel I',
					description: 'Resucitación',
					color: {
						name: 'Rojo',
						code: '#FF0000'
					}
				},
				professional: {
					id: 1,
					firstName: 'Julio',
					lastName: 'Rivera'
				},
				doctorsOffice: {
					id: 1,
					description: 'Sala N'
				},
				vitalSigns: {
					bloodOxygenSaturation: {
						value: 9,
						effectiveTime: {
							date: {day: 21, month: 11, year: 2020},
							time: {hours: 12, minutes: 0, seconds: 0}
						}
					},
					diastolicBloodPressure: {
						value: 12,
						effectiveTime: {
							date: {day: 21, month: 11, year: 2020},
							time: {hours: 12, minutes: 0, seconds: 0}
						}
					},
					heartRate: {
						value: 18,
						effectiveTime: {
							date: {day: 21, month: 11, year: 2020},
							time: {hours: 12, minutes: 0, seconds: 0}
						}
					},
					respiratoryRate: {
						value: 3,
						effectiveTime: {
							date: {day: 21, month: 11, year: 2020},
							time: {hours: 12, minutes: 0, seconds: 0}
						}
					},
					systolicBloodPressure: {
						value: 1,
						effectiveTime: {
							date: {day: 21, month: 11, year: 2020},
							time: {hours: 12, minutes: 0, seconds: 0}
						}
					},
					temperature: {
						value: 10,
						effectiveTime: {
							date: {day: 21, month: 11, year: 2020},
							time: {hours: 12, minutes: 0, seconds: 0}
						}
					}
				},
				notes: 'El paciente muestra signos de mejora'
			}
		]);
	}

}

const MOCK_PEDIATRIC =
	[
		{
			id: 1,
			creationDate: {
				date: {day: 11, month: 1, year: 2021},
				time: {hours: 10, minutes: 30}
			},
			category: {
				id: 1,
				name: 'Nivel I',
				description: 'Resucitación',
				color: {
					name: 'Rojo',
					code: '#FF0000'
				}
			},
			professional: {
				id: 1,
				firstName: 'Lucas',
				lastName: 'Bergottini'
			},
			doctorsOffice: {
				id: 1,
				description: 'Sala N'
			},
			appearance: {
				bodyTemperature: {
					id: 1,
					description: 'Fiebre'
				},
				muscleHypertonia: {
					id: 1,
					description: 'Hipertonía'
				},
				cryingExcessive: undefined
			},
			breathing: {
				respiratoryRetraction: {
					id: 1,
					description: 'Intercostal',
				},
				stridor: undefined,
				respiratoryRate: {
					value: 10,
					effectiveTime: {   // DateTimeDto
						date: { day: 21, month: 11, year: 2020},
						time: { hours: 12, minutes: 0, seconds: 12},
					}
				},
				bloodOxygenSaturation: {
					value: 10,
					effectiveTime: {   // DateTimeDto
						date: { day: 21, month: 11, year: 2020},
						time: { hours: 12, minutes: 0, seconds: 12},
					}
				}
			},
			circulation: {
				perfusion: {
					id: 1,
					description: 'Normal'
				},
				heartRate: {
					value: 10,
					effectiveTime: {   // DateTimeDto
						date: { day: 21, month: 11, year: 2020},
						time: { hours: 12, minutes: 0, seconds: 12},
					}
				}
			},
			notes: 'El paciente se encuentra tranquilo'
		},
		{
			id: 2,
			creationDate: {
				date: {day: 11, month: 1, year: 2021},
				time: {hours: 9, minutes: 30}
			},
			category: {
				id: 1,
				name: 'Nivel I',
				description: 'Resucitación',
				color: {
					name: 'Rojo',
					code: '#FF0000'
				}
			},
			professional: {
				id: 1,
				firstName: 'Julio',
				lastName: 'Rivera'
			},
			doctorsOffice: {
				id: 1,
				description: 'Sala N'
			},
			vitalSigns: {
				bloodOxygenSaturation: {
					value: 9,
					effectiveTime: {
						date: {day: 21, month: 11, year: 2020},
						time: {hours: 12, minutes: 0, seconds: 0}
					}
				},
				diastolicBloodPressure: {
					value: 12,
					effectiveTime: {
						date: {day: 21, month: 11, year: 2020},
						time: {hours: 12, minutes: 0, seconds: 0}
					}
				},
				heartRate: {
					value: 18,
					effectiveTime: {
						date: {day: 21, month: 11, year: 2020},
						time: {hours: 12, minutes: 0, seconds: 0}
					}
				},
				respiratoryRate: {
					value: 3,
					effectiveTime: {
						date: {day: 21, month: 11, year: 2020},
						time: {hours: 12, minutes: 0, seconds: 0}
					}
				},
				systolicBloodPressure: {
					value: 1,
					effectiveTime: {
						date: {day: 21, month: 11, year: 2020},
						time: {hours: 12, minutes: 0, seconds: 0}
					}
				},
				temperature: {
					value: 10,
					effectiveTime: {
						date: {day: 21, month: 11, year: 2020},
						time: {hours: 12, minutes: 0, seconds: 0}
					}
				}
			},
			notes: 'El paciente muestra signos de mejora'
		}
	];

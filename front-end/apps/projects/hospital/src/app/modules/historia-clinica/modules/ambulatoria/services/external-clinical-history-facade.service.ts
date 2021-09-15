import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

export interface ExternalClinicalHistory {
	institution?: string,
	professionalName?: string,
	professionalSpecialty?: string,
	consultationDate: string,
	notes: string
}

@Injectable({
	providedIn: 'root'
})
export class ExternalClinicalHistoryFacadeService {

	private externalClinicalHistoryList: ExternalClinicalHistory[];

	constructor() {
		this.externalClinicalHistoryList = [
			{
				institution: "Hospital Ramon Santamarina",
				professionalName: "Adrian Zapata",
				professionalSpecialty: "Enfermería",
				consultationDate: "2021-01-30",
				notes: "Descripcion que intenta rellenar un poco y deberian ser las notas del profesional"
			},
			{
				institution: "Hospital Ramon Santamarina",
				professionalSpecialty: "Pediatría",
				consultationDate: "2021-01-20",
				notes: "Descripcion que intenta rellenar un poco y deberian ser las notas del profesional"
			},
			{
				professionalName: "Carlos Aguirre",
				consultationDate: "2021-01-01",
				notes: "Descripcion que intenta rellenar un poco y deberian ser las notas del profesional"
			},
			{
				consultationDate: "2021-01-10",
				notes: "Descripcion que intenta rellenar un poco y deberian ser las notas del profesional"
			}
		];
	}

	public getFilteredHistories(): Observable<ExternalClinicalHistory[]> {
		return of(this.externalClinicalHistoryList);
	}

	public getSpecialties(): string[] {
		return this.externalClinicalHistoryList.reduce(
			function (filtered: string[], history: ExternalClinicalHistory) {
				if (history.professionalSpecialty && !filtered.find(specialty => specialty == history.professionalSpecialty))
					filtered.push(history.professionalSpecialty);
				return filtered;
			}, []);
	}
	public getProfessionals(): string[] {
		return this.externalClinicalHistoryList.reduce(
			function (filtered: string[], history: ExternalClinicalHistory) {
				if (history.professionalName && !filtered.find(professional => professional == history.professionalName))
					filtered.push(history.professionalName);
				return filtered;
			}, []);
	}
	public getInstitutions(): string[] {
		return this.externalClinicalHistoryList.reduce(
			function (filtered: string[], history: ExternalClinicalHistory) {
				if (history.institution && !filtered.find(institution => institution == history.institution))
					filtered.push(history.institution);
				return filtered;
			}, []);
	}

}

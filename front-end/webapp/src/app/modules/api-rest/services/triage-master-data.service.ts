import { Injectable } from '@angular/core';
import { environment } from "@environments/environment";
import { Observable, of } from "rxjs";

const TRIAGE_CATEGORIES = [
	{
		"id": 1,
		"description": "Nivel I",
		"colour": {
			"name": "red",
			"code": "some"
		}
	},
	{
		"id": 2,
		"description": "Nivel II",
		"colour": {
			"name": "orange",
			"code": "some"
		}
	},
	{
		"id": 3,
		"description": "Nivel III",
		"colour": {
			"name": "yellow",
			"code": "some"
		}
	},
	{
		"id": 4,
		"description": "Nivel IV",
		"colour": {
			"name": "green",
			"code": "some"
		}
	},
	{
		"id": 5,
		"description": "Nivel V",
		"colour": {
			"name": "blue",
			"code": "some"
		}
	}
];

const BASIC_URL_PREFIX = '/emergency-care/triage/masterdata';

@Injectable({
	providedIn: 'root'
})
export class TriageMasterDataService {

	constructor() {
	}

	getCategories(): Observable<TriageCategoryDto[]> {
		let url = `${environment.apiBase + BASIC_URL_PREFIX}/category`;
		return of(TRIAGE_CATEGORIES);
	}
}

export interface TriageCategoryDto {
	id: number,
	description: string,
	colour: {
		name: string,
		code: string
	}
}

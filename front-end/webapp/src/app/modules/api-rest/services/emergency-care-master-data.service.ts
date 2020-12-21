import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MasterDataInterface } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { Observable, of } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareMasterDataService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService
	) { }

	getType(): Observable<MasterDataInterface<number>[]> {
		return of([{
			id: 1,
			description: 'Adulto'
		},
		{
			id: 2,
			description: 'Pediátrico'
		},
		{
			id: 3,
			description: 'Ginecología y obstetricia'
		}]);
	}


	getEntranceType():  Observable<MasterDataInterface<number>[]>{
		return of([

			{
				id: 1,
				description: 'Caminando'
			},
			{
				id: 2,
				description: 'En silla de ruedas'
			},
			{
				id: 3,
				description: 'Ambulancia sin médico'
			},
			{
				id: 4,
				description: 'Ambulancia con médico'
			}
		]);
	}

}

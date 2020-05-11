import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { InstitutionService } from '@api-rest/services/institution.service';
import { InstitutionDto } from '@api-rest/api-model';
import { map } from 'rxjs/operators';

@Injectable({
	providedIn: 'root'
})
export class ContextService {
	private _institutionId: number;

	constructor(
		private institutionService: InstitutionService,
	) {
		//TODO eliminar cuando dispongamos del listado de instituciones y sus asociaciones x rol
		this._institutionId = 10;
	}

	get institutionId(): number {
		return this._institutionId;
	}

	public setInstitutionId(id: number): void {
		this._institutionId = id;
	}

	get institution$(): Observable<InstitutionDto> {
		return this.institutionService.getInstitutions([this._institutionId]).pipe(
			map(list => list && list.length ? list[0] : undefined),
		);
	}
}

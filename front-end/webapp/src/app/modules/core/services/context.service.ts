import { Injectable } from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class ContextService {
	private _institutionId: number;

	constructor() { }

	/**
	 * Retorna el id de la institución del contexto
	 */
	get institutionId(): number {
		return this._institutionId;
	}

	/**
	 * Permite definir la institución del contexto
	 * Usar con cuidado ya que al no usar Observables no se puede avisar que la institución cambió.
	 * @param id
	 */
	public setInstitutionId(id: number): void {
		this._institutionId = id;
	}

}

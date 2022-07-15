import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ContextService {
	private _institutionId: number;
	private institutionSource = new BehaviorSubject<number>(undefined);
	public institutionId$: Observable<number> = this.institutionSource.asObservable();


	constructor() { }

	/**
	 * Retorna el id de la institución del contexto
	 */
	get institutionId(): number {
		return this._institutionId;
	}

	/**
	 * Permite definir la institución del contexto
	 * Subscribiendose a institutionId$ se puede saber cuando cambia su valor.
	 */
	public setInstitutionId(id: number): void {
		this._institutionId = id;
		localStorage.setItem("INSTITUTION_ID", id.toString());
		this.institutionSource.next(id);
	}

}

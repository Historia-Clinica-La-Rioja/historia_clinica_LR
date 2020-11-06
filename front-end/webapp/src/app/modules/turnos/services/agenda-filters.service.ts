import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class AgendaFiltersService {

	private agendaFiltersSource = new BehaviorSubject<AgendaFilters>(undefined);

	constructor() {
	}

	getFilters(): Observable<AgendaFilters> {
		return this.agendaFiltersSource.asObservable();
	}

	setFilters(idProfesional: number, idEspecialidad?: number): void {
		this.agendaFiltersSource.next({ idProfesional, idEspecialidad });
	}
}

export interface AgendaFilters {
	idEspecialidad?: number;
	idProfesional: number;
}

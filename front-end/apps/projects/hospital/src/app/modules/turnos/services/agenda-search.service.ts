import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { CompleteDiaryDto, DiaryListDto } from '@api-rest/api-model';
import { DiariesService } from '@api-rest/services/diaries.service';

@Injectable({
	providedIn: 'root'
})
export class AgendaSearchService {

	private agendaFiltersSource = new BehaviorSubject<AgendaFilters>(undefined);
	private agendasSource = new BehaviorSubject<AgendaOptionsData>(undefined);
	private agendaSelected: CompleteDiaryDto;

	constructor(
		private readonly diariesService: DiariesService,
	) {
	}

	getFilters$(): Observable<AgendaFilters> {
		return this.agendaFiltersSource.asObservable();
	}

	 getAgendas$(): Observable<AgendaOptionsData> {
		return this.agendasSource.asObservable();
	}

	search(idProfesional: number): void {
		if (idProfesional) {
			this.updateDiaries(idProfesional);
		} else {
			this.clearSearch();
		}
	}

	setAgendaSelected(agendaSelected: CompleteDiaryDto): void {
		if (!agendaSelected) {
			this.updateSelectAgenda();
		}
		if (this.newAgendaSelected(agendaSelected)) {
			this.updateProfesionalSelected(agendaSelected);
		}
		this.agendaSelected = agendaSelected;
	}

	clearAll(): void {
		this.agendaFiltersSource.next(undefined);
		this.agendasSource.next(undefined);
		this.agendaSelected = undefined;
	}

	private updateDiaries(idProfesional: number): void {
		this.diariesService.getDiaries(idProfesional).subscribe(agendas => {
			this.agendasSource.next({
				agendas,
				idAgendaSelected: this.agendaSelected?.id,
				filteredBy: {idProfesional}
			});
		});
	}

	private clearSearch(): void {
		delete this.agendaSelected;
		this.agendasSource.next({
			agendas: undefined,
			idAgendaSelected: undefined,
			filteredBy: undefined
		});
	}

	private newAgendaSelected(agendaSelected: CompleteDiaryDto): boolean {
		return agendaSelected && agendaSelected.id !== this.agendaSelected?.id;
	}

	private updateProfesionalSelected(agendaSelected: CompleteDiaryDto): void {
		this.agendaFiltersSource.next({idProfesional: agendaSelected.healthcareProfessionalId});
	}

	private updateSelectAgenda(): void {
		this.agendasSource.next({
			agendas: this.agendasSource.getValue()?.agendas,
			idAgendaSelected: undefined,
			filteredBy: this.agendasSource.getValue()?.filteredBy
		});
	}
}

export interface AgendaOptionsData {
	agendas: DiaryListDto[];
	idAgendaSelected?: number;
	filteredBy: AgendaFilters;
}

export interface AgendaFilters {
	idProfesional: number;
}

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
	private agendaSelected = new BehaviorSubject<CompleteDiaryDto>(undefined);

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

	getAgendaSelected$(): Observable<CompleteDiaryDto> {
		return this.agendaSelected?.asObservable();
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
			this.updateSelectAgenda(agendaSelected);
		}
		if (this.newAgendaSelected(agendaSelected)) {
			this.updateProfesionalSelected(agendaSelected);
		}
		this.agendaSelected?.next(agendaSelected);
	}

	clearAll(): void {
		this.agendaFiltersSource.next(null);
		this.agendasSource.next(null);
		this.agendaSelected?.next(null);
	}

	private updateDiaries(idProfesional: number): void {
		this.diariesService.getDiaries(idProfesional).subscribe(agendas => {
			this.agendasSource.next({
				agendas,
				idAgendaSelected: this.agendaSelected?.getValue()?.id,
				filteredBy: { idProfesional }
			});
		});
	}

	private clearSearch(): void {
		this.agendaSelected.next(null);
		this.agendasSource.next({
			agendas: undefined,
			idAgendaSelected: undefined,
			filteredBy: undefined
		});
	}

	private newAgendaSelected(agendaSelected: CompleteDiaryDto): boolean {
		return agendaSelected && agendaSelected.id !== this.agendaSelected?.getValue()?.id;
	}

	private updateProfesionalSelected(agendaSelected: CompleteDiaryDto): void {
		this.agendaFiltersSource.next({ idProfesional: agendaSelected.healthcareProfessionalId });
	}

	private updateSelectAgenda(agendaSelected: CompleteDiaryDto): void {
		const idProfesional = agendaSelected?.healthcareProfessionalId;
		if (idProfesional) {
			this.diariesService.getDiaries(idProfesional).subscribe(agendas => {
				this.agendasSource.next({
					agendas,
					idAgendaSelected: undefined,
					filteredBy: { idProfesional }
				});
			});
		}
		else {
			this.agendasSource.next({
				agendas: this.agendasSource.getValue()?.agendas,
				idAgendaSelected: undefined,
				filteredBy: this.agendasSource.getValue()?.filteredBy
			});
		}
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

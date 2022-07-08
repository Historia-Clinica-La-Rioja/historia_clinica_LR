import { STYLE } from './../../constants/calendar-professional-view';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatOptionSelectionChange } from '@angular/material/core';
import { DiaryListDto } from '@api-rest/api-model';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { AgendaFilters, AgendaOptionsData, AgendaSearchService } from '@turnos/services/agenda-search.service';
import { isBefore, parseISO, startOfToday } from 'date-fns';
import { Subscription } from 'rxjs';
import { HEADER_CALENDAR_PROFESSIONAL_VIEW } from '@turnos/constants/calendar-professional-view';
import { CalendarView } from 'angular-calendar';
import { AppointmentsFacadeService } from '@turnos/services/appointments-facade.service';

const SINGLE_DIARY = 1;

@Component({
	selector: 'app-calendar-professional-view-dock-popup',
	templateUrl: './calendar-professional-view-dock-popup.component.html',
	styleUrls: ['./calendar-professional-view-dock-popup.component.scss']
})
export class CalendarProfessionalViewDockPopupComponent implements OnInit {

	agendaFiltersSubscription: Subscription;
	filters: AgendaFilters;
	agendaSelected: DiaryListDto;
	agendas: DiaryListDto[] = [];
	userId: number;
	HEADER = HEADER_CALENDAR_PROFESSIONAL_VIEW;
	STYLE = STYLE;
	showButtonToClear = true;
	professionalId: number;
	readonly calendarViewEnum = CalendarView;
	readonly dateFormats = DatePipeFormat;

	constructor(
		private readonly agendaSearchService: AgendaSearchService,
		private readonly healthcareProfessional: HealthcareProfessionalService,
		public dockPopupRef: DockPopupRef,
		private readonly changeDetectorRef: ChangeDetectorRef,
		private readonly appointmentFacade: AppointmentsFacadeService,
	) { }

	ngOnInit(): void {
		this.healthcareProfessional.getHealthcareProfessionalByUserId().subscribe(professionalId => {
			this.professionalId = professionalId;
			this.agendaSearchService.search(professionalId);
			this.agendaFiltersSubscription = this.agendaSearchService.getAgendas$().subscribe((data: AgendaOptionsData) => {
				if (data) {
					this.loadAgendas(data.agendas, data.idAgendaSelected);
					this.filters = data.filteredBy;
				}
			});
		});
	}

	changeAgendaSelected(event: MatOptionSelectionChange, agenda: DiaryListDto): void {
		if (event.isUserInput)
			this.agendaSelected = agenda;
	}

	private loadAgendas(diaries, idAgendaSelected?): void {
		delete this.agendas;
		delete this.agendaSelected;
		this.filterAgendas(diaries);
		if (this.agendas.length === SINGLE_DIARY) {
			this.agendaSelected = this.agendas[0];
			this.showButtonToClear = false;
		}
		if (idAgendaSelected) {
			this.agendaSelected = this.agendas.find(agenda => agenda.id === idAgendaSelected);
		}
	}

	private filterAgendas(diaries: DiaryListDto[]): void {
		this.agendas = [];
		if (diaries?.length)
			diaries.forEach(diary => {
				if (isBefore(startOfToday(), parseISO(diary.endDate)))
					this.agendas.push(diary)
			});
	}

	clear(): void {
		this.agendaSelected = null;
		this.changeDetectorRef.detectChanges();
	}

}

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
import { CalendarProfessionalInformation } from '@turnos/services/calendar-professional-information';

const SINGLE_DIARY = 1;

@Component({
	selector: 'app-calendar-professional-view-dock-popup',
	templateUrl: './calendar-professional-view-dock-popup.component.html',
	styleUrls: ['./calendar-professional-view-dock-popup.component.scss']
})
export class CalendarProfessionalViewDockPopupComponent implements OnInit {

	agendaFiltersSubscription: Subscription;
	filters: AgendaFilters;
	diarySelected: DiaryListDto;
	diaries: DiaryListDto[] = [];
	userId: number;
	HEADER = HEADER_CALENDAR_PROFESSIONAL_VIEW;
	STYLE = STYLE;
	showButtonToClear = true;
	professionalId: number;
	calendarDate: Date;
	readonly calendarViewEnum = CalendarView;
	readonly dateFormats = DatePipeFormat;

	constructor(
		private readonly agendaSearchService: AgendaSearchService,
		private readonly healthcareProfessional: HealthcareProfessionalService,
		public dockPopupRef: DockPopupRef,
		private readonly changeDetectorRef: ChangeDetectorRef,
		private calendarProfessionalInfo: CalendarProfessionalInformation,
		private readonly appointmentFacade: AppointmentsFacadeService,
	) { }

	ngOnInit() {
		this.healthcareProfessional.getHealthcareProfessionalByUserId().subscribe(professionalId => {
			this.professionalId = professionalId;
			this.agendaSearchService.search(professionalId);
			this.appointmentFacade.setProfessionalId(professionalId);
			this.agendaFiltersSubscription = this.agendaSearchService.getAgendas$().subscribe((data: AgendaOptionsData) => {
				if (data) {
					this.loadDiaries(data.agendas, data.idAgendaSelected);
					this.filters = data.filteredBy;
				}
			});
		});
		this.calendarDate = this.calendarProfessionalInfo.getCalendarDate();
	}

	changeDiarySelected(event: MatOptionSelectionChange, diary: DiaryListDto) {
		if (event.isUserInput)
			this.diarySelected = diary;
	}

	private loadDiaries(diaries: DiaryListDto[], idDiarySelected?: number) {
		delete this.diaries;
		delete this.diarySelected;
		this.filterDiaries(diaries);
		if (this.diaries.length === SINGLE_DIARY) {
			this.diarySelected = this.diaries[0];
			this.showButtonToClear = false;
		}
		if (idDiarySelected) {
			this.diarySelected = this.diaries.find(diary => diary.id === idDiarySelected);
		}
	}

	private filterDiaries(diaries: DiaryListDto[]) {
		this.diaries = [];
		if (diaries?.length)
			diaries.forEach(diary => {
				if (isBefore(startOfToday(), parseISO(diary.endDate)))
					this.diaries.push(diary)
			});
	}

	clear() {
		this.diarySelected = null;
		this.changeDetectorRef.detectChanges();
	}

}

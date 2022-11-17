import { STYLE } from './../../constants/calendar-professional-view';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatOptionSelectionChange } from '@angular/material/core';
import { DiaryListDto, ProfessionalDto } from '@api-rest/api-model';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { AgendaFilters, AgendaOptionsData, AgendaSearchService } from '@turnos/services/agenda-search.service';
import { isBefore, isToday, parseISO, startOfToday } from 'date-fns';
import { forkJoin, Subscription } from 'rxjs';
import { HEADER_CALENDAR_PROFESSIONAL_VIEW } from '@turnos/constants/calendar-professional-view';
import { CalendarView } from 'angular-calendar';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import { CalendarProfessionalInformation } from '@turnos/services/calendar-professional-information';
import { AppointmentsFacadeService } from '@turnos/services/appointments-facade.service';
import { FormBuilder, FormGroup } from '@angular/forms';

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
	professionalLogged: ProfessionalDto;
	professionalSelected: ProfessionalDto;
	professionals: ProfessionalDto[] = [];
	calendarDate: Date;
	loading = true;
	form: FormGroup;
	readonly calendarViewEnum = CalendarView;
	readonly dateFormats = DatePipeFormat;

	constructor(
		private readonly agendaSearchService: AgendaSearchService,
		private readonly healthcareProfessional: HealthcareProfessionalService,
		public dockPopupRef: DockPopupRef,
		private readonly changeDetectorRef: ChangeDetectorRef,
		private calendarProfessionalInfo: CalendarProfessionalInformation,
		private readonly healthCareProfessionalService: HealthcareProfessionalByInstitutionService,
		private readonly appointmentFacade: AppointmentsFacadeService,
		private readonly formBuilder: FormBuilder
	) { }

	ngOnInit() {
		this.form = this.formBuilder.group({
			diary: [null]
		});
		const professionalLogged$ = this.healthcareProfessional.getHealthcareProfessionalByUserId();
		const asociatedProfessionals$ = this.healthCareProfessionalService.getAllAssociatedWithActiveDiaries();

		forkJoin([professionalLogged$, asociatedProfessionals$]).subscribe(data => {
			this.professionals = data[1];
			this.professionalLogged = data[1].find(p => p.id === data[0]);
			const profSelected = this.calendarProfessionalInfo.getProfessionalSelected();
			this.professionalSelected = profSelected ? profSelected : this.professionalLogged;
			this.changeDetectorRef.detectChanges();
			if (this.professionals.length === 1) {
				this.appointmentFacade.setProfessional(this.professionalSelected);
				this.agendaSearchService.search(this.professionalSelected?.id);
				this.setDiaries(this.professionalSelected);
			}
		});

		this.calendarDate = this.calendarProfessionalInfo.getCalendarDate();
	}

	ngOnDestroy() {
		this.agendaFiltersSubscription.unsubscribe();
		this.agendaSearchService.clearAll();
	}

	changeDiarySelected(event: MatOptionSelectionChange, diary: DiaryListDto) {
		if (event.isUserInput)
			this.diarySelected = diary;
	}

	setDiaries(professional: ProfessionalDto) {
		if (professional) {
			this.professionalSelected = professional;
			this.calendarProfessionalInfo.setProfessionalSelected(professional);
			this.agendaFiltersSubscription = this.agendaSearchService.getAgendas$().subscribe((data: AgendaOptionsData) => {
				if (data) {
					this.loadDiaries(data.agendas, data.idAgendaSelected);
					this.filters = data.filteredBy;
				}
			});
		}
		else
			this.professionalSelected = null;
	}

	private loadDiaries(diaries: DiaryListDto[], idDiarySelected?: number) {
		delete this.diaries;
		delete this.diarySelected;
		this.filterDiaries(diaries);
		if (this.diaries.length === SINGLE_DIARY) {
			this.diarySelected = this.diaries[0];
			this.form.controls.diary.setValue(this.diarySelected);
			this.showButtonToClear = false;
		}
		if (idDiarySelected) {
			this.diarySelected = this.diaries.find(diary => diary.id === idDiarySelected);
		}
		this.loading = false;
	}

	private filterDiaries(diaries: DiaryListDto[]) {
		this.diaries = [];
		if (diaries?.length)
			diaries.forEach(diary => {
				if (isBefore(startOfToday(), parseISO(diary.endDate)) || isToday(parseISO(diary.endDate)))
					this.diaries.push(diary)
			});
	}

	clear() {
		this.diarySelected = null;
		this.changeDetectorRef.detectChanges();
	}

}

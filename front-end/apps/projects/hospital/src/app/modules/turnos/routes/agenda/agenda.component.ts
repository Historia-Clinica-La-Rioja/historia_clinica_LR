import { CalendarProfessionalInformation } from '../../services/calendar-professional-information';
import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { AppointmentDailyAmountDto, CompleteDiaryDto, DiaryOpeningHoursDto, MedicalCoverageDto, ProfessionalDto, ProfessionalPersonDto } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { CalendarMonthViewBeforeRenderEvent, CalendarView, CalendarWeekViewBeforeRenderEvent, DAYS_OF_WEEK } from 'angular-calendar';
import {
	buildFullDate,
	DateFormat,
	dateToMoment,
	dateToMomentTimeZone,
	momentFormat,
	momentParseDate,
	momentParseTime
} from '@core/utils/moment.utils';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { DiaryService } from '@api-rest/services/diary.service';
import { Moment } from 'moment';
import { NewAppointmentComponent } from '../../dialogs/new-appointment/new-appointment.component';
import { CalendarEvent, MonthViewDay, WeekViewHourSegment } from 'calendar-utils';
import { MEDICAL_ATTENTION } from '../../constants/descriptions';
import { AppointmentComponent } from '../../dialogs/appointment/appointment.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

import { AppointmentsFacadeService } from '@turnos/services/appointments-facade.service';

import { APPOINTMENT_STATES_ID, MINUTES_IN_HOUR } from '../../constants/appointment';
import { map, take } from 'rxjs/operators';
import { forkJoin, Observable, of, Subject } from 'rxjs';
import { PermissionsService } from '@core/services/permissions.service';
import { HealthInsuranceService } from '@api-rest/services/health-insurance.service';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { AgendaSearchService } from '../../services/agenda-search.service';
import { ContextService } from '@core/services/context.service';
import { ConfirmBookingComponent } from '@turnos/dialogs/confirm-booking/confirm-booking.component';
import { DatePipeFormat } from '@core/utils/date.utils';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { LoggedUserService } from '../../../auth/services/logged-user.service';
import * as moment from 'moment';
import { endOfMonth, endOfWeek, startOfMonth, startOfWeek } from 'date-fns';
import { DatePipe } from "@angular/common";
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { TranslateService } from '@ngx-translate/core';
import { PatientNameService } from '../../../core/services/patient-name.service';

const ASIGNABLE_CLASS = 'cursor-pointer';
const AGENDA_PROGRAMADA_CLASS = 'bg-green';
const AGENDA_ESPONTANEA_CLASS = 'bg-yellow';
const ROLES_TO_CREATE: ERole[] = [ERole.ADMINISTRATIVO, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
@Component({
	selector: 'app-agenda',
	templateUrl: './agenda.component.html',
	styleUrls: ['./agenda.component.scss'],
})
export class AgendaComponent implements OnInit, OnDestroy, OnChanges {

	readonly calendarViewEnum = CalendarView;
	readonly MONDAY = DAYS_OF_WEEK.MONDAY;
	readonly dateFormats = DatePipeFormat;
	hasRoleToCreate: boolean;

	hourSegments: number;
	agenda: CompleteDiaryDto;

	loading = true;
	dayStartHour: number;
	dayEndHour: number;
	diaryOpeningHours: DiaryOpeningHoursDto[];

	enableAppointmentScheduling = true;
	appointments: CalendarEvent[];
	holidays: CalendarEvent[] = [];
	dailyAmounts: AppointmentDailyAmountDto[];
	dailyAmounts$: Observable<AppointmentDailyAmountDto[]>;
	refreshCalendar = new Subject<void>();
	startDate: string;
	endDate: string;
	careLinesToShow: string = "";
	associatedProfessionalsToShow: string = "";

	private readonly routePrefix = 'institucion/' + this.contextService.institutionId;
	private patientId: number;
	private loggedUserHealthcareProfessionalId: number;
	private loggedUserRoles: string[];
	@Input() canCreateAppoinment = true;
	idAgenda: number;
	@Input()
	set id(id: number) {
		if (id) {
			this.idAgenda = id;
			this.getAgenda();
		}
	}
	@Input() showAll = true;
	@Input() view: CalendarView = CalendarView.Week;
	@Input() viewDate: Date = new Date();

	constructor(
		private readonly dialog: MatDialog,
		private readonly diaryService: DiaryService,
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private snackBarService: SnackBarService,
		private readonly permissionsService: PermissionsService,
		public readonly appointmentFacade: AppointmentsFacadeService,
		private readonly appointmentsService: AppointmentsService,
		private readonly healthInsuranceService: HealthInsuranceService,
		private readonly agendaSearchService: AgendaSearchService,
		private readonly contextService: ContextService,
		private readonly healthcareProfessionalService: HealthcareProfessionalService,
		private readonly loggedUserService: LoggedUserService,
		private readonly calendarProfessionalInfo: CalendarProfessionalInformation,
		private readonly datePipe: DatePipe,
		private readonly translateService: TranslateService,
		private readonly patientNameService: PatientNameService,
	) {
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe((params: ParamMap) => {
			if (params.get('idAgenda')) {
				this.idAgenda = Number(params.get('idAgenda'));
				this.getAgenda();
			}
		});
		this.route.queryParams.subscribe(qp => {
			this.patientId = Number(qp.idPaciente);
		});

		this.appointmentFacade.clear();
		this.loading = true;
		this.appointmentFacade.getAppointments().subscribe(appointments => {
			if (appointments) {
				if (appointments.length) {
					this.appointments = this.unifyEvents(appointments);
				}
				else {
					this.appointments = appointments;
				}
				this.dailyAmounts$ = this.appointmentsService.getDailyAmounts(this.idAgenda, this.startDate, this.endDate);
				this.loading = false;
			}
		});
		this.appointmentFacade.getHolidays().subscribe(holidays => this.holidays = holidays);
		this.permissionsService.hasContextAssignments$(ROLES_TO_CREATE).subscribe(hasRole => this.hasRoleToCreate = hasRole);
		this.healthcareProfessionalService.getHealthcareProfessionalByUserId().subscribe(healthcareProfessionalId => this.loggedUserHealthcareProfessionalId = healthcareProfessionalId);
		this.loggedUserService.assignments$.subscribe(response => this.loggedUserRoles = response.map(role => role.role));
	}

	ngOnDestroy() {
		this.agendaSearchService.setAgendaSelected(undefined);
		this.appointmentFacade.clear();
	}

	ngOnChanges(changes: SimpleChanges) {
		if (changes.idAgenda?.currentValue)
			this.getAgenda();
	}

	changeViewDate(date: Date) {
		if (this.view !== CalendarView.Month) {
			this.setDateRange(date);
			this.appointmentFacade.setValues(this.agenda.id, this.agenda.appointmentDuration, this.startDate, this.endDate);
			this.calendarProfessionalInfo.setCalendarDate(date);
		}
	}

	loadCalendar(renderEvent: CalendarWeekViewBeforeRenderEvent) {
		if (this.agenda)
			renderEvent.hourColumns.forEach((hourColumn) => {
				const openingHours: DiaryOpeningHoursDto[] = this._getOpeningHoursFor(hourColumn.date);
				if (openingHours.length) {
					hourColumn.hours.forEach((hour) => {
						hour.segments.forEach((segment) => {
							openingHours.forEach(openingHour => {
								const from: Moment = momentParseTime(openingHour.openingHours.from);
								const to: Moment = momentParseTime(openingHour.openingHours.to);
								if (isBetween(segment, from, to)) {
									segment.cssClass = this.getOpeningHoursCssClass(openingHour);
								}
							});
						});
					});
				}
			});

		function isBetween(segment: WeekViewHourSegment, from: Moment, to: Moment) {
			return ((segment.date.getHours() > from.hours()) ||
				(segment.date.getHours() === from.hours() && segment.date.getMinutes() >= from.minutes()))
				&& ((segment.date.getHours() < to.hours()) ||
					(segment.date.getHours() === to.hours() && segment.date.getMinutes() < to.minutes()));
		}
	}

	loadDailyAmounts(calendarMonthViewBeforeRenderEvent: CalendarMonthViewBeforeRenderEvent): void {
		const from = this.datePipe.transform(calendarMonthViewBeforeRenderEvent.period.start, 'YYYY-MM-dd');
		const to = this.datePipe.transform(calendarMonthViewBeforeRenderEvent.period.end, 'YYYY-MM-dd');
		if(this.view === CalendarView.Month){
			this.dailyAmounts$ = this.appointmentsService.getDailyAmounts(this.idAgenda, from, to);
		}
		const daysCells: MonthViewDay[] = calendarMonthViewBeforeRenderEvent.body;
		if (this.appointments) {
			if (agendaOverlapsWithViewRange(this.agenda.startDate, this.agenda.endDate)) {
				this.dailyAmounts$.subscribe(dailyAmounts => {
					if (dailyAmounts) {
						this.dailyAmounts = dailyAmounts;
						this.dailyAmounts$ = of(null);
					}
					this.setDailyAmounts(daysCells, this.dailyAmounts);
				});
			}
		}

		function agendaOverlapsWithViewRange(start: string, end: string): boolean {
			const viewPeriod = calendarMonthViewBeforeRenderEvent.period;
			const startAgenda = momentParseDate(start);
			const endAgenda = momentParseDate(end);

			const firstViewDay = dateToMomentTimeZone(viewPeriod.start);
			const lastViewDay = dateToMomentTimeZone(viewPeriod.end);

			return (startAgenda.isSameOrBefore(lastViewDay, 'days') ||
				firstViewDay.isSameOrBefore(endAgenda, 'days'));
		}

	}

	private setDailyAmounts(daysCells: MonthViewDay[], dailyAmounts: AppointmentDailyAmountDto[]) {
		daysCells.forEach((cell: MonthViewDay) => {
			const amount = getAmount(cell.date);
			if (amount) {
				cell.meta = {
					amount
				};
			}

			function getAmount(date: Date): AppointmentDailyAmountDto {
				return dailyAmounts
					.find(dailyAmount => {
						return dateToMoment(date).isSame(momentParseDate(dailyAmount.date), 'days');
					});
			}
		});
	}

	private getAgenda() {
		this.diaryService.get(this.idAgenda).subscribe(agenda => {
			this.setAgenda(agenda);
			this.agendaSearchService.setAgendaSelected(agenda);
		}, _ => {
			this.snackBarService.showError('turnos.home.AGENDA_NOT_FOUND');
			this.router.navigateByUrl(`${this.routePrefix}/turnos`);
		});
	}

	onClickedSegment(event) {
		if (this.getOpeningHoursId(event.date) && this.enableAppointmentScheduling) {
			const clickedDate: Moment = dateToMomentTimeZone(event.date);
			const openingHourId: number = this.getOpeningHoursId(event.date);
			const diaryOpeningHourDto: DiaryOpeningHoursDto =
				this.diaryOpeningHours.find(diaryOpeningHour => diaryOpeningHour.openingHours.id === openingHourId);

			forkJoin([
				this.getAppointmentAt(event.date).pipe(take(1)),
				this.allOverturnsAssignedForDiaryOpeningHour(diaryOpeningHourDto, clickedDate).pipe(take(1))
			]).subscribe(([busySlot, numberOfOverturnsAssigned]) => {

				if (busySlot && busySlot.meta.appointmentStateId === APPOINTMENT_STATES_ID.BLOCKED) {
					this.snackBarService.showError('No es posible agragar un turno en un horario bloqueado');
					return;
				}
				const addingOverturn = !!busySlot;

				if (addingOverturn && (numberOfOverturnsAssigned === diaryOpeningHourDto.overturnCount)) {
					if (diaryOpeningHourDto.medicalAttentionTypeId !== MEDICAL_ATTENTION.SPONTANEOUS_ID) {
						this.snackBarService.showError('turnos.overturns.messages.ERROR');
						return;
					}
				}

				if (this.loggedUserHealthcareProfessionalId !== this.appointmentFacade.getProfessional()?.id && !this.userHasValidRoles()) {
					this.snackBarService.showError('turnos.new-appointment.messages.NOT_RESPONSIBLE');
					return;
				} else {
						if (!this.holidays?.find(holiday => holiday.start.getDate() === clickedDate.toDate().getDate())) {
							this.openNewAppointmentDialog(clickedDate, openingHourId, addingOverturn);
						}
						else {
							const holidayText = this.translateService.instant('turnos.holiday.HOLIDAY_RELATED');
							const holidayDateText = this.datePipe.transform(clickedDate.toDate(), DatePipeFormat.FULL_DATE);
							const dialogRef = this.dialog.open(DiscardWarningComponent, {
								data: {
									title: 'turnos.holiday.TITLE',
									content: `${holidayDateText.charAt(0).toUpperCase() + holidayDateText.slice(1)} ${holidayText}`,
									contentBold: `turnos.holiday.HOLIDAY_DISCLAIMER`,
									okButtonLabel: 'turnos.holiday.OK_BUTTON',
									cancelButtonLabel: 'turnos.holiday.CANCEL_BUTTON',
								}
							});
							dialogRef.afterClosed().subscribe((result: boolean) => {
								if (result) {
									dialogRef?.close();
								}
								else {
									this.openNewAppointmentDialog(clickedDate, openingHourId, addingOverturn);
								}
							});
						}
					}
			});
		}
	}

	private openNewAppointmentDialog(clickedDate: Moment, openingHourId: number, addingOverturn: boolean) {
		const dialogRef = this.dialog.open(NewAppointmentComponent, {
			width: '35%',
			data: {
				date: clickedDate.format(DateFormat.API_DATE),
				diaryId: this.agenda.id,
				hour: clickedDate.format(DateFormat.HOUR_MINUTE_SECONDS),
				openingHoursId: openingHourId,
				overturnMode: addingOverturn,
				patientId: this.patientId ? Number(this.patientId) : null,
			}
		});
		dialogRef.afterClosed().subscribe(() => this.appointmentFacade.loadAppointments());
	}

	viewAppointment(event: CalendarEvent): void {
		if (event.meta?.appointmentStateId === APPOINTMENT_STATES_ID.BLOCKED || !event.meta) {
			return;
		}
		if (!event.meta.patient?.id) {
			this.dialog.open(ConfirmBookingComponent, {
				width: '30%',
				data: {
					date: event.meta.date.format(DateFormat.API_DATE),
					diaryId: this.agenda.id,
					hour: event.meta.date.format(DateFormat.HOUR_MINUTE_SECONDS),
					openingHoursId: this.getOpeningHoursId(event.meta.date.toDate()),
					overturnMode: false,
					identificationTypeId: event.meta.patient.typeId ? event.meta.patient.typeId : 1,
					idNumber: event.meta.patient.identificationNumber,
					appointmentId: event.meta.appointmentId,
					phoneNumber: event.meta.phoneNumber
				}
			});
		} else {
			let dialogRef;
			if (event.meta.rnos) {
				this.healthInsuranceService.get(event.meta.rnos)
					.subscribe((medicalCoverageDto: MedicalCoverageDto) => {
						event.meta.healthInsurance = medicalCoverageDto;
						dialogRef = this.dialog.open(AppointmentComponent, {
							disableClose: true,
							data: {
								appointmentData: event.meta,
								professionalPermissions: this.agenda.professionalAssignShift,
								agenda: this.agenda
							}
						});
					});
			} else {
				dialogRef = this.dialog.open(AppointmentComponent, {
					disableClose: true,
					data: {
						appointmentData: event.meta,
						hasPermissionToAssignShift: this.agenda.professionalAssignShift,
						agenda: this.agenda
					},
				});
			}
			dialogRef.afterClosed().subscribe((appointmentInformation) => {
				this.viewDate = appointmentInformation.date;
				this.setDateRange(this.viewDate);
				this.appointmentFacade.setValues(this.agenda.id, this.agenda.appointmentDuration, this.startDate, this.endDate);
			});
		}
	}

	setAgenda(agenda: CompleteDiaryDto): void {
		this.resetInformation();
		this.agenda = agenda;
		this.loadInformationToShow(agenda);
		this.setEnableAppointmentScheduling();
		this.viewDate = this._getViewDate();
		this.setDateRange(this.viewDate);
		this.hourSegments = MINUTES_IN_HOUR / agenda.appointmentDuration;
		this.appointmentFacade.setValues(agenda.id, agenda.appointmentDuration, this.startDate, this.endDate);
		this.diaryOpeningHours = agenda.diaryOpeningHours;
		this.setDayStartHourAndEndHour(agenda.diaryOpeningHours);
	}

	goToDayViewOn(date: Date) {
		this.viewDate = date;
		this.view = this.calendarViewEnum.Day;
		this.setDateRange(date);
		this.appointmentFacade.setValues(this.agenda.id, this.agenda.appointmentDuration, this.startDate, this.endDate);
	}

	goToDiary() {
		const url = `institucion/${this.contextService.institutionId}/turnos/agenda/${this.idAgenda}`;
		this.router.navigate([url]);
	}

	private setDayStartHourAndEndHour(openingHours: DiaryOpeningHoursDto[]) {
		openingHours.forEach(oh => {
			const from = momentParseTime(oh.openingHours.from).hour();
			if (this.dayStartHour === undefined || from <= this.dayStartHour) {
				this.dayStartHour = (from > 0) ? from - 1 : from;
			}
			const to = momentParseTime(oh.openingHours.to).hour();
			if (this.dayEndHour === undefined || to >= this.dayEndHour) {
				this.dayEndHour = (to < 23) ? to + 1 : to;
			}
		});
	}

	/**
	 * returns a Date that defines which week is going to show in the header of the calendar
	 *
	 */
	private _getViewDate(): Date {
		const momentStartDate = momentParseDate(this.agenda.startDate);
		const momentEndDate = momentParseDate(this.agenda.endDate);
		const lastSelectedDate = moment(this.viewDate);

		if (lastSelectedDate.isBetween(momentStartDate, momentEndDate)) {
			return this.viewDate;
		}
		if (lastSelectedDate.isSameOrBefore(momentStartDate)) {
			return momentStartDate.toDate();
		}
		return momentEndDate.toDate();
	}

	private getOpeningHoursId(date: Date): number {
		const openingHoursSelectedDay = this._getOpeningHoursFor(date);

		const selectedOpeningHour = openingHoursSelectedDay.find(oh => {
			const hourFrom = momentParseTime(oh.openingHours.from);
			const hourTo = momentParseTime(oh.openingHours.to);
			const selectedHour = dateToMomentTimeZone(date).format(DateFormat.HOUR_MINUTE_SECONDS);

			return momentParseTime(selectedHour).isBetween(hourFrom, hourTo, null, '[)');
		});

		return selectedOpeningHour?.openingHours.id;
	}

	private existsAppointmentAt(date: Date): Observable<boolean> {
		return this.appointmentFacade.getAppointments().pipe(
			map(array => {
				return array.filter(appointment => appointment.start.getTime() === date.getTime()).length > 0;
			})
		);
	}
	private getAppointmentAt(date: Date): Observable<CalendarEvent> {
		return this.appointmentFacade.getAppointments().pipe(
			map(array => {
				return array.find(appointment => appointment.start.getTime() === date.getTime());
			})
		);
	}

	private allOverturnsAssignedForDiaryOpeningHour(diaryOpeningHourDto: DiaryOpeningHoursDto, clickedDate: Moment): Observable<number> {

		const openingHourStart = buildFullDate(diaryOpeningHourDto.openingHours.from, clickedDate);
		const openingHourEnd = buildFullDate(diaryOpeningHourDto.openingHours.to, clickedDate);

		return this.appointmentFacade.getAppointments().pipe(
			map((array: CalendarEvent[]) =>
				array.filter(event =>
					event.meta?.overturn && dateToMoment(event.start).isBetween(openingHourStart, openingHourEnd, null, '[)')
				).length
			)
		);
	}

	private _getOpeningHoursFor(date: Date): DiaryOpeningHoursDto[] {
		const dateMoment = dateToMoment(date);
		const start = momentParseDate(this.agenda.startDate);
		const end = momentParseDate(this.agenda.endDate);

		if (dateMoment.isBetween(start, end, 'date', '[]')) {
			return this.diaryOpeningHours.filter(oh => oh.openingHours.dayWeekId === date.getDay());
		}
		return [];
	}

	private setEnableAppointmentScheduling(): void {
		if (this.agenda.professionalAssignShift) {
			this.enableAppointmentScheduling = true;
		} else {
			this.permissionsService.hasContextAssignments$([ERole.ADMINISTRATIVO, ERole.ADMINISTRADOR_AGENDA])
				.subscribe(hasAdministrativeRole => {
					this.enableAppointmentScheduling = hasAdministrativeRole;
				});
		}
	}

	private getOpeningHoursCssClass(openingHour: DiaryOpeningHoursDto): string {
		if (openingHour.medicalAttentionTypeId === MEDICAL_ATTENTION.SPONTANEOUS_ID) {
			return this.enableAppointmentScheduling ? `${AGENDA_ESPONTANEA_CLASS} ${ASIGNABLE_CLASS}` : AGENDA_ESPONTANEA_CLASS;
		} else {
			return this.enableAppointmentScheduling ? `${AGENDA_PROGRAMADA_CLASS} ${ASIGNABLE_CLASS}` : AGENDA_PROGRAMADA_CLASS;
		}
	}

	private unifyEvents(events: CalendarEvent[]): CalendarEvent[] {
		const notUnifiedEvents = events.sort(
			(firstEvent, secondEvent) => firstEvent.start.getTime() - secondEvent.start.getTime()
		).filter(event => !event.allDay);
		let unifiedEvents = [];
		let processedEvents = [notUnifiedEvents[0]];
		for (let currentNotUnifiedEvent = 1; currentNotUnifiedEvent < notUnifiedEvents.length; currentNotUnifiedEvent++) {
			if (notUnifiedEvents[currentNotUnifiedEvent - 1].end.getTime() === notUnifiedEvents[currentNotUnifiedEvent].start.getTime()
				&& notUnifiedEvents[currentNotUnifiedEvent - 1].title === notUnifiedEvents[currentNotUnifiedEvent].title)
				processedEvents.push(notUnifiedEvents[currentNotUnifiedEvent]);
			else {
				unifiedEvents.push(this.unifyBlockedEvents(processedEvents));
				processedEvents = [notUnifiedEvents[currentNotUnifiedEvent]];
			}
		}
		unifiedEvents.push(this.unifyBlockedEvents(processedEvents));
		unifiedEvents = unifiedEvents.concat(events.filter(event => event.allDay));
		return unifiedEvents;
	}

	private unifyBlockedEvents(events: CalendarEvent[]): CalendarEvent {
		return {
			...events[0],
			start: events[0]?.start,
			end: events[events.length - 1]?.end,
		};
	}

	private userHasValidRoles(): boolean {
		return this.loggedUserRoles.includes(ERole.ADMINISTRATIVO) || this.loggedUserRoles.includes(ERole.ADMINISTRADOR_AGENDA);
	}

	private setDateRange(date: Date) {
		if (CalendarView.Day === this.view) {
			const d = moment(date);
			this.startDate = momentFormat(d, DateFormat.API_DATE);
			this.endDate = momentFormat(d, DateFormat.API_DATE);
			return;
		}
		if (CalendarView.Month === this.view) {
			const from = startOfMonth(date);
			const to = endOfMonth(date);
			this.startDate = momentFormat(moment(from), DateFormat.API_DATE);
			this.endDate = momentFormat(moment(to), DateFormat.API_DATE);
			return;
		}
		const start = startOfWeek(date, { weekStartsOn: 1 });
		this.startDate = momentFormat(moment(start), DateFormat.API_DATE);

		const end = endOfWeek(date, { weekStartsOn: 1 });
		this.endDate = momentFormat(moment(end), DateFormat.API_DATE);
	}

	private loadInformationToShow(diary: CompleteDiaryDto) {
		this.loadCareLinesToShow(diary);
		this.loadAssociatedProfessionalsToShow(diary);
	}

	private loadCareLinesToShow(diary: CompleteDiaryDto) {
		diary.careLinesInfo.forEach((careLine, index) => {
			if (index === diary.careLinesInfo.length - 1)
				this.careLinesToShow = this.careLinesToShow.concat(careLine.description);
			else
				this.careLinesToShow = this.careLinesToShow.concat(careLine.description + ", ");
		});
	}

	private loadAssociatedProfessionalsToShow(diary: CompleteDiaryDto) {
		if (diary.associatedProfessionalsInfo.length) {
			this.appointmentFacade.professional$.subscribe(professional => {
				if (professional) {
					this.associatedProfessionalsToShow = this.associatedProfessionalsToShow.concat(this.getProfessionalFullName(professional) + ", ");
					diary.associatedProfessionalsInfo.forEach((associatedProfessional, index) => {
						if (index === diary.associatedProfessionalsInfo.length - 1)
							this.associatedProfessionalsToShow = this.associatedProfessionalsToShow.concat(this.getProfessionalFullName(associatedProfessional));
						else
							this.associatedProfessionalsToShow = this.associatedProfessionalsToShow.concat(this.getProfessionalFullName(associatedProfessional) + ", ");
					});
				}
			});
		}
	}

	private getProfessionalFullName(professional: ProfessionalPersonDto | ProfessionalDto): string {
		return `${this.patientNameService.getPatientName(professional?.firstName, professional?.nameSelfDetermination)} ${professional?.lastName}`;
	}

	private resetInformation() {
		delete this.dayEndHour;
		delete this.dayStartHour;
		this.careLinesToShow = "";
		this.associatedProfessionalsToShow = "";
	}

}

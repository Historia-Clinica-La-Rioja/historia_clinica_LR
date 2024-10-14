import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { AppFeature, AppointmentDailyAmountDto, CompleteDiaryDto, DiaryOpeningHoursDto, EAppointmentModality, ERole, MedicalCoverageDto } from '@api-rest/api-model';
import { DiaryService } from '@api-rest/services/diary.service';
import {
	buildFullDateFromDate,
	dateISOParseDate,
	dateParseTime,
	isBetweenDates,
	isSameOrBefore,
} from '@core/utils/moment.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { CalendarMonthViewBeforeRenderEvent, CalendarView, CalendarWeekViewBeforeRenderEvent, DAYS_OF_WEEK } from 'angular-calendar';
import { CalendarEvent, MonthViewDay, WeekViewHourSegment } from 'calendar-utils';
import { MEDICAL_ATTENTION } from '../../constants/descriptions';
import { AppointmentComponent } from '../../dialogs/appointment/appointment.component';
import { NewAppointmentComponent } from '../../dialogs/new-appointment/new-appointment.component';
import { CalendarProfessionalInformation } from '../../services/calendar-professional-information';

import { AppointmentsFacadeService } from '@turnos/services/appointments-facade.service';

import { AppointmentsService } from '@api-rest/services/appointments.service';
import { HealthInsuranceService } from '@api-rest/services/health-insurance.service';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { ContextService } from '@core/services/context.service';
import { PermissionsService } from '@core/services/permissions.service';
import { toHourMinuteSecond } from '@core/utils/date.utils';
import { TranslateService } from '@ngx-translate/core';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { ConfirmBookingComponent } from '@turnos/dialogs/confirm-booking/confirm-booking.component';
import { endOfMonth, endOfWeek, isBefore, isSameDay, startOfMonth, startOfWeek } from 'date-fns';
import { forkJoin, Observable, of, Subject } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { LoggedUserService } from '../../../auth/services/logged-user.service';
import { APPOINTMENT_STATES_ID, COLORES, MINUTES_IN_HOUR, WHITE_TEXT } from '../../constants/appointment';
import { AgendaSearchService } from '../../services/agenda-search.service';
import { pushIfNotExists } from '@core/utils/array.utils';
import { MAX_APPOINTMENT_PER_HOUR, getHourFromString } from '@turnos/utils/appointment.utils';
import { AppointmentListComponent } from '@turnos/dialogs/appointment-list/appointment-list.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { fixDate } from '@core/utils/date/format';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

const ASIGNABLE_CLASS = 'cursor-pointer';
const AGENDA_PROGRAMADA_CLASS = 'bg-green';
const AGENDA_ESPONTANEA_CLASS = 'bg-yellow';
const ROLES_TO_CREATE: ERole[] = [ERole.ADMINISTRATIVO, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
const FIVE_MINUTES: number = 5 * 60 * 1000;

@Component({
	selector: 'app-agenda',
	templateUrl: './agenda.component.html',
	styleUrls: ['./agenda.component.scss'],
})
export class AgendaComponent implements OnInit, OnDestroy, OnChanges {

	readonly calendarViewEnum = CalendarView;
	readonly MONDAY = DAYS_OF_WEEK.MONDAY;
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
	private readonly routePrefix = 'institucion/' + this.contextService.institutionId;
	private patientId: number;
	private loggedUserHealthcareProfessionalId: number;
	private loggedUserRoles: string[];
	private timer;
	@Input() canCreateAppoinment = true;
	idAgenda: number = null;
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
	modality: EAppointmentModality = null;
	appointmentsCopy: CalendarEvent[] = [];
	HABILITAR_ACTUALIZACION_AGENDA: boolean = false;
	isEnableTelemedicina: boolean = false

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
		private readonly dateFormatPipe: DateFormatPipe,
		private readonly translateService: TranslateService,
		private readonly featureFlagService: FeatureFlagService
	) {
		this.setFeatureFlags();
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
			this.loading = false;
			if (appointments?.length) {
				this.appointments = this.unifyEvents(appointments);
				this.dailyAmounts$ = this.appointmentsService.getDailyAmounts(this.idAgenda, this.startDate, this.endDate);
			}
			else {
				this.appointments = appointments;
			}
		});
		this.appointmentFacade.getHolidays().subscribe(holidays => this.holidays = holidays);
		this.permissionsService.hasContextAssignments$(ROLES_TO_CREATE).subscribe(hasRole => this.hasRoleToCreate = hasRole);
		this.healthcareProfessionalService.getHealthcareProfessionalByUserId().subscribe(healthcareProfessionalId => this.loggedUserHealthcareProfessionalId = healthcareProfessionalId);
		this.loggedUserService.assignments$.subscribe(response => {
			this.loggedUserRoles = response.filter(role => role.institutionId === this.contextService.institutionId)
				.map(role => role.role)
		});
		this.loadAppointmentsEveryFiveMinutes();
	}

	ngOnDestroy() {
		this.agendaSearchService.setAgendaSelected(null);
		this.appointmentFacade.clear();
		clearInterval(this.timer);
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
								const from = dateParseTime(openingHour.openingHours.from);
								const to = dateParseTime(openingHour.openingHours.to);
								if (isBetween(segment, from, to)) {
									segment.cssClass = this.getOpeningHoursCssClass(openingHour);
								}
							});
						});
					});
				}
			});

		function isBetween(segment: WeekViewHourSegment, from: Date, to: Date) {
			return ((segment.date.getHours() > from.getHours()) ||
				(segment.date.getHours() === from.getHours() && segment.date.getMinutes() >= from.getMinutes()))
				&& ((segment.date.getHours() < to.getHours()) ||
					(segment.date.getHours() === to.getHours() && segment.date.getMinutes() < to.getMinutes()));
		}
	}

	loadDailyAmounts(calendarMonthViewBeforeRenderEvent: CalendarMonthViewBeforeRenderEvent): void {
		const from = toApiFormat(calendarMonthViewBeforeRenderEvent.period.start);
		const to = toApiFormat(calendarMonthViewBeforeRenderEvent.period.end);
		if (this.view === CalendarView.Month) {
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
			const startAgenda = dateISOParseDate(start);
			const endAgenda = dateISOParseDate(end);

			return (isSameOrBefore(startAgenda, viewPeriod.end) || isSameOrBefore(viewPeriod.start, endAgenda));
		}

	}

	private setFeatureFlags = () => {
		this.featureFlagService.isActive(AppFeature.HABILITAR_TELEMEDICINA).subscribe(isEnabled => this.isEnableTelemedicina = isEnabled);
		this.featureFlagService.isActive(AppFeature.HABILITAR_ACTUALIZACION_AGENDA).subscribe(isOn => this.HABILITAR_ACTUALIZACION_AGENDA = isOn);
	}

	private loadAppointmentsEveryFiveMinutes = () => {
		if (this.HABILITAR_ACTUALIZACION_AGENDA)
			this.timer = setInterval(() => this.appointmentFacade.loadAppointments(), FIVE_MINUTES);
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
						return isSameDay(date, dateISOParseDate(dailyAmount.date));
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

	setModality(diaryOpeningHour: DiaryOpeningHoursDto) {
		if (diaryOpeningHour.onSiteAttentionAllowed) {
			this.modality = EAppointmentModality.ON_SITE_ATTENTION;
		}
		if (diaryOpeningHour.patientVirtualAttentionAllowed) {
			if (this.modality === null) {
				this.modality = EAppointmentModality.PATIENT_VIRTUAL_ATTENTION;
			} else {
				this.modality = null;
			}
		}
	}

	onClickedSegment(event) {
		if (this.getOpeningHoursId(event.date) && this.enableAppointmentScheduling) {

			const clickedDate = fixDate(event.date);
			const openingHourId: number = this.getOpeningHoursId(event.date);
			const diaryOpeningHourDto: DiaryOpeningHoursDto =
				this.diaryOpeningHours.find(diaryOpeningHour => diaryOpeningHour.openingHours.id === openingHourId);

			if (diaryOpeningHourDto.secondOpinionVirtualAttentionAllowed && !diaryOpeningHourDto.patientVirtualAttentionAllowed && !diaryOpeningHourDto.onSiteAttentionAllowed) {
				this.snackBarService.showError('turnos.home.messages.NO_ACCEPT_FACE_TO_FACE_APPOINTMMENTS');
				return;
			}

			this.setModality(diaryOpeningHourDto);
			if (!this.isEnableTelemedicina && this.modality !== null && this.modality !== EAppointmentModality.ON_SITE_ATTENTION) {
				this.snackBarService.showError('turnos.home.messages.NO_ACCEPT_FACE_TO_FACE_APPOINTMMENTS');
				this.modality = null;
				return;
			}
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
					if (!this.holidays?.find(holiday => holiday.start.getDate() === clickedDate.getDate())) {
						this.openNewAppointmentDialog(clickedDate, openingHourId, addingOverturn);
					}
					else {
						const holidayText = this.translateService.instant('turnos.holiday.HOLIDAY_RELATED');
						const holidayDateText = this.dateFormatPipe.transform(clickedDate, 'fulldate');
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

	private openNewAppointmentDialog(clickedDate: Date, openingHourId: number, addingOverturn: boolean) {
		const dialogRef = this.dialog.open(NewAppointmentComponent, {
			width: '43%',
			disableClose: true,
			data: {
				date: toApiFormat(clickedDate),
				diaryId: this.agenda.id,
				hour: toHourMinuteSecond(clickedDate),
				openingHoursId: openingHourId,
				overturnMode: addingOverturn,
				patientId: this.patientId ? Number(this.patientId) : null,
				modalityAttention: this.modality,
				expiredAppointment: isBefore(clickedDate, new Date())
			}
		});
		dialogRef.afterClosed().subscribe(() => this.appointmentFacade.loadAppointments(), this.modality = null);
	}

	viewAppointment(event: CalendarEvent): void {
		if (event.meta?.appointmentStateId === APPOINTMENT_STATES_ID.BLOCKED || !event.meta) {
			return;
		}

		if (event.meta.quantity) {
			return this.openAppointmentListDialog(event);
		}

		if (!event.meta.patient?.id) {
			this.dialog.open(ConfirmBookingComponent, {
				width: '30%',
				data: {
					date: event.meta.date,
					diaryId: this.agenda.id,
					openingHoursId: this.getOpeningHoursId(event.meta.date),
					overturnMode: false,
					identificationTypeId: event.meta.patient.typeId ? event.meta.patient.typeId : 1,
					idNumber: event.meta.patient.identificationNumber,
					appointmentId: event.meta.appointmentId,
					phoneNumber: event.meta.phoneNumber,
					fullName: event.meta.patient.fullName,
					email: event.meta.patient.email,
					phonePrefix: event.meta.phonePrefix
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
				dialogRef.afterClosed().subscribe((appointmentInformation) => {
					this.viewDate = appointmentInformation.date;
					this.setDateRange(this.viewDate);
					this.appointmentFacade.setValues(this.agenda.id, this.agenda.appointmentDuration, this.startDate, this.endDate);
				});
			}
		}
	}

	private openAppointmentListDialog = (event: CalendarEvent) => {
		this.dialog.open(AppointmentListComponent, {
			data: {
				date: event.start,
				hasPermissionToAssignShift: this.agenda.professionalAssignShift,
				agenda: this.agenda,
				appointments: this.appointmentsCopy
			}
		});
	}

	setAgenda(agenda: CompleteDiaryDto): void {
		this.resetInformation();
		this.agenda = agenda;
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
			const from = dateParseTime(oh.openingHours.from).getHours();
			if (this.dayStartHour === undefined || from <= this.dayStartHour) {
				this.dayStartHour = (from > 0) ? from - 1 : from;
			}
			const to = dateParseTime(oh.openingHours.to).getHours();
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
		const startDate = dateISOParseDate(this.agenda.startDate);
		const endDate = dateISOParseDate(this.agenda.endDate);
		const lastSelectedDate = this.viewDate;

		if (isBetweenDates(lastSelectedDate, startDate, endDate)) {
			return this.viewDate;
		}
		if (isSameOrBefore(lastSelectedDate, startDate)) {
			return startDate;
		}
		return endDate;
	}

	private getOpeningHoursId(date: Date): number {
		const openingHoursSelectedDay = this._getOpeningHoursFor(date);

		const selectedOpeningHour = openingHoursSelectedDay.find(oh => {
			const hourFrom = dateParseTime(oh.openingHours.from);
			const hourTo = dateParseTime(oh.openingHours.to);
			const selectedHour = toHourMinuteSecond(date);

			return isBetweenDates(dateParseTime(selectedHour), hourFrom, hourTo, '[)');
		});

		return selectedOpeningHour?.openingHours.id;
	}

	private getAppointmentAt(date: Date): Observable<CalendarEvent> {
		return this.appointmentFacade.getAppointments().pipe(
			map(array => {
				return array.find(appointment => appointment.start.getTime() === date.getTime());
			})
		);
	}

	private allOverturnsAssignedForDiaryOpeningHour(diaryOpeningHourDto: DiaryOpeningHoursDto, clickedDate: Date): Observable<number> {

		const openingHourStart = buildFullDateFromDate(diaryOpeningHourDto.openingHours.from, clickedDate);
		const openingHourEnd = buildFullDateFromDate(diaryOpeningHourDto.openingHours.to, clickedDate);

		return this.appointmentFacade.getAppointments().pipe(
			map((array: CalendarEvent[]) =>
				array.filter(event =>
					event.meta?.overturn && isBetweenDates(event.start, openingHourStart, openingHourEnd, '[)')
				).length
			)
		);
	}

	private _getOpeningHoursFor(date: Date): DiaryOpeningHoursDto[] {
		const start = dateISOParseDate(this.agenda.startDate);
		const end = dateISOParseDate(this.agenda.endDate);
        end.setHours(23,59,59,59);

		return isBetweenDates(date, start, end, '[]') ?
			this.diaryOpeningHours.filter(oh => oh.openingHours.dayWeekId === date.getDay()) : [];
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
		this.appointmentsCopy = unifiedEvents;
		unifiedEvents = this.unifyManyAppointments(unifiedEvents);
		return unifiedEvents;
	}

	private unifyManyAppointments = (unifiedEvents: CalendarEvent[]) => {
		const dates: Date[] = this.getUniqueDates(unifiedEvents);
		return this.getAppointmentsFromDateTime(dates, unifiedEvents);
	}

	private compareAppointmentsDate = (date: Date, date2: Date): boolean => {
		return new Date(date).toLocaleDateString() === new Date(date2).toLocaleDateString()
			&& new Date(date).toLocaleTimeString() === new Date(date2).toLocaleTimeString()
	}

	private getUniqueDates = (unifiedEvents: CalendarEvent[]): Date[] => {
		let dates: Date[] = [];
		unifiedEvents.map((event: CalendarEvent) => {
			if (event.start)
				dates = pushIfNotExists<Date>(dates, event.start, this.compareAppointmentsDate);
		});
		return dates;
	}

	private getAppointmentsFromDateTime = (dates: Date[], unifiedEvents: CalendarEvent[]) => {
		const filteredUnifiedEvents: CalendarEvent[] = [];

		const eventsPerDate: Map<string, number> = this.getEventQuantityByDate(unifiedEvents);
		dates.forEach((date: Date) => {
			const dateString = date.toISOString();

			if (eventsPerDate.has(dateString) && eventsPerDate.get(dateString) >= MAX_APPOINTMENT_PER_HOUR) {
				const firstEvent = unifiedEvents.find((ce: CalendarEvent) => this.compareAppointmentsDate(ce.start, date));
				if (firstEvent) {
					this.parseNameAndCssToAppointmentGroup(firstEvent, eventsPerDate, dateString);
					filteredUnifiedEvents.push(firstEvent);
				}
			} else {
				unifiedEvents.forEach((ce: CalendarEvent) => {
					if (this.compareAppointmentsDate(ce.start, date)) {
						filteredUnifiedEvents.push(ce);
					}
				});
			}
		});
		return filteredUnifiedEvents;
	}

	private getEventQuantityByDate = (unifiedEvents: CalendarEvent[]) => {
		const eventsPerDate: Map<string, number> = new Map();
		unifiedEvents.forEach((ce: CalendarEvent) => {
			const dateString = ce.start?.toISOString();
			eventsPerDate.set(dateString, (eventsPerDate.get(dateString) || 0) + 1);
		});
		return eventsPerDate;
	}

	private unifyBlockedEvents(events: CalendarEvent[]): CalendarEvent {
		return {
			...events[0],
			start: events[0]?.start,
			end: events[events.length - 1]?.end,
		};
	}

	private parseNameAndCssToAppointmentGroup = (firstEvent: CalendarEvent, eventsPerDate: Map<string, number>, dateString: string) => {
		const hour = getHourFromString(firstEvent.title);
		const quantity = eventsPerDate.get(dateString);
		firstEvent.title = `${hour} ${quantity} turnos`;
		firstEvent.meta.quantity = quantity;
		firstEvent.color = {
			primary: COLORES.ASSIGNED,
			secondary: COLORES.ASSIGNED
		}
		firstEvent.cssClass = WHITE_TEXT;
	}

	private userHasValidRoles(): boolean {
		return this.loggedUserRoles.includes(ERole.ADMINISTRATIVO) || this.loggedUserRoles.includes(ERole.ADMINISTRADOR_AGENDA);
	}

	private setDateRange(date: Date) {
		if (CalendarView.Day === this.view) {
			this.startDate = toApiFormat(date);
			this.endDate = toApiFormat(date);
			return;
		}
		if (CalendarView.Month === this.view) {
			const from = startOfMonth(date);
			const to = endOfMonth(date);
			this.startDate = toApiFormat(from);
			this.endDate = toApiFormat(to);
			return;
		}
		const start = startOfWeek(date, { weekStartsOn: 1 });
		this.startDate = toApiFormat(start);

		const end = endOfWeek(date, { weekStartsOn: 1 });
		this.endDate = toApiFormat(end);
	}


	private resetInformation() {
		delete this.dayEndHour;
		delete this.dayStartHour;
	}

}

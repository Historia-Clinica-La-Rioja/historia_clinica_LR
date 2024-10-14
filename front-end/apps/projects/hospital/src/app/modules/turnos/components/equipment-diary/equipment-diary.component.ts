import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CompleteEquipmentDiaryDto, DiaryOpeningHoursDto, ERole, MedicalCoverageDto } from '@api-rest/api-model';
import { EquipmentDiaryService } from '@api-rest/services/equipment-diary.service';
import { HealthInsuranceService } from '@api-rest/services/health-insurance.service';
import { PermissionsService } from '@core/services/permissions.service';
import { toHourMinuteSecond } from '@core/utils/date.utils';
import { buildFullDateFromDate, dateISOParseDate, isBetweenDates, isSameOrBefore } from '@core/utils/moment.utils';
import { TranslateService } from '@ngx-translate/core';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { APPOINTMENT_STATES_ID, MINUTES_IN_HOUR } from '@turnos/constants/appointment';
import { MEDICAL_ATTENTION } from '@turnos/constants/descriptions';
import { ConfirmBookingComponent } from '@turnos/dialogs/confirm-booking/confirm-booking.component';
import { NewAppointmentComponent } from '@turnos/dialogs/new-appointment/new-appointment.component';
import { CalendarEvent, CalendarView, CalendarWeekViewBeforeRenderEvent, DAYS_OF_WEEK } from 'angular-calendar';
import { endOfMonth, endOfWeek, startOfMonth, startOfWeek } from 'date-fns';
import { Subject } from 'rxjs';
import { EquipmentAppointmentsFacadeService } from '../../services/equipment-appointments-facade.service';
import { OpeningHoursDiaryService } from '../../services/opening-hours-diary.service';
import { SearchEquipmentDiaryService } from '../../services/search-equipment-diary.service';
import { ImageNetworkAppointmentComponent } from '../image-network-appointment/image-network-appointment.component';
import { fixDate } from '@core/utils/date/format';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { TranscribedOrderService } from '@turnos/services/transcribed-order.service';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

@Component({
	selector: 'app-equipment-diary',
	templateUrl: './equipment-diary.component.html',
	styleUrls: ['./equipment-diary.component.scss'],
	providers: [SearchEquipmentDiaryService, OpeningHoursDiaryService, EquipmentAppointmentsFacadeService]
})
export class EquipmentDiaryComponent implements OnInit {

	hourSegments: number;
	diary: CompleteEquipmentDiaryDto;

	view: CalendarView = CalendarView.Week;
	viewDate: Date = new Date();

	refreshCalendar = new Subject<void>();

	hasRoleToCreate = false;
	appointments: CalendarEvent[] = [];
	holidays: CalendarEvent[] = [];

	startDate: string;
	endDate: string;

	readonly calendarViewEnum = CalendarView;
	readonly MONDAY = DAYS_OF_WEEK.MONDAY;

	@Input()
	set diaryId(diaryId: number) {
		if (diaryId) {
			this.getAgenda(diaryId);
		}
	}

	constructor(
		private snackBarService: SnackBarService,
		private readonly equipmentDiaryService: EquipmentDiaryService,
		private readonly searchEquipmentDiary: SearchEquipmentDiaryService,
		readonly openingHoursService: OpeningHoursDiaryService,
		private readonly permissionService: PermissionsService,
		private readonly healthInsuranceService: HealthInsuranceService,
		private readonly dialog: MatDialog,
		private readonly equipmentAppointmentsFacade: EquipmentAppointmentsFacadeService,
		private readonly translateService: TranslateService,
		private readonly dateFormatPipe: DateFormatPipe,
		private readonly transcribedOrderService: TranscribedOrderService
	) { }

	ngOnInit() {
		this.permissionService.hasContextAssignments$([ERole.ADMINISTRATIVO_RED_DE_IMAGENES]).subscribe(hasRole => this.hasRoleToCreate = hasRole);

		this.equipmentAppointmentsFacade.getAppointments().subscribe(appointments => {
			if (appointments) {
				this.appointments = appointments;
			}
		})
		this.equipmentAppointmentsFacade.getHolidays().subscribe(holidays => this.holidays = holidays);
	}

	loadCalendar(renderEvent: CalendarWeekViewBeforeRenderEvent) {
		if (this.diary) {
			this.openingHoursService.loadOpeningHoursOfCalendar(renderEvent, this.diary.startDate, this.diary.endDate);
			this.openingHoursService.setDiaryOpeningHours(this.diary.equipmentDiaryOpeningHours);
		}
	}

	setAgenda(agenda: CompleteEquipmentDiaryDto) {
		this.diary = agenda;
		this.viewDate = this._getViewDate();
		this.setDateRange(this.viewDate);
		this.hourSegments = MINUTES_IN_HOUR / agenda.appointmentDuration;
		this.openingHoursService.setDiaryOpeningHours(agenda.equipmentDiaryOpeningHours);
		this.openingHoursService.setDayStartHourAndEndHour();
		this.equipmentAppointmentsFacade.setValues(agenda.id, agenda.appointmentDuration, this.startDate, this.endDate);
	}

	goToDayViewOn(date: Date) {
		this.viewDate = date;
		this.view = this.calendarViewEnum.Day;
		this.setDateRange(date);
	}

	onClickedSegment(event) {
		const openingHourId = this.openingHoursService.getOpeningHoursId(this.diary.startDate, this.diary.endDate, event.date);
		if (openingHourId) {
			const clickedDate = fixDate(event.date);
			const diaryOpeningHourDto: DiaryOpeningHoursDto = this.openingHoursService.getEquipmentDiaryOpeningHours().find(diaryOpeningHour => diaryOpeningHour.openingHours.id === openingHourId);

			const busySlot = this.getAppointmentAt(clickedDate);
			const numberOfOverturnsAssigned = this.allOverturnsAssignedForDiaryOpeningHour(diaryOpeningHourDto, clickedDate)

			const addingOverturn = !!busySlot;

			if (this.verifyOverturns(addingOverturn, numberOfOverturnsAssigned, diaryOpeningHourDto)) {
				return;
			}

			const isHoliday = this.holidays.find(holiday => holiday.start.getDate() === clickedDate.getDate());

			if (!isHoliday) {
				this.openNewAppointmentDialog(clickedDate, openingHourId, addingOverturn);
			}
			else {
				this.openWarningHoliday(clickedDate, openingHourId, addingOverturn);
			}
		}
	}

	changeViewDate(date: Date) {
		if (this.view !== CalendarView.Month) {
			this.setDateRange(date);
			this.equipmentAppointmentsFacade.setValues(this.diary.id, this.diary.appointmentDuration, this.startDate, this.endDate);
		}
	}

	private getAppointmentAt(date: Date): CalendarEvent {
		return this.appointments.find(appointment => appointment.start.getTime() === date.getTime());
	}

	private allOverturnsAssignedForDiaryOpeningHour(diaryOpeningHourDto: DiaryOpeningHoursDto, clickedDate: Date): number {
		const openingHourStart = buildFullDateFromDate(diaryOpeningHourDto.openingHours.from, clickedDate);
		const openingHourEnd = buildFullDateFromDate(diaryOpeningHourDto.openingHours.to, clickedDate);
		return this.appointments.filter(event =>
			event.meta?.overturn && isBetweenDates(event.start, openingHourStart, openingHourEnd, '[)')
		).length
	}

	private _getViewDate(): Date {
		const startDate = dateISOParseDate(this.diary.startDate);
		const endDate = dateISOParseDate(this.diary.endDate);
		const lastSelectedDate = this.viewDate;

		if (isBetweenDates(lastSelectedDate, startDate, endDate)) {
			return this.viewDate;
		}
		if (isSameOrBefore(lastSelectedDate, startDate)) {
			return startDate;
		}
		return endDate;
	}

	private getAgenda(diaryId: number) {
		this.equipmentDiaryService.getBy(diaryId).subscribe(agenda => {
			this.setAgenda(agenda);
			this.searchEquipmentDiary.setAgendaSelected(agenda);
		}, _ => {
			this.snackBarService.showError('turnos.home.AGENDA_NOT_FOUND');
		});
	}

	private openNewAppointmentDialog(clickedDate: Date, openingHourId: number, addingOverturn: boolean) {
		const dialogRef = this.dialog.open(NewAppointmentComponent, {
			width: '40%',
			disableClose: true,
			data: {
				date: toApiFormat(clickedDate),
				diaryId: this.diary.id,
				hour: toHourMinuteSecond(clickedDate),
				openingHoursId: openingHourId,
				overturnMode: addingOverturn,
				patientId: null,
				isEquipmentAppointment: true
			}
		});
		dialogRef.afterClosed().subscribe(() => this.equipmentAppointmentsFacade.loadAppointments());
	}

	viewAppointment(event: CalendarEvent): void {
		if (event.meta?.appointmentStateId === APPOINTMENT_STATES_ID.BLOCKED || !event.meta) {
			return;
		}
		if (!event.meta.patient?.id) {
			const date = fixDate(event.meta.date);
			this.dialog.open(ConfirmBookingComponent, {
				width: '30%',
				data: {
					date: date,
					diaryId: this.diary.id,
					openingHoursId: this.openingHoursService.getOpeningHoursId(this.diary.startDate, this.diary.endDate, date),
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
						dialogRef = this.dialog.open(ImageNetworkAppointmentComponent, {
							disableClose: true,
							data: {
								appointmentData: event.meta,
								professionalPermissions: this.hasRoleToCreate,
								agenda: this.diary
							}
						});
					});
			} else {
				dialogRef = this.dialog.open(ImageNetworkAppointmentComponent, {
					disableClose: true,
					data: {
						appointmentData: event.meta,
						hasPermissionToAssignShift: this.hasRoleToCreate,
						agenda: this.diary
					},
				});
			}
			dialogRef.afterClosed().subscribe((appointmentInformation) => {
				this.transcribedOrderService.resetTranscribedOrder()
				this.viewDate = appointmentInformation.date;
				this.setDateRange(this.viewDate);
				this.equipmentAppointmentsFacade.setValues(this.diary.id, this.diary.appointmentDuration, this.startDate, this.endDate);
			});
		}
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

	private verifyOverturns(addingOverturn: boolean, numberOfOverturnsAssigned: number, diaryOpeningHourDto: DiaryOpeningHoursDto) {
		if (addingOverturn && (numberOfOverturnsAssigned === diaryOpeningHourDto.overturnCount)) {
			if (diaryOpeningHourDto.medicalAttentionTypeId !== MEDICAL_ATTENTION.SPONTANEOUS_ID) {
				this.snackBarService.showError('turnos.overturns.messages.ERROR');
				return true;
			}
		}
		else
			return false;
	}

	private openWarningHoliday(clickedDate: Date, openingHourId: number, addingOverturn: boolean) {
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
		dialogRef.afterClosed().subscribe(result => {
			if (result) {
				dialogRef?.close();
			}
			else {
				this.openNewAppointmentDialog(clickedDate, openingHourId, addingOverturn);
			}
		})
	}

}

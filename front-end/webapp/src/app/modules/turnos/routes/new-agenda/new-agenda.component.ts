import { ChangeDetectorRef, Component, ElementRef, OnInit } from '@angular/core';
import { SectorService } from '@api-rest/services/sector.service';
import { ClinicalSpecialtySectorService } from '@api-rest/services/clinical-specialty-sector.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { scrollIntoError } from '@core/utils/form.utils';
import { ConfirmDialogComponent } from '@core/dialogs/confirm-dialog/confirm-dialog.component';
import { TranslateService } from '@ngx-translate/core';
import { MatDialog } from '@angular/material/dialog';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { ContextService } from '@core/services/context.service';
import { Router } from '@angular/router';
import { APPOINTMENT_DURATIONS } from '../../constants/appointment';
import { NewAgendaService } from '../../services/new-agenda.service';
import { momentFormat, DateFormat, momentParseDateTime, momentFormatDate } from '@core/utils/moment.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Moment } from 'moment';
import { DoctorsOfficeDto, ProfessionalDto, DiaryADto, DiaryOpeningHoursDto, OccupationDto, TimeRangeDto } from '@api-rest/api-model';
import * as moment from 'moment';
import { CalendarEvent } from 'angular-calendar';
import { map } from 'rxjs/operators';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { DiaryService } from '@api-rest/services/diary.service';

const ROUTE_APPOINTMENT = 'turnos';

@Component({
	selector: 'app-new-agenda',
	templateUrl: './new-agenda.component.html',
	styleUrls: ['./new-agenda.component.scss']
})
export class NewAgendaComponent implements OnInit {

	public form: FormGroup;
	public sectors;
	public specialties;
	public doctorOffices: DoctorsOfficeDto[];
	public professionals;
	public appointmentManagement: boolean = false;
	public autoRenew: boolean = false;
	public holidayWork: boolean = false;
	public appointmentDurations;
	public openingTime: number;
	public closingTime: number;

	private readonly routePrefix;

	newAgendaService: NewAgendaService;

	constructor(private readonly formBuilder: FormBuilder,
		private readonly el: ElementRef,
		private readonly sectorService: SectorService,
		private readonly clinicalSpecialtySectorService: ClinicalSpecialtySectorService,
		private translator: TranslateService,
		private dialog: MatDialog,
		private doctorsOfficeService: DoctorsOfficeService,
		private healthcareProfessionalService: HealthcareProfessionalService,
		private readonly contextService: ContextService,
		private readonly router: Router,
		private readonly cdr: ChangeDetectorRef,
		private readonly diaryService: DiaryService,
		private readonly snackBarService: SnackBarService,
		private readonly diaryOpeningHoursService: DiaryOpeningHoursService
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
		this.newAgendaService = new NewAgendaService(this.dialog, this.cdr);
	}

	ngOnInit(): void {

		this.form = this.formBuilder.group({
			sectorId: [null, [Validators.required]],
			specialtyId: [{ value: null, disabled: true }, [Validators.required]],
			doctorOffice: [{ value: null, disabled: true }, [Validators.required]],
			healthcareProfessionalId: [{ value: null, disabled: true }, [Validators.required]],
			startDate: [null, [Validators.required]],
			endDate: [null, [Validators.required]],
			appointmentDuration: [null, [Validators.required]],
		});

		this.sectorService.getAll().subscribe(data => {
			this.sectors = data;
		});

		this.appointmentDurations = APPOINTMENT_DURATIONS;
	}

	setSpecialties(): void {
		const sectorId: number = this.form.controls.sectorId.value;
		this.clinicalSpecialtySectorService.getClinicalSpecialty(sectorId).subscribe(data => {
			this.specialties = data;
		});
		this.enableFormControl('specialtyId');
	}

	setDoctorOffices(): void {
		this.doctorsOfficeService.getAll(this.form.value.sectorId, this.form.value.specialtyId)
			.subscribe((data: DoctorsOfficeDto[]) => this.doctorOffices = data);
		this.enableFormControl('doctorOffice');
	}

	setProfessionals(): void {
		//TODO para traer doctores por sector utilizar let sectorId: number = this.form.controls.sectorId.value;
		this.healthcareProfessionalService.getAll().subscribe(data => this.professionals = data);
		this.enableFormControl('healthcareProfessionalId');
		this.openingTime = getHours(this.form.value.doctorOffice.openingTime);
		this.closingTime = getHours(this.form.value.doctorOffice.closingTime) - 1; // we don't want to include the declared hour

		this.getAllWeeklyDoctorsOfficeOcupation();

		function getHours(time: string): number {
			const hours = moment(time, 'HH:mm:ss');
			return Number(hours.hours());
		}
	}

	getFullNameLicence(professional: ProfessionalDto): string {
		return `${professional.lastName}, ${professional.firstName} - ${professional.licenceNumber}`;
	}

	enableAgendaDetails(): void {
		this.enableFormControl('startDate');
		this.enableFormControl('appointmentDuration');
	}

	appointmentManagementChange(): void {
		this.appointmentManagement = !this.appointmentManagement;
	}

	private enableFormControl(controlName): void {
		this.form.get(controlName).reset();
		this.form.get(controlName).enable();
	}

	autoRenewChange(): void {
		this.autoRenew = !this.autoRenew;
	}

	holidayWorkChange(): void {
		this.holidayWork = !this.holidayWork;
	}

	save(): void {
		if (this.form.valid) {
			this.openDialog();
		} else {
			scrollIntoError(this.form, this.el);
		}
	}

	openDialog(): void {
		this.translator.get('turnos.new-agenda.CONFIRM').subscribe((res: string) => {
			const dialogRef = this.dialog.open(ConfirmDialogComponent, {
				width: '450px',
				data: {
					title: 'Nueva agenda',
					content: `${res}`,
					okButtonLabel: 'Confirmar'
				}
			});

			dialogRef.afterClosed().subscribe(confirmed => {
				if (confirmed) {
					const agenda: DiaryADto = this.mapEventsToAgendaDto();
					this.diaryService.addDiary(agenda)
						.subscribe((agendaId: number) => {
							if (agendaId) {
								this.snackBarService.showSuccess('turnos.new-agenda.messages.SUCCESS');
								const url = `${this.routePrefix}${ROUTE_APPOINTMENT}`;
								this.router.navigate([url]);
							}

						});
				}
			});
		});
	}

	private mapEventsToAgendaDto(): DiaryADto {
		return {

			appointmentDuration: this.form.value.appointmentDuration,
			healthcareProfessionalId: this.form.value.healthcareProfessionalId,
			doctorsOfficeId: this.form.value.doctorOffice.id,

			startDate: momentFormat(this.form.value.startDate, DateFormat.API_DATE),
			endDate: momentFormat(this.form.value.endDate, DateFormat.API_DATE),

			automaticRenewal: this.autoRenew,
			includeHoliday: this.holidayWork,
			professionalAsignShift: this.appointmentManagement,

			diaryOpeningHours: this.mapDiaryOpeningHours()
		};
	}

	private mapDiaryOpeningHours(): DiaryOpeningHoursDto[] {
		const diaryOpeningHours: DiaryOpeningHoursDto[] = [];
		this.newAgendaService.getEvents()
			.filter(event => event.actions)
			.forEach(event => diaryOpeningHours.push(toDiaryOpeningHoursDto(event)));
		return diaryOpeningHours;


		function toDiaryOpeningHoursDto(event: CalendarEvent): DiaryOpeningHoursDto {
			return {
				openingHours: {
					dayWeekId: event.start.getDay(),
					from: momentFormatDate(event.start, DateFormat.HOUR_MINUTE),
					to: momentFormatDate(event.end, DateFormat.HOUR_MINUTE),
				},
				medicalAttentionTypeId: event.meta.medicalAttentionType.id,
				overturnCount: event.meta.overTurnCount
			};
		}
	}

	public setAppointmentDurationToAgendaService() {
		this.newAgendaService.setAppointmentDuration(this.form.value.appointmentDuration);
	}

	public getAllWeeklyDoctorsOfficeOcupation(): void {
		const formValue = this.form.value;
		if (!formValue.doctorOffice || !formValue.startDate || !formValue.endDate) {
			return;
		}

		const startDate = momentFormat(formValue.startDate, DateFormat.API_DATE);
		const endDate = momentFormat(formValue.endDate, DateFormat.API_DATE);
		const weekDays = getCurrentWeek();

		const mappedDays = {};
		weekDays.forEach(day => {
			mappedDays[day.day()] = day;
		});

		this.diaryOpeningHoursService.getAllWeeklyDoctorsOfficeOcupation(formValue.doctorOffice.id, null, startDate, endDate)
			.pipe(
				map((ocupations: OccupationDto[]): CalendarEvent[] => {
					let doctorsOfficeEvents: CalendarEvent[] = [];
					ocupations.forEach(ocupation => {
						const events: CalendarEvent[] = ocupation.timeRanges.map(toCalendarEvents);
						doctorsOfficeEvents = doctorsOfficeEvents.concat(events);

						function toCalendarEvents(timeRange: TimeRangeDto): CalendarEvent {
							const newEvent: CalendarEvent = {
								start: getFullDate(ocupation.id, timeRange.from),
								end: getFullDate(ocupation.id, timeRange.to),
								title: 'Horario ocupado',
								color: {
									primary: '#C8C8C8',
									secondary: '#C8C8C8'
								}
							};

							function getFullDate(dayNumber: number, time: string): Date {
								let dayMoment = mappedDays[dayNumber].clone();
								const dayTime = moment(time, 'HH:mm:ss');
								dayMoment = dayMoment.hour(dayTime.hours());
								dayMoment = dayMoment.minute(dayTime.minutes());
								return new Date(dayMoment);
							}
							return newEvent;
						}
					});
					return doctorsOfficeEvents;
				})
			).subscribe((doctorsOfficeEvents: CalendarEvent[]) => {
				this.newAgendaService.setEvents(doctorsOfficeEvents);
			});

		function getCurrentWeek(): Moment[] {
			const currentDate = moment();

			const weekStart = currentDate.clone().startOf('isoWeek');
			const days = [];

			for (let i = 0; i <= 6; i++) {
				days.push(moment(weekStart).add(i, 'days'));
			}
			return days;
		}
	}
}

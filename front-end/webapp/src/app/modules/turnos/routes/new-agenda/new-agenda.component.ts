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
import { Router, ActivatedRoute } from '@angular/router';
import { APPOINTMENT_DURATIONS } from '../../constants/appointment';
import { NewAgendaService } from '../../services/new-agenda.service';
import { momentFormat, DateFormat, momentParseDateTime, momentFormatDate, momentParseDate, currentWeek } from '@core/utils/moment.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Moment } from 'moment';
import { DoctorsOfficeDto, ProfessionalDto, DiaryADto, DiaryOpeningHoursDto, OccupationDto, TimeRangeDto, DiaryDto, CompleteDiaryDto, OpeningHoursDto } from '@api-rest/api-model';
import * as moment from 'moment';
import { CalendarEvent } from 'angular-calendar';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { DiaryService } from '@api-rest/services/diary.service';
import { map, filter } from 'rxjs/operators';

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
	public appointmentManagement = false;
	public autoRenew = false;
	public holidayWork = false;
	public appointmentDurations = APPOINTMENT_DURATIONS;
	public openingTime: number;
	public closingTime: number;
	public defaultDoctorOffice: DoctorsOfficeDto;
	public editMode = false;
	private editingDiaryId = null;
	private readonly routePrefix;
	private mappedCurrentWeek = {};
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
		private readonly diaryOpeningHoursService: DiaryOpeningHoursService,
		private readonly route: ActivatedRoute,
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
		this.newAgendaService = new NewAgendaService(this.dialog, this.cdr);
	}

	ngOnInit(): void {

		currentWeek().forEach(day => {
			this.mappedCurrentWeek[day.day()] = day;
		});

		this.form = this.formBuilder.group({
			sectorId: [null, [Validators.required]],
			specialtyId: [null, [Validators.required]],
			doctorOffice: [null, [Validators.required]],
			healthcareProfessionalId: [null, [Validators.required]],
			startDate: [null, [Validators.required]],
			endDate: [null, [Validators.required]],
			appointmentDuration: [null, [Validators.required]],
		});

		this.route.data.subscribe(data => {
			if (data.editMode) {
				this.editMode = true;
				this.route.paramMap.subscribe((params) => {
					this.editingDiaryId = Number(params.get('agendaId'));
					this.diaryService.get(this.editingDiaryId).subscribe((diary: CompleteDiaryDto) => {
						this.setValuesFromExistingAgenda(diary);
						this.disableNotEditableControls();
					});

				});
			}
		});
		this.sectorService.getAll().subscribe(data => {
			this.sectors = data;
		});

	}

	private setValuesFromExistingAgenda(diary: CompleteDiaryDto): void {
		this.form.controls.sectorId.setValue(diary.sectorId);
		this.form.controls.specialtyId.setValue(diary.clinicalSpecialtyId);

		this.doctorsOfficeService.getAll(diary.sectorId, diary.clinicalSpecialtyId)
			.subscribe((doctorsOffice: DoctorsOfficeDto[]) => {
				this.doctorOffices = doctorsOffice;
				const office = doctorsOffice.find(office => office.id === diary.doctorsOfficeId);
				this.form.controls.doctorOffice.setValue(office);
				this.setDoctorOfficeRangeTime();
				this.setAllWeeklyDoctorsOfficeOcupation();
			});

		this.healthcareProfessionalService.getAll().subscribe(healthcareProfessionals => {
			this.professionals = healthcareProfessionals;
			const healthcareProfessionalId = healthcareProfessionals.find(professional => professional.id === diary.healthcareProfessionalId);
			this.form.controls.healthcareProfessionalId.setValue(healthcareProfessionalId.id);

		});

		this.form.controls.healthcareProfessionalId.setValue(diary.healthcareProfessionalId);
		this.form.controls.startDate.setValue(momentParseDate(diary.startDate));
		this.form.controls.endDate.setValue(momentParseDate(diary.endDate));
		this.form.controls.appointmentDuration.setValue(diary.appointmentDuration);
		this.newAgendaService.setAppointmentDuration(diary.appointmentDuration);

		this.appointmentManagement = diary.professionalAsignShift;
		this.autoRenew = diary.automaticRenewal;
		this.holidayWork = diary.includeHoliday;

		this.setSpecialties(diary.sectorId);

		const diaryOpeningHours: DiaryOpeningHoursDto[] = diary.diaryOpeningHours;
		diaryOpeningHours.forEach((diaryOpeningHour: DiaryOpeningHoursDto) => {
			const calendarEvent: CalendarEvent = this.toEditableCalendarEvents(diaryOpeningHour);
			this.newAgendaService.setEvents([calendarEvent]);
		});
	}


	private toEditableCalendarEvents(diaryOpeningHour: DiaryOpeningHoursDto): CalendarEvent {
		return {
			start: this.getFullDate(diaryOpeningHour.openingHours.dayWeekId, diaryOpeningHour.openingHours.from),
			end: this.getFullDate(diaryOpeningHour.openingHours.dayWeekId, diaryOpeningHour.openingHours.to),
			title: this.newAgendaService.getMedicalAttentionTypeText(diaryOpeningHour.medicalAttentionTypeId)
				+ this.newAgendaService.getOverturnsText(diaryOpeningHour.overturnCount),
			color: this.newAgendaService.getMedicalAttentionColor(diaryOpeningHour.medicalAttentionTypeId),
			actions: this.newAgendaService.getActions(),
			meta: {
				medicalAttentionType: { id: diaryOpeningHour.medicalAttentionTypeId },
				overturnCount: diaryOpeningHour.overturnCount
			}
		};
	}

	private getFullDate(dayNumber: number, time: string): Date {
		let dayMoment = this.mappedCurrentWeek[dayNumber].clone();
		const dayTime = moment(time, 'HH:mm:ss');
		dayMoment = dayMoment.hour(dayTime.hours());
		dayMoment = dayMoment.minute(dayTime.minutes());
		return new Date(dayMoment);
	}

	private disableNotEditableControls(): void {
		this.form.get('sectorId').disable();
		this.form.get('specialtyId').disable();
		this.form.get('doctorOffice').disable();
		this.form.get('healthcareProfessionalId').disable();
	}

	setSpecialties(sectorId: number): void {
		this.clinicalSpecialtySectorService.getClinicalSpecialty(sectorId).subscribe(data => {
			this.specialties = data;
		});
	}

	setDoctorOffices(sectorId: number, specialtyId: number): void {
		this.doctorsOfficeService.getAll(sectorId, specialtyId)
			.subscribe((data: DoctorsOfficeDto[]) => this.doctorOffices = data);
	}

	setProfessionals(): void {
		//TODO para traer doctores por sector utilizar let sectorId: number = this.form.controls.sectorId.value;
		this.healthcareProfessionalService.getAll().subscribe(data => this.professionals = data);
		this.setDoctorOfficeRangeTime();
		this.setAllWeeklyDoctorsOfficeOcupation();
	}

	private setDoctorOfficeRangeTime() {
		this.openingTime = getHours(this.form.getRawValue().doctorOffice.openingTime);
		this.closingTime = getHours(this.form.getRawValue().doctorOffice.closingTime) - 1; // we don't want to include the declared hour
		this.newAgendaService.setClosingTime(this.closingTime);
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
		const confirmMessage = this.editMode ? 'turnos.new-agenda.CONFIRM_EDIT_AGENDA' : 'turnos.new-agenda.CONFIRM_NEW_AGENDA';
		this.translator.get(confirmMessage).subscribe((res: string) => {
			const dialogRef = this.dialog.open(ConfirmDialogComponent, {
				width: '450px',
				data: {
					title: this.editMode ? 'Editar agenda' : 'Nueva agenda',
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

			appointmentDuration: this.form.getRawValue().appointmentDuration,
			healthcareProfessionalId: this.form.getRawValue().healthcareProfessionalId,
			doctorsOfficeId: this.form.getRawValue().doctorOffice.id,

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

	public setAllWeeklyDoctorsOfficeOcupation(): void {
		const formValue = this.form.getRawValue();
		if (!formValue.doctorOffice || !formValue.startDate || !formValue.endDate) {
			return;
		}

		const startDate = momentFormat(formValue.startDate, DateFormat.API_DATE);
		const endDate = momentFormat(formValue.endDate, DateFormat.API_DATE);

		this.diaryOpeningHoursService.getAllWeeklyDoctorsOfficeOcupation(formValue.doctorOffice.id, this.editingDiaryId, startDate, endDate)
			.pipe(
				map((ocupations: OccupationDto[]): CalendarEvent[] => {
					let doctorsOfficeEvents: CalendarEvent[] = [];
					ocupations.forEach(ocupation => {
						const events: CalendarEvent[] = ocupation.timeRanges
							.map((timeRange: TimeRangeDto) => {
								return {
									start: this.getFullDate(ocupation.id, timeRange.from),
									end: this.getFullDate(ocupation.id, timeRange.to),
									title: 'Horario ocupado',
									color: {
										primary: '#C8C8C8',
										secondary: '#C8C8C8'
									}
								};
							});
						doctorsOfficeEvents = doctorsOfficeEvents.concat(events);
					});
					return doctorsOfficeEvents;
				})
			).subscribe((doctorsOfficeEvents: CalendarEvent[]) => {
				this.newAgendaService.setEvents(doctorsOfficeEvents);
			});
	}
}

import { ChangeDetectorRef, Component, ElementRef, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { DAYS_OF_WEEK } from 'angular-calendar';
import { Observable } from 'rxjs';

import { processErrors, scrollIntoError } from '@core/utils/form.utils';
import { ContextService } from '@core/services/context.service';
import { currentWeek, DateFormat, momentFormat, momentParseDate, momentParseTime } from '@core/utils/moment.utils';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { SectorService } from '@api-rest/services/sector.service';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import {
	CompleteDiaryDto,
	DiaryADto,
	DiaryDto,
	DoctorsOfficeDto,
	OccupationDto,
	ProfessionalDto
} from '@api-rest/api-model';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { DiaryService } from '@api-rest/services/diary.service';
import { APPOINTMENT_DURATIONS, MINUTES_IN_HOUR } from '../../constants/appointment';
import { AgendaHorarioService } from '../../services/agenda-horario.service';

const ROUTE_APPOINTMENT = 'turnos';

@Component({
	selector: 'app-agenda-setup',
	templateUrl: './agenda-setup.component.html',
	styleUrls: ['./agenda-setup.component.scss']
})
export class AgendaSetupComponent implements OnInit {

	readonly MONDAY = DAYS_OF_WEEK.MONDAY;
	readonly TODAY: Date = new Date();
	readonly PIXEL_SIZE_HEIGHT = 30;
	readonly TURN_STARTING_HOUR = 6;

	appointmentDurations = APPOINTMENT_DURATIONS;
	appointmentManagement = false;
	autoRenew = false;
	closingTime: number;
	defaultDoctorOffice: DoctorsOfficeDto;
	doctorOffices: DoctorsOfficeDto[];
	editMode = false;
	errors: string[] = [];
	form: FormGroup;
	holidayWork = false;
	hourSegments: number;
	minDate = new Date();
	openingTime: number;
	professionals: ProfessionalDto[];
	sectors;
	agendaHorarioService: AgendaHorarioService;
	professionalId: string;

	private editingDiaryId = null;
	private readonly routePrefix;
	private mappedCurrentWeek = {};

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly el: ElementRef,
		private readonly sectorService: SectorService,
		private translator: TranslateService,
		private dialog: MatDialog,
		private doctorsOfficeService: DoctorsOfficeService,
		private healthcareProfessionalService: HealthcareProfessionalByInstitutionService,
		private readonly contextService: ContextService,
		private readonly router: Router,
		private readonly cdr: ChangeDetectorRef,
		private readonly diaryService: DiaryService,
		private readonly snackBarService: SnackBarService,
		private readonly diaryOpeningHoursService: DiaryOpeningHoursService,
		private readonly route: ActivatedRoute,

	) {
		this.route.paramMap.subscribe(params => this.professionalId = params.get("idProfessional"));
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
		this.agendaHorarioService = new AgendaHorarioService(this.dialog, this.cdr, this.TODAY, this.MONDAY, snackBarService);
	}

	ngOnInit(): void {

		currentWeek().forEach(day => {
			this.mappedCurrentWeek[day.day()] = day;
		});

		this.form = this.formBuilder.group({
			sectorId: [null, [Validators.required]],
			doctorOffice: [null, [Validators.required]],
			healthcareProfessionalId: [Number(this.professionalId), [Validators.required]],
			startDate: [null, [Validators.required]],
			endDate: [null, [Validators.required]],
			appointmentDuration: [null, [Validators.required]],
		});

		this.form.controls.appointmentDuration.valueChanges
			.subscribe(newDuration => this.hourSegments = MINUTES_IN_HOUR / newDuration);

		this.route.data.subscribe(data => {
			if (data.editMode) {
				this.editMode = true;
				this.route.paramMap.subscribe((params) => {
					this.editingDiaryId = Number(params.get('agendaId'));
					this.diaryService.get(this.editingDiaryId).subscribe((diary: CompleteDiaryDto) => {
						this.minDate = momentParseDate(diary.startDate).toDate();
						this.setValuesFromExistingAgenda(diary);
						this.disableNotEditableControls();
					});

				});
			}
		});
		this.sectorService.getAll().subscribe(data => {
			this.sectors = data;
		});

		this.healthcareProfessionalService.getAll().subscribe(data => this.professionals = data);

	}

	private setValuesFromExistingAgenda(diary: CompleteDiaryDto): void {
		this.form.controls.sectorId.setValue(diary.sectorId);

		this.doctorsOfficeService.getAll(diary.sectorId)
			.subscribe((doctorsOffice: DoctorsOfficeDto[]) => {
				this.doctorOffices = doctorsOffice;
				const office = doctorsOffice.find(o => o.id === diary.doctorsOfficeId);
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
		this.agendaHorarioService.setAppointmentDuration(diary.appointmentDuration);

		this.appointmentManagement = diary.professionalAssignShift;
		this.autoRenew = diary.automaticRenewal;
		this.holidayWork = diary.includeHoliday;

		this.agendaHorarioService.setDiaryOpeningHours(diary.diaryOpeningHours);
	}

	private disableNotEditableControls(): void {
		this.form.get('sectorId').disable();
		this.form.get('doctorOffice').disable();
		this.form.get('healthcareProfessionalId').disable();
		this.form.get('appointmentDuration').disable();
	}

	setDoctorOfficesAndResetFollowingControls(sectorId: number): void {
		this.doctorsOfficeService.getAll(sectorId).subscribe((data: DoctorsOfficeDto[]) => this.doctorOffices = data);
		this.form.get('doctorOffice').reset();
	}

	loadCalendar(): void {
		this.setDoctorOfficeRangeTime();
		this.setAllWeeklyDoctorsOfficeOcupation();
	}

	private setDoctorOfficeRangeTime() {
		this.openingTime = getHours(this.form.getRawValue().doctorOffice.openingTime);
		this.closingTime = getHours(this.form.getRawValue().doctorOffice.closingTime) - 1;
		function getHours(time: string): number {
			const hours = momentParseTime(time);
			return Number(hours.hours());
		}
	}

	getFullNameLicence(professional: ProfessionalDto): string {
		return `${professional.lastName}, ${professional.firstName} - ${professional.licenceNumber}`;
	}

	appointmentManagementChange(): void {
		this.appointmentManagement = !this.appointmentManagement;
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
		const confirmMessage = this.editMode ? 'turnos.agenda-setup.CONFIRM_EDIT_AGENDA' : 'turnos.agenda-setup.CONFIRM_NEW_AGENDA';
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
					this.errors = [];
					if (this.editMode) {
						const agendaEdit: DiaryDto = this.addAgendaId(this.buildDiaryADto());
						this.diaryService.updateDiary(agendaEdit)
							.subscribe((agendaId: number) => {
								this.processSuccess(agendaId);
							}, error => processErrors(error, (msg) => this.errors.push(msg)));
					} else {
						const agenda: DiaryADto = this.buildDiaryADto();
						this.diaryService.addDiary(agenda)
							.subscribe((agendaId: number) => {
								this.processSuccess(agendaId);
							}, error => processErrors(error, (msg) => this.errors.push(msg)));
					}
				}
			});
		});
	}

	setAllWeeklyDoctorsOfficeOcupation(): void {
		if (!this.form.controls['startDate'].valid && !this.form.controls['endDate'].valid) {
			return;
		}
		const formValue = this.form.getRawValue();
		if (!formValue.doctorOffice || !formValue.startDate || !formValue.endDate) {
			return;
		}

		const startDate = momentFormat(formValue.startDate, DateFormat.API_DATE);
		const endDate = momentFormat(formValue.endDate, DateFormat.API_DATE);

		const ocupations$: Observable<OccupationDto[]> = this.diaryOpeningHoursService
			.getAllWeeklyDoctorsOfficeOcupation(formValue.doctorOffice.id, this.editingDiaryId, startDate, endDate);

		this.agendaHorarioService.setWeeklyDoctorsOfficeOcupation(ocupations$);
	}

	private processSuccess(agendaId: number) {
		if (agendaId) {
			this.snackBarService.showSuccess('turnos.agenda-setup.messages.SUCCESS');
			const url = `${this.routePrefix}${ROUTE_APPOINTMENT}`;
			this.router.navigate([url]);
		}
	}

	private addAgendaId(diary: any): DiaryDto {
		diary.id = this.editingDiaryId;
		return diary;
	}

	private buildDiaryADto(): DiaryADto {
		return {

			appointmentDuration: this.form.getRawValue().appointmentDuration,
			healthcareProfessionalId: this.form.getRawValue().healthcareProfessionalId,
			doctorsOfficeId: this.form.getRawValue().doctorOffice.id,

			startDate: momentFormat(this.form.value.startDate, DateFormat.API_DATE),
			endDate: momentFormat(this.form.value.endDate, DateFormat.API_DATE),

			automaticRenewal: this.autoRenew,
			includeHoliday: this.holidayWork,
			professionalAssignShift: this.appointmentManagement,

			diaryOpeningHours: this.agendaHorarioService.getDiaryOpeningHours()
		};
	}

	scrollToDefaultStartingHour(): void{
		this.agendaHorarioService.setAppointmentDuration(this.form.getRawValue().appointmentDuration);

		const scrollbar = document.getElementsByClassName('cal-time-events')[0];
		scrollbar?.scrollTo(0, this.PIXEL_SIZE_HEIGHT * this.TURN_STARTING_HOUR * this.hourSegments);
	}

}

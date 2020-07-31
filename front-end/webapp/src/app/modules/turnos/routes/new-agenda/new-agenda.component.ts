import { Component, ElementRef, OnInit, Input, ChangeDetectorRef } from '@angular/core';
import { SectorService } from "@api-rest/services/sector.service";
import { ClinicalSpecialtySectorService } from "@api-rest/services/clinical-specialty-sector.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { scrollIntoError } from "@core/utils/form.utils";
import { ConfirmDialogComponent } from "@core/dialogs/confirm-dialog/confirm-dialog.component";
import { TranslateService } from "@ngx-translate/core";
import { MatDialog } from "@angular/material/dialog";
import { DoctorsOfficeService } from "@api-rest/services/doctors-office.service";
import { HealthcareProfessionalService } from "@api-rest/services/healthcare-professional.service";
import { ContextService } from "@core/services/context.service";
import { Router } from "@angular/router";
import { APPOINTMENT_DURATIONS } from "../../constants/appointment";
import { NewAgendaService } from '../../services/new-agenda.service';
import { DAYS_OF_WEEK, CalendarEvent } from 'angular-calendar';
import { DoctorsOfficeDto, DiaryADto, DiaryOpeningHoursDto, ProfessionalDto } from '@api-rest/api-model';
import * as moment from 'moment';
import { DiaryService } from '@api-rest/services/diary.service';
import { momentFormat, DateFormat } from '@core/utils/moment.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';

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

	public readonly MINUTES_IN_HOUR = 60;

	private readonly routePrefix;

	newAgendaService: NewAgendaService;
	public weekStartsOn = DAYS_OF_WEEK.MONDAY;
	public viewDate: Date = new Date();
	public events: CalendarEvent[] = [];

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
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
		this.newAgendaService = new NewAgendaService(this.weekStartsOn, this.viewDate, this.events, this.dialog, this.cdr);
	}

	ngOnInit(): void {

		this.form = this.formBuilder.group({
			sectorId: [null, [Validators.required]],
			specialtyId: [{ value: null, disabled: true }, [Validators.required]],
			doctorOffice: [{ value: null, disabled: true }, [Validators.required]],
			healthcareProfessionalId: [{ value: null, disabled: true }, [Validators.required]],
			startDate: [{ value: null, disabled: true }, [Validators.required]],
			endDate: [{ value: null, disabled: true }, [Validators.required]],
			appointmentDuration: [{ value: null, disabled: true }, [Validators.required]],
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

	startDateChange(event): void {
		this.form.controls.endDate.enable();
		//TODO set fecha minima del endDate a partir de startDate
	}

	private enableFormControl(controlName): void {
		this.form.get(controlName).reset();
		this.form.get(controlName).enable();
	}

	endDateChange(event): void {
		//TODO llamar servicio  para disponibilidad de consultorio a partir de las fechas
		//TODO validaciones de fechas
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
		let agenda: DiaryADto;
		agenda = this.form.value;
		agenda.doctorsOfficeId = this.form.value.doctorOffice.id;

		agenda.startDate = momentFormat(this.form.value.startDate, DateFormat.API_DATE);
		agenda.endDate = momentFormat(this.form.value.endDate, DateFormat.API_DATE);

		agenda.automaticRenewal = this.autoRenew;
		agenda.includeHoliday = this.holidayWork;
		agenda.professionalAsignShift = this.appointmentManagement;

		agenda.diaryOpeningHours = this.mapDiaryOpeningHours();

		return agenda;

	}

	private mapDiaryOpeningHours(): DiaryOpeningHoursDto[] {
		const diaryOpeningHours: DiaryOpeningHoursDto[] = [];
		this.newAgendaService.getEvents().
			forEach(event => diaryOpeningHours.push(toDiaryOpeningHoursDto(event)));
		return diaryOpeningHours;


		function toDiaryOpeningHoursDto(event: CalendarEvent): DiaryOpeningHoursDto {
			return {
				openingHours: {
					dayWeekId: event.start.getDay(),
					from: event.start.toLocaleTimeString(),
					to: event.end.toLocaleTimeString(),
				},
				medicalAttentionTypeId: event.meta.medicalAttentionType.id,
				overturnCount: event.meta.overTurnCount
			};
		}
	}

	public setAppointmentDurationToAgendaService() {
		this.newAgendaService.setAppointmentDuration(this.form.value.appointmentDuration);
	}

}

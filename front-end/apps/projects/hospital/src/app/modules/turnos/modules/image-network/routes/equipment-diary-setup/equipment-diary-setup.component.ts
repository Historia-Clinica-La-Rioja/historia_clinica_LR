import { ChangeDetectorRef, Component, ElementRef, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { DAYS_OF_WEEK } from 'angular-calendar';
import { Observable } from 'rxjs';
import { getError, hasError, processErrors, scrollIntoError } from '@core/utils/form.utils';
import { ContextService } from '@core/services/context.service';
import { currentWeek, DateFormat, momentFormat } from '@core/utils/moment.utils';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { SectorService } from '@api-rest/services/sector.service';
import { EquipmentDiaryService } from '@api-rest/services/equipment-diary.service';
import { APPOINTMENT_DURATIONS, MINUTES_IN_HOUR } from '@turnos/constants/appointment';
import { AgendaHorarioService } from '@turnos/services/agenda-horario.service';
import { EquipmentService } from '@api-rest/services/equipment.service';
import { EquipmentDiaryOpeningHoursService } from '@api-rest/services/equipment-diary-opening-hours.service';
import { EquipmentDiaryADto, EquipmentDto, SectorDto } from '@api-rest/api-model';

const ROUTE_APPOINTMENT = 'turnos';
const START = 0;
const END = 23;
const DIAGNOSTIC_IMAGING = "Diagnóstico por imagen";

@Component({
	selector: 'app-equipment-diary-setup',
	templateUrl: './equipment-diary-setup.component.html',
	styleUrls: ['./equipment-diary-setup.component.scss']
})
export class EquipmentDiarySetupComponent implements OnInit {

	readonly MONDAY = DAYS_OF_WEEK.MONDAY;
	readonly TODAY: Date = new Date();
	readonly PIXEL_SIZE_HEIGHT = 30;
	readonly APPOINTMENT_STARTING_HOUR = 6;

	appointmentDurations = APPOINTMENT_DURATIONS;
	autoRenew = false;
	closingTime: number;

	errors: string[] = [];
	form: FormGroup;
	holidayWork = false;
	hourSegments: number;
	minDate = new Date();
	openingTime: number;

	sectors$: Observable<SectorDto[]>;
	agendaHorarioService: AgendaHorarioService;
	equipments$: Observable<EquipmentDto[]>;

	hasError = hasError;
	getError = getError;

	private readonly routePrefix;
	private mappedCurrentWeek = {};

	editMode = false;
	editingDiaryId:number;

	constructor(
		private readonly el: ElementRef,
		private readonly sectorService: SectorService,
		private translator: TranslateService,
		private dialog: MatDialog,
		private readonly contextService: ContextService,
		private readonly router: Router,
		private readonly cdr: ChangeDetectorRef,
		private readonly equipmentDiaryService: EquipmentDiaryService,
		private readonly snackBarService: SnackBarService,
		private readonly equipmentDiaryOpeningHoursService: EquipmentDiaryOpeningHoursService,
		private readonly equipmentService: EquipmentService,
		private readonly formBuilder: FormBuilder,
		private readonly route: ActivatedRoute
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
		this.agendaHorarioService = new AgendaHorarioService(this.dialog, this.cdr, this.TODAY, this.MONDAY, snackBarService);
	}

	ngOnInit(): void {

		this.route.data.subscribe(data => {
			if (data.editMode) {
				this.editMode = true;
				this.route.paramMap.subscribe((params) => {
					this.editingDiaryId = Number(params.get('agendaId'));
				});
			}
		});

		currentWeek().forEach(day => {
			this.mappedCurrentWeek[day.day()] = day;
		});

		this.form = this.formBuilder.group({
			sectorId: [null, [Validators.required]],
			equipmentId: [null, [Validators.required]],
			startDate: [null, [Validators.required]],
			endDate: [null, [Validators.required]],
			appointmentDuration: [null, [Validators.required]],
		});

		this.form.controls.appointmentDuration.valueChanges
			.subscribe(newDuration => this.hourSegments = MINUTES_IN_HOUR / newDuration);

		this.sectorService.getTypes().subscribe(types => {
			const diagnosticImagingId = types.find(type => type.description === DIAGNOSTIC_IMAGING).id;
			this.sectors$ = this.sectorService.getDiagnosticImagingType(diagnosticImagingId);
		});
	}

	loadCalendar() {
		this.setRangeTime();
		this.setAllWeeklyEquipmentOcupation();
	}

	save() {
		if (this.form.valid) {
			this.openDialog();
		} else {
			scrollIntoError(this.form, this.el);
		}
	}

	setAllWeeklyEquipmentOcupation() {
		if (!this.form.controls['startDate'].valid && !this.form.controls['endDate'].valid) {
			return;
		}

		const formValue = this.form.getRawValue();
		if (!formValue.equipmentId || !formValue.startDate || !formValue.endDate) {
			return;
		}

		const startDate: string = momentFormat(formValue.startDate, DateFormat.API_DATE);
		const endDate: string = momentFormat(formValue.endDate, DateFormat.API_DATE);

		const ocupations$: Observable<any[]> = this.equipmentDiaryOpeningHoursService
			.getAllWeeklyEquipmentOcupation(this.form.value.equipmentId, null, startDate, endDate);

		this.agendaHorarioService.setWeeklyOcupation(ocupations$);
	}

	scrollToDefaultStartingHour() {
		this.agendaHorarioService.setAppointmentDuration(this.form.value.appointmentDuration);

		const scrollbar = document.getElementsByClassName('cal-time-events')[0];
		scrollbar?.scrollTo(0, this.PIXEL_SIZE_HEIGHT * this.APPOINTMENT_STARTING_HOUR * this.hourSegments);
	}

	setEquipmentsBySector() {
		this.form.controls.equipmentId.reset();
		this.equipments$ = this.equipmentService.getBySectorId(this.form.value.sectorId);
	}

	private openDialog() {
		const confirmMessage = 'turnos.agenda-setup.CONFIRM_NEW_AGENDA';
		this.translator.get(confirmMessage).subscribe((res: string) => {
			const dialogRef = this.dialog.open(ConfirmDialogComponent, {
				width: '450px',
				data: {
					title: 'turnos.agenda-setup.NEW_AGENDA',
					content: `${res}`,
					okButtonLabel: 'buttons.CONFIRM'
				}
			});

			dialogRef.afterClosed().subscribe(confirmed => {
				if (confirmed) {
					this.errors = [];
					const diary: EquipmentDiaryADto = this.buildEquipmentDiaryDto();
					this.equipmentDiaryService.addEquipmentDiary(diary)
						.subscribe((diaryId: number) => {
							this.processSuccess(diaryId);
						}, error => processErrors(error, (msg) => this.errors.push(msg)));
				}
			});
		});
	}

	private processSuccess(agendaId: number) {
		if (agendaId) {
			this.snackBarService.showSuccess('turnos.agenda-setup.messages.SUCCESS');
			const url = `${this.routePrefix}${ROUTE_APPOINTMENT}`;
			this.router.navigate([url]);
		}
	}

	private buildEquipmentDiaryDto(): EquipmentDiaryADto {
		return {
			appointmentDuration: this.form.value.appointmentDuration,
			startDate: momentFormat(this.form.value.startDate, DateFormat.API_DATE),
			endDate: momentFormat(this.form.value.endDate, DateFormat.API_DATE),
			automaticRenewal: this.autoRenew,
			includeHoliday: this.holidayWork,
			equipmentDiaryOpeningHours: this.agendaHorarioService.getDiaryOpeningHours(),
			equipmentId: this.form.value.equipmentId
		};
	}

	private setRangeTime() {
		this.openingTime = START;
		this.closingTime = END;
	}

}

import { ChangeDetectorRef, Component, ElementRef, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { DAYS_OF_WEEK } from 'angular-calendar';
import { Observable } from 'rxjs';
import { getError, hasError, processErrors, scrollIntoError } from '@core/utils/form.utils';
import { ContextService } from '@core/services/context.service';
import { dateISOParseDate } from '@core/utils/moment.utils';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { SectorService } from '@api-rest/services/sector.service';
import { EquipmentDiaryService } from '@api-rest/services/equipment-diary.service';
import { APPOINTMENT_DURATIONS, MINUTES_IN_HOUR } from '@turnos/constants/appointment';
import { AgendaHorarioService, EDiaryType } from '@turnos/services/agenda-horario.service';
import { EquipmentService } from '@api-rest/services/equipment.service';
import { EquipmentDiaryOpeningHoursService } from '@api-rest/services/equipment-diary-opening-hours.service';
import { CompleteEquipmentDiaryDto, EquipmentDiaryADto, EquipmentDiaryDto, EquipmentDto, SectorDto } from '@api-rest/api-model';
import { TabsLabel } from '@turnos/constants/tabs';
import { toApiFormat } from '@api-rest/mapper/date.mapper';

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
	form: UntypedFormGroup;
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

	editMode = false;
	editingDiaryId: number;
	diaryStartDate: Date;
	diaryEndDate: Date;

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
		private readonly formBuilder: UntypedFormBuilder,
		private readonly route: ActivatedRoute
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
		this.agendaHorarioService = new AgendaHorarioService(this.dialog, this.cdr, this.TODAY, this.MONDAY, snackBarService, EDiaryType.EQUIPMENT);
	}

	ngOnInit(): void {

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
			this.sectors$ = this.sectorService.getAllSectorByType(diagnosticImagingId);
		});

		this.route.data.subscribe(data => {
			if (data["editMode"]) {
				this.editMode = true;
				this.route.paramMap.subscribe((params) => {
					this.editingDiaryId = Number(params.get('agendaId'));
					this.equipmentDiaryService.getBy(this.editingDiaryId).subscribe((diary: CompleteEquipmentDiaryDto) => {
						this.minDate = dateISOParseDate(diary.startDate);
						this.setValuesFromExistingAgenda(diary);
					})
				});
			}
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

		const startDate: string = toApiFormat(formValue.startDate);
		const endDate: string = toApiFormat(formValue.endDate);

		const ocupations$: Observable<any[]> = this.equipmentDiaryOpeningHoursService
			.getAllWeeklyEquipmentOcupation(this.form.controls.equipmentId.value, this.editingDiaryId, startDate, endDate);
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
		let confirmMessage: string;
		let title: string;
		if (this.editMode) {
			confirmMessage = 'image-network.form.CONFIRM_EDIT_DIARY';
			title = 'image-network.form.EDIT_DIARY';
		} else {
			confirmMessage = 'image-network.form.CONFIRM_NEW_DIARY';
			title = 'image-network.form.NEW_DIARY';
		}
		this.translator.get(confirmMessage).subscribe((res: string) => {
			const dialogRef = this.dialog.open(ConfirmDialogComponent, {
				width: '450px',
				data: {
					title: title,
					content: `${res}`,
					okButtonLabel: 'buttons.CONFIRM'
				}
			});

			dialogRef.afterClosed().subscribe(confirmed => {
				if (confirmed) {
					this.errors = [];
					const diary: EquipmentDiaryADto = this.buildEquipmentDiaryDto();
					if (this.editMode) {
						this.equipmentDiaryService.updateEquipmentDiary(diary, this.editingDiaryId)
							.subscribe((diaryId: number) => {
								this.processSuccess(diaryId);
							}, error => processErrors(error, (msg) => this.errors.push(msg)))
					} else {
						this.equipmentDiaryService.addEquipmentDiary(diary)
							.subscribe((diaryId: number) => {
								this.processSuccess(diaryId);
							}, error => processErrors(error, (msg) => this.errors.push(msg)));
					}

				}
			});
		});
	}

	private processSuccess(agendaId: number) {
		if (agendaId) {
			this.snackBarService.showSuccess('turnos.agenda-setup.messages.SUCCESS');
			const url = `${this.routePrefix}${ROUTE_APPOINTMENT}`;

			let selectedEquipment = window.history.state.selectedEquipment || {id: this.form.get('equipmentId').value};
			let selectedDiary = window.history.state.selectedDiary;

			if (!window.history.state.selectedDiary) {
				this.equipmentDiaryService.getBy(agendaId).subscribe(agenda => {
					selectedDiary = agenda;
					this.router.navigate([url], { state: { tab: TabsLabel.IMAGE_NETWORK, selectedEquipment, selectedDiary} });
				});
			} else {
				this.router.navigate([url], { state: { tab: TabsLabel.IMAGE_NETWORK, selectedEquipment, selectedDiary} });
			}
		}
	}

	private buildEquipmentDiaryDto(): EquipmentDiaryDto {
		return {
			id: this.editingDiaryId,
			appointmentDuration: this.form.controls.appointmentDuration.value,
			startDate: toApiFormat(this.form.controls.startDate.value),
			endDate: toApiFormat(this.form.controls.endDate.value),
			automaticRenewal: this.autoRenew,
			includeHoliday: this.holidayWork,
			equipmentDiaryOpeningHours: this.agendaHorarioService.getDiaryOpeningHours(),
			equipmentId: this.form.controls.equipmentId.value,
		};
	}

	private setRangeTime() {
		this.openingTime = START;
		this.closingTime = END;
	}

	setValuesFromExistingAgenda(diary: CompleteEquipmentDiaryDto) {
		this.form.controls.sectorId.setValue(diary.sectorId);
		this.setEquipmentsBySector();
		this.form.controls.equipmentId.setValue(diary.equipmentId);
		this.diaryStartDate = dateISOParseDate(diary.startDate);
		this.diaryEndDate = dateISOParseDate(diary.endDate);
		this.loadCalendar();
		this.form.controls.appointmentDuration.setValue(diary.appointmentDuration);

		this.disableNotEditableControls();
		this.agendaHorarioService.setAppointmentDuration(diary.appointmentDuration);
		this.agendaHorarioService.setDiaryOpeningHours(diary.equipmentDiaryOpeningHours);

	}

	setSelectedStartDate(selectDate: Date) {
		this.form.controls.startDate.setValue(selectDate);
		this.setAllWeeklyEquipmentOcupation();
	}

	setSelectedEndDate(selectDate: Date) {
		this.form.controls.endDate.setValue(selectDate);
		this.setAllWeeklyEquipmentOcupation();
	}

	private disableNotEditableControls() {
		this.form.get('sectorId').disable();
		this.form.get('equipmentId').disable();
		this.form.get('appointmentDuration').disable();

	}
}

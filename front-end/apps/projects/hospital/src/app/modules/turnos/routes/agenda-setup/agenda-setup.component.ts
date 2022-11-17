import { ChangeDetectorRef, Component, ElementRef, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { DAYS_OF_WEEK } from 'angular-calendar';
import { Observable } from 'rxjs';

import { getError, hasError, processErrors, scrollIntoError } from '@core/utils/form.utils';
import { ContextService } from '@core/services/context.service';
import { currentWeek, DateFormat, momentFormat, momentParseDate, momentParseTime } from '@core/utils/moment.utils';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { SectorService } from '@api-rest/services/sector.service';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import {
	CareLineDto,
	CompleteDiaryDto,
	DiaryADto,
	DiaryDto,
	DoctorsOfficeDto,
	OccupationDto,
	ProfessionalDto,
} from '@api-rest/api-model';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { DiaryService } from '@api-rest/services/diary.service';
import { APPOINTMENT_DURATIONS, MINUTES_IN_HOUR } from '../../constants/appointment';
import { AgendaHorarioService } from '../../services/agenda-horario.service';
import { PatientNameService } from "@core/services/patient-name.service";
import { SpecialtyService } from '@api-rest/services/specialty.service';
import { CareLineService } from '@api-rest/services/care-line.service';

const ROUTE_APPOINTMENT = 'turnos';
const MAX_INPUT= 100;
const PATTERN = /^[0-9]\d*$/;

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
	professionalSpecialties: any[];
	hasError = hasError;
	getError = getError;
	careLines: CareLineDto[] = [];
	careLinesSelected: CareLineDto[] = [];
	private editingDiaryId = null;
	private readonly routePrefix;
	private mappedCurrentWeek = {};

	constructor(
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
		private readonly patientNameService: PatientNameService,
		private readonly specialtyService: SpecialtyService,
		private readonly carelineService: CareLineService
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
		this.agendaHorarioService = new AgendaHorarioService(this.dialog, this.cdr, this.TODAY, this.MONDAY, snackBarService);
	}

	ngOnInit(): void {

		currentWeek().forEach(day => {
			this.mappedCurrentWeek[day.day()] = day;
		});

		this.form = new FormGroup({
			sectorId: new FormControl(null, [Validators.required]),
			doctorOffice: new FormControl(null, [Validators.required]),
			healthcareProfessionalId: new FormControl(null, [Validators.required]),
			startDate: new FormControl(null, [Validators.required]),
			endDate: new FormControl(null, [Validators.required]),
			appointmentDuration: new FormControl(null, [Validators.required]),
			healthcareProfessionalSpecialtyId: new FormControl(null, [Validators.required]),
			conjointDiary: new FormControl(false, [Validators.nullValidator]),
			alias: new FormControl(null, [Validators.nullValidator]),
			otherProfessionals: new FormArray([], [this.otherPossibleProfessionals()]),
			protectedAppointmentsPercentage: new FormControl(null, [Validators.pattern(PATTERN), Validators.max(MAX_INPUT)]),
			careLines: new FormControl([null])
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

	get careLinesAssociated(): FormControl {
		return this.form.get('careLines') as FormControl;
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
			this.specialtyService.getAllSpecialtyByProfessional(this.contextService.institutionId, healthcareProfessionalId.id)
				.subscribe(response => {
					this.professionalSpecialties = response;
					this.form.controls.healthcareProfessionalSpecialtyId.markAsTouched();
					if (this.professionalSpecialties.find(specialty => specialty.id === diary.clinicalSpecialtyId)) {
						this.form.controls.healthcareProfessionalSpecialtyId.setValue(diary.clinicalSpecialtyId);
						this.getCareLines();
					}
				})
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

		if (diary.associatedProfessionalsInfo.length > 0) {
			const professionalsReference = this.form.controls.otherProfessionals as FormArray;
			this.form.controls.conjointDiary.setValue(true);
			diary.associatedProfessionalsInfo.forEach(diaryAssociatedProfessional => {
				professionalsReference.push(this.initializeAnotherProfessional());
				professionalsReference.controls[professionalsReference.length-1].setValue({ healthcareProfessionalId: diaryAssociatedProfessional.id });
			});
		}

		if (diary.alias) {
			this.form.controls.alias.setValue(diary.alias);
		}

		if (diary.protectedAppointmentsPercentage)
			this.form.controls.protectedAppointmentsPercentage.setValue(diary.protectedAppointmentsPercentage);

		if (diary.careLinesInfo.length)
			this.careLinesSelected = diary.careLinesInfo;
	}

	private disableNotEditableControls(): void {
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
		this.closingTime = getHours(this.form.getRawValue().doctorOffice.closingTime);
		function getHours(time: string): number {
			const hours = momentParseTime(time);
			return Number(hours.hours());
		}
	}

	getFullName(professional: ProfessionalDto): string {
		return `${professional.lastName}, ${this.patientNameService.getPatientName(professional.firstName, professional.nameSelfDetermination)}`;
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
		const percentage = this.form.value.protectedAppointmentsPercentage;
		return {

			appointmentDuration: this.form.getRawValue().appointmentDuration,
			healthcareProfessionalId: this.form.getRawValue().healthcareProfessionalId,
			doctorsOfficeId: this.form.getRawValue().doctorOffice.id,

			startDate: momentFormat(this.form.value.startDate, DateFormat.API_DATE),
			endDate: momentFormat(this.form.value.endDate, DateFormat.API_DATE),

			automaticRenewal: this.autoRenew,
			includeHoliday: this.holidayWork,
			professionalAssignShift: this.appointmentManagement,

			diaryOpeningHours: this.agendaHorarioService.getDiaryOpeningHours(),
			clinicalSpecialtyId: this.form.value.healthcareProfessionalSpecialtyId,
			alias: this.form.value.alias === "" ? null : this.form.value.alias,
			diaryAssociatedProfessionalsId: this.form.value.otherProfessionals.map(professional => professional.healthcareProfessionalId),
			careLines: this.careLinesSelected.map(careLine => { return careLine.id }),
			protectedAppointmentsPercentage: percentage ? percentage : 0
		};
	}

	scrollToDefaultStartingHour(): void {
		this.agendaHorarioService.setAppointmentDuration(this.form.getRawValue().appointmentDuration);

		const scrollbar = document.getElementsByClassName('cal-time-events')[0];
		scrollbar?.scrollTo(0, this.PIXEL_SIZE_HEIGHT * this.TURN_STARTING_HOUR * this.hourSegments);
	}

	getProfessionalSpecialties() {
		this.resetValues();
		this.specialtyService.getAllSpecialtyByProfessional(this.contextService.institutionId, this.form.get("healthcareProfessionalId").value)
			.subscribe(response => this.professionalSpecialties = response)
	}

	getControl(key: string): any {
		return this.form.get(key);
	}

	addAssociatedProfessional() {
		const currentOtherProfessionals = this.form.controls.otherProfessionals as FormArray;
		currentOtherProfessionals.push(this.initializeAnotherProfessional());
	}

	private initializeAnotherProfessional(): FormGroup {
		return new FormGroup({
			healthcareProfessionalId: new FormControl(null, [Validators.required]),
		});
	}

	clear(i: number): void {
		const professionalsReference = this.form.controls.otherProfessionals as FormArray;
		if (professionalsReference.length === 1) {
			professionalsReference.controls[0].setValue({ healthcareProfessionalId: null });
		}
		else {
			professionalsReference.removeAt(i);
		}
	}

	updateAssociatedProfessionalsForm() {
		let professionalsReference = this.form.controls.otherProfessionals as FormArray;
		if (this.form.value.conjointDiary === true) {
			professionalsReference.push(this.initializeAnotherProfessional());
		}
		else {
			professionalsReference.clear();
			this.form.controls.conjointDiary.setValue(false);
		}
	}

	private otherPossibleProfessionals(): ValidatorFn {
		return (control: FormArray): ValidationErrors | null => {
			return control.valid ? null : { validProfessionals: { valid: false } };
		};
	}

	validExtraProfessionalsList(): boolean {
		const professionalsReference = this.form.controls.otherProfessionals as FormArray;
		return professionalsReference.valid;
	}

	isProfessionalNonSelectable(professionalId: number): boolean {
		const professionalsReference = this.form.controls.otherProfessionals as FormArray;
		return professionalsReference.value.map(professional => professional.healthcareProfessionalId).includes(professionalId);
	}

	getNonResponsibleProfessional(): ProfessionalDto[] {
		return this.professionals?.filter(professional => professional.id !== this.form.controls.healthcareProfessionalId.value);
	}

	getCareLines() {
		const specialtyId = this.form.get("healthcareProfessionalSpecialtyId").value;
		this.carelineService.getCareLinesBySpecialty(specialtyId).subscribe(careLines => {
			this.careLines = careLines;
			this.checkCareLinesSelected();
		});
	}

	setCareLines(careLines: string[]) {
		if (careLines.length) {
			if (!this.form.controls.protectedAppointmentsPercentage.hasValidator(Validators.required)) {
				this.addValidators();
			}

			this.careLinesSelected = careLines.map(careLine => ({
				id: this.careLines.find(c => c.description === careLine).id,
				description: careLine,
				clinicalSpecialties: this.careLines.find(c => c.description === careLine).clinicalSpecialties
			}));
		}
		else {
			this.form.controls.protectedAppointmentsPercentage.removeValidators([Validators.required]);
			this.careLinesSelected = [];
		}
		this.form.controls.protectedAppointmentsPercentage.updateValueAndValidity();
	}

	private checkCareLinesSelected() {
		const careLinesToSelect: CareLineDto[] = [];
		this.careLinesSelected.forEach(careLineSelected => {
			if (this.careLines.some(careLine => careLine.id === careLineSelected.id)) {
				careLinesToSelect.push(careLineSelected);
			}
		});
		this.careLinesSelected = careLinesToSelect;
		const careLinesDescription = this.careLinesSelected.map(careLine => careLine.description);
		this.form.controls.careLines.patchValue(careLinesDescription);
	}

	private addValidators() {
		this.form.controls.protectedAppointmentsPercentage.addValidators([Validators.required]);
		this.form.controls.protectedAppointmentsPercentage.markAsTouched();
	}

	private resetValues() {
		this.form.controls.healthcareProfessionalSpecialtyId.reset();
		this.form.controls.careLines.reset();
		this.form.controls.protectedAppointmentsPercentage.removeValidators([Validators.required]);
		this.form.controls.protectedAppointmentsPercentage.updateValueAndValidity();
	}
}

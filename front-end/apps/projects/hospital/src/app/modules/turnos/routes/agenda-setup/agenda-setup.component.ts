import { ChangeDetectorRef, Component, ElementRef, OnInit } from '@angular/core';
import { UntypedFormArray, UntypedFormControl, UntypedFormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { SECTOR_AMBULATORIO } from "@historia-clinica/modules/guardia/constants/masterdata";
import { TranslateService } from '@ngx-translate/core';
import { DAYS_OF_WEEK } from 'angular-calendar';
import { Observable, filter, finalize, map, switchMap } from 'rxjs';

import { getError, hasError, processErrors, scrollIntoError } from '@core/utils/form.utils';
import { ContextService } from '@core/services/context.service';
import { dateISOParseDate, dateParseTime } from '@core/utils/moment.utils';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { SectorService } from '@api-rest/services/sector.service';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import {
	ApiErrorDto,
	AppFeature,
	CareLineDto,
	CompleteDiaryDto,
	DiaryADto,
	DiaryBookingRestrictionDto,
	DiaryDto,
	DiaryLabelDto,
	DoctorsOfficeDto,
	EDiaryBookingRestrictionType,
	HierarchicalUnitDto,
	OccupationDto,
	ProfessionalDto, SectorDto,
	SnomedDto
} from '@api-rest/api-model';
import { DiaryOpeningHoursService } from '@api-rest/services/diary-opening-hours.service';
import { DiaryService } from '@api-rest/services/diary.service';
import { APPOINTMENT_DURATIONS, COLOR, DIARY_LABEL_COLORS, MINUTES_IN_HOUR } from '../../constants/appointment';
import { AgendaHorarioService, EDiaryType } from '../../services/agenda-horario.service';
import { PatientNameService } from "@core/services/patient-name.service";
import { SpecialtyService } from '@api-rest/services/specialty.service';
import { HierarchicalUnitsService } from '@api-rest/services/hierarchical-units.service';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { DiaryCareLineService } from '@api-rest/services/diary-care-line.service';
import { PracticesService } from '@api-rest/services/practices.service';
import { ChipsOption } from '@presentation/components/chips-autocomplete/chips-autocomplete.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { ButtonType } from '@presentation/components/button/button.component';

const ROUTE_APPOINTMENT = 'turnos';
const ROUTE_AGENDAS = "agenda";
const MAX_INPUT = 100;
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

	CONSULTATION = true;
	PRACTICE = false;

	appointmentDurations = APPOINTMENT_DURATIONS;
	appointmentManagement = false;
	autoRenew = false;
	closingTime: number;
	doctorOffices: DoctorsOfficeDto[];
	editMode = false;
	errors: string[] = [];
	form: UntypedFormGroup;
	holidayWork = false;
	hourSegments: number;
	minDate = new Date();
	openingTime: number;
	professionals: ProfessionalDto[];
	sectors: SectorDto[];
	agendaHorarioService: AgendaHorarioService;
	professionalSpecialties: any[];
	specialityId: number;
	alias: string;
	hasError = hasError;
	getError = getError;
	careLines: CareLineDto[] = [];
	careLinesSelected: CareLineDto[] = [];
	hierarchicalUnits: HierarchicalUnitDto[] = [];
	professionalsWithoutResponsibility = [];
	editingDiaryId = null;
	private readonly routePrefix;
	lineOfCareAndPercentageOfProtectedAppointmentsValid = true;
	loadSavedData = false;
	temporary = false;
	practices: SnomedDto[];
	practicesOptions: ChipsOption<SnomedDto>[];
	practicesSelected: ChipsOption<SnomedDto>[] = [];
	showPractices = false;
	hasHealthcareProfessional = false;
	private fieldHierarchicalUnitRequired = false;
	colorList: COLOR[] = Object.assign([], DIARY_LABEL_COLORS);
	diaryLabels: DiaryLabelDto[] = [];
	diaryStartDate: Date;
	diaryEndDate: Date;
	isSaving = false;
	isHabilitarSolicitudReferenciaOn = false;
	isHabilitarConfiguracionAgendasOn = false;
	ButtonType = ButtonType;

	constructor(
		private readonly router: Router,
		private readonly route: ActivatedRoute,
		private readonly el: ElementRef,
		private readonly cdr: ChangeDetectorRef,
		private dialog: MatDialog,
		private translator: TranslateService,
		private readonly snackBarService: SnackBarService,
		private readonly contextService: ContextService,
		private readonly sectorService: SectorService,
		private doctorsOfficeService: DoctorsOfficeService,
		private healthcareProfessionalService: HealthcareProfessionalByInstitutionService,
		private readonly diaryService: DiaryService,
		private readonly diaryOpeningHoursService: DiaryOpeningHoursService,
		private readonly patientNameService: PatientNameService,
		private readonly specialtyService: SpecialtyService,
		private readonly hierarchicalUnitsService: HierarchicalUnitsService,
		private readonly professionalService: HealthcareProfessionalService,
		private readonly diaryCareLine: DiaryCareLineService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly practicesService: PracticesService,
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
		this.agendaHorarioService = new AgendaHorarioService(this.dialog, this.cdr, this.TODAY, this.MONDAY, snackBarService, EDiaryType.CLASSIC);
		this.featureFlagService.isActive(AppFeature.HABILITAR_OBLIGATORIEDAD_UNIDADES_JERARQUICAS).subscribe(isOn =>
			this.fieldHierarchicalUnitRequired = isOn
		);
		this.featureFlagService.isActive(AppFeature.HABILITAR_SOLICITUD_REFERENCIA).subscribe(isOn =>
			this.isHabilitarSolicitudReferenciaOn = isOn
		);
		this.featureFlagService.isActive(AppFeature.HABILITAR_RESTRICCION_CANTIDAD_DIAS_ASIG_TURNOS).subscribe(isOn =>
			this.isHabilitarConfiguracionAgendasOn = isOn
		);
	}

	ngOnInit(): void {
		this.form = new UntypedFormGroup({
			sectorId: new UntypedFormControl(null, [Validators.required]),
			doctorOffice: new UntypedFormControl(null, [Validators.required]),
			healthcareProfessionalId: new UntypedFormControl(null, [Validators.required]),
			professionalReplacedId: new UntypedFormControl(null),
			startDate: new UntypedFormControl(null, [Validators.required]),
			endDate: new UntypedFormControl(null, [Validators.required]),
			hierarchicalUnit: new UntypedFormControl(null),
			hierarchicalUnitTemporary: new UntypedFormControl(null),
			appointmentDuration: new UntypedFormControl(null, [Validators.required]),
			healthcareProfessionalSpecialtyId: new UntypedFormControl(null, [Validators.required]),
			conjointDiary: new UntypedFormControl(false, [Validators.nullValidator]),
			temporaryReplacement: new UntypedFormControl(false),
			alias: new UntypedFormControl(null, [Validators.nullValidator]),
			otherProfessionals: new UntypedFormArray([], [this.otherPossibleProfessionals()]),
			careLines: new UntypedFormControl([null]),
			diaryType: new UntypedFormControl(this.CONSULTATION),
			practices: new UntypedFormControl([]),
			protectedAppointmentsPercentage: new UntypedFormControl({ value: 0, disabled: true }, [Validators.pattern(PATTERN), Validators.max(MAX_INPUT)]),
			configurationDiary: new UntypedFormControl(null),
		});

		if (this.fieldHierarchicalUnitRequired) {
			const hierarchicalUnitCtrl = this.form.controls.hierarchicalUnit;
			hierarchicalUnitCtrl.setValidators(Validators.required);
			hierarchicalUnitCtrl.updateValueAndValidity();
		}

		this.form.controls.appointmentDuration.valueChanges
			.subscribe(newDuration => this.hourSegments = MINUTES_IN_HOUR / newDuration);

		this.form.controls.temporaryReplacement.valueChanges.subscribe((temporaryReplacement: boolean) => {
			const hierarchicalUnitCtrl = this.form.controls.hierarchicalUnit;
			const hierarchicalUnitTemporaryCtrl = this.form.controls.hierarchicalUnitTemporary;
			const professionalReplacedIdCtrl = this.form.controls.professionalReplacedId;
			if (temporaryReplacement) {
				hierarchicalUnitTemporaryCtrl.setValue(null);
				professionalReplacedIdCtrl.setValidators([Validators.required]);
				hierarchicalUnitTemporaryCtrl.setValidators([Validators.required]);
				hierarchicalUnitCtrl.setValue(null);
				if (this.fieldHierarchicalUnitRequired){
					hierarchicalUnitCtrl.clearValidators();
					hierarchicalUnitCtrl.updateValueAndValidity();
				}
			}
			else {
				professionalReplacedIdCtrl.clearValidators();
				hierarchicalUnitTemporaryCtrl.clearValidators();
				this.setHierarchicalUnits(this.form.value.healthcareProfessionalId);
			}
			professionalReplacedIdCtrl.updateValueAndValidity();
			hierarchicalUnitTemporaryCtrl.updateValueAndValidity();
		});



		this.route.data.subscribe(data => {
			if (data["editMode"]) {
				this.editMode = true;
				this.route.paramMap.subscribe((params) => {
					this.editingDiaryId = Number(params.get('agendaId'));
					this.diaryService.get(this.editingDiaryId).subscribe((diary: CompleteDiaryDto) => {
						this.setHierarchicalUnitsSync(diary.healthcareProfessionalId).subscribe(response => {
							this.hierarchicalUnits = response;
							this.setValuesFromExistingAgenda(diary);
						});
						this.disableNotEditableControls();
						this.validateLineOfCare();
					});

				});
			}
			else {
				this.form.controls.healthcareProfessionalId.valueChanges
					.pipe(filter(id => !!id))
					.subscribe((healthcareProfessionalId: number) => {
						this.form.controls.hierarchicalUnit.setValue(null);
						const professionalsWithoutSelected = this.professionals.filter((p: ProfessionalDto) => p.id !== healthcareProfessionalId);
						const { professionalReplacedId, temporaryReplacement } = this.form.value;

						this.professionalsWithoutResponsibility = professionalsWithoutSelected;

						if (temporaryReplacement) {
							if (professionalReplacedId === healthcareProfessionalId) {
								this.form.controls.hierarchicalUnitTemporary.setValue(null);
								this.form.controls.professionalReplacedId.setValue(null);
							}
						} else {
							this.setHierarchicalUnits(healthcareProfessionalId);
						}
					});

				this.form.controls.professionalReplacedId.valueChanges.pipe(filter(healthcareProfessionalId => !!healthcareProfessionalId)).subscribe((professionalReplacedId: number) => {
					this.form.controls.hierarchicalUnit.setValue(null);
					if (this.form.value.temporaryReplacement) {
						this.form.controls.hierarchicalUnitTemporary.setValue(null);
						this.setHierarchicalUnits(professionalReplacedId);
					}
				});
			}
		});


		this.sectorService.getAllSectorByType(SECTOR_AMBULATORIO).subscribe(data => {
			this.sectors = this.filterSectorsWithoutDoctorsOffice(data);
		});

		this.healthcareProfessionalService.getAll().subscribe(data => {
			this.professionals = data;
			this.professionalsWithoutResponsibility = data
		});

		if (!this.editMode) {

			this.form.controls.diaryType.valueChanges.subscribe(diaryType => {
				switch (diaryType) {
					case this.CONSULTATION: {
						this.validationsConsultation();
						if (this.form.value.healthcareProfessionalSpecialtyId)
							this.setCarelinesBySpecialty();
						this.showPractices = false;
						this.clearPractices();
						this.clearCareLines();
						break;
					}
					case this.PRACTICE: {
						this.setPractices();
						this.validationsPractices();
						this.clearCareLines();
						this.showPractices = true;
						break;
					}
				}
			});
		}

	}

	get careLinesAssociated(): UntypedFormControl {
		return this.form.get('careLines') as UntypedFormControl;
	}

	private setValuesFromExistingAgenda(diary: CompleteDiaryDto): void {
		if (diary.predecessorProfessionalId) {
			this.temporary = true;
			this.form.controls.temporaryReplacement.setValue(true);
			this.form.controls.professionalReplacedId.setValue(diary.predecessorProfessionalId);
			this.form.controls.hierarchicalUnitTemporary.disable();
			this.form.get('professionalReplacedId').disable();
		} else {
			this.form.controls.healthcareProfessionalId.setValue(diary.healthcareProfessionalId);
			if (!diary?.hierarchicalUnitAlias) {
				this.form.markAllAsTouched();
				this.hasHealthcareProfessional = true;
			}
		}

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
			if (!!diary?.hierarchicalUnitId)
				this.setHierarchicalUnitsByName(diary?.hierarchicalUnitId);

			this.specialtyService.getAllSpecialtyByProfessional(this.contextService.institutionId, healthcareProfessionalId.id)
				.subscribe(response => {
					this.professionalSpecialties = response;
					this.form.controls.healthcareProfessionalSpecialtyId.markAsTouched();
					if (this.professionalSpecialties.find(specialty => specialty.id === diary.clinicalSpecialtyId)) {
						this.form.controls.healthcareProfessionalSpecialtyId.setValue(diary.clinicalSpecialtyId);
						if (!diary.practicesInfo.length)
							this.setCarelinesBySpecialty();
					}
				});
		});

		this.form.controls.healthcareProfessionalId.setValue(diary.healthcareProfessionalId);
		this.diaryStartDate = dateISOParseDate(diary.startDate);
		this.diaryEndDate = dateISOParseDate(diary.endDate);
		this.form.controls.appointmentDuration.setValue(diary.appointmentDuration);

		this.agendaHorarioService.setAppointmentDuration(diary.appointmentDuration);

		this.appointmentManagement = diary.professionalAssignShift;
		this.autoRenew = diary.automaticRenewal;
		this.holidayWork = diary.includeHoliday;

		this.agendaHorarioService.setDiaryOpeningHours(diary.diaryOpeningHours);

		if (diary.associatedProfessionalsInfo.length > 0) {
			const professionalsReference = this.form.controls.otherProfessionals as UntypedFormArray;
			this.form.controls.conjointDiary.setValue(true);
			diary.associatedProfessionalsInfo.forEach(diaryAssociatedProfessional => {
				professionalsReference.push(this.initializeAnotherProfessional());
				professionalsReference.controls[professionalsReference.length - 1].setValue({ healthcareProfessionalId: diaryAssociatedProfessional.id });
			});
		}

		if (diary.alias) {
			this.form.controls.alias.setValue(diary.alias);
		}

		if (diary.careLinesInfo.length)
			this.careLinesSelected = diary.careLinesInfo;

		this.setSpecialityId(diary.clinicalSpecialtyId);
		this.setAlias(diary.alias);
		diary.predecessorProfessionalId ? this.setHierarchicalUnits(diary.predecessorProfessionalId, diary) : this.setHierarchicalUnits(diary.healthcareProfessionalId, diary);

		this.form.controls.diaryType.disable();

		if (diary.practicesInfo.length > 0) {
			this.form.controls.diaryType.setValue(this.PRACTICE);
			this.showPractices = true;
			this.practicesSelected = diary.practicesInfo.map(p => { return this.toChipsOptions(p) });
			this.setPractices();
			this.getCarelinesByPracticesAndSpecialty(diary.practicesInfo.map(p => { return p.id }), diary.clinicalSpecialtyId);
			this.validationsPractices();
		}

		if (this.isHabilitarConfiguracionAgendasOn) {
			this.form.controls.configurationDiary.setValue(diary.bookingRestriction);
		}
	}

	private setHierarchicalUnitsByName(hierarchicalUnitId: number) {
		this.form.get('hierarchicalUnit').disable();
		this.loadSavedData = true;
		if(this.hierarchicalUnits.length){
			this.form.controls.hierarchicalUnit.setValue(hierarchicalUnitId);
		}
		this.form.controls.professionalReplacedId.updateValueAndValidity();
	}

	ngAfterContentChecked(): void {
		this.cdr.detectChanges();
	}

	private setSpecialityId(healthcareProfesionalId) {
		this.specialityId = healthcareProfesionalId;
		this.form.controls.healthcareProfessionalSpecialtyId.setValue(healthcareProfesionalId);
	}

	private setAlias(alias) {
		this.alias = alias;
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
			const hours = dateParseTime(time);
			return Number(hours.getHours());
		}
	}

	getFullName(professional: ProfessionalDto): string {
		return `${professional.lastName} ${this.patientNameService.getPatientName(professional.firstName, professional.nameSelfDetermination)}`;
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
			dialogRef.afterClosed().subscribe(confirmed => this.savedDiary(confirmed));
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

		const startDate = toApiFormat(formValue.startDate);
		const endDate = toApiFormat(formValue.endDate);

		const ocupations$: Observable<OccupationDto[]> = this.diaryOpeningHoursService
			.getAllWeeklyDoctorsOfficeOcupation(formValue.doctorOffice.id, this.editingDiaryId, startDate, endDate);

		this.agendaHorarioService.setWeeklyOcupation(ocupations$);
	}

	private savedDiary = (confirmed: boolean) => {
		if (!confirmed) return;

		this.isSaving = true;
		this.errors = [];
		if (this.editMode) 
			this.updateDiary();
		else 
			this.addDiary();
	}

	private updateDiary = () => {
		const agendaEdit: DiaryDto = this.addAgendaId(this.buildDiaryADto(this.diaryLabels));
		this.diaryService.updateDiary(agendaEdit)
		.pipe(finalize(() => this.isSaving = false))
		.subscribe({
			next: (diaryId: number) => this.processSuccess(diaryId),
			error: (error: ApiErrorDto) => processErrors(error, (msg) => this.errors.push(msg)) 
		});
	}

	private addDiary = () => {
		const agenda: DiaryADto = this.buildDiaryADto(this.diaryLabels);
		this.diaryService.addDiary(agenda)
		.pipe(finalize(() => this.isSaving = false))
		.subscribe({
			next: (diaryId: number) => this.processSuccess(diaryId),
			error: (error: ApiErrorDto) => processErrors(error, (msg) => this.errors.push(msg)) 
		});
	}

	private processSuccess(agendaId: number) {
		if (agendaId) {
			this.snackBarService.showSuccess('turnos.agenda-setup.messages.SUCCESS');
			const url = `${this.routePrefix}${ROUTE_APPOINTMENT}/${ROUTE_AGENDAS}/${agendaId}`;
			this.router.navigate([url]);
		}
	}

	private addAgendaId(diary: any): DiaryDto {
		diary.id = this.editingDiaryId;
		return diary;
	}

	private buildDiaryADto(diaryLabels: DiaryLabelDto[]): DiaryADto {
		const resultDto = {
			appointmentDuration: this.form.getRawValue().appointmentDuration,
			healthcareProfessionalId: this.form.getRawValue().healthcareProfessionalId,
			doctorsOfficeId: this.form.getRawValue().doctorOffice.id,

			predecessorProfessionalId: this.form.get("professionalReplacedId").value,
			hierarchicalUnitId: this.form.get("temporaryReplacement").value ? this.form.get("hierarchicalUnitTemporary").value : this.form.get("hierarchicalUnit").value,

			startDate: toApiFormat(this.form.value.startDate),
			endDate: toApiFormat(this.form.value.endDate),

			automaticRenewal: this.autoRenew,
			includeHoliday: this.holidayWork,
			professionalAssignShift: this.appointmentManagement,

			diaryOpeningHours: this.agendaHorarioService.getDiaryOpeningHours(),
			clinicalSpecialtyId: this.form.value.healthcareProfessionalSpecialtyId,
			alias: this.form.value.alias === "" ? null : this.form.value.alias,
			diaryAssociatedProfessionalsId: this.form.value.otherProfessionals.map(professional => professional.healthcareProfessionalId),
			careLines: this.careLinesSelected.map(careLine => { return careLine.id }),
			practicesId: this.form.controls.practices.value,
			protectedAppointmentsPercentage: null,
			institutionId : this.contextService.institutionId,
			diaryLabelDto: diaryLabels,
		};
		const configurationDiary: DiaryBookingRestrictionDto = this.isHabilitarConfiguracionAgendasOn ? this.handleConfigurationDiaryValues() : null
		return {bookingRestriction: configurationDiary, ...resultDto}
	}

	private handleConfigurationDiaryValues(): DiaryBookingRestrictionDto {
		if (this.form.controls.configurationDiary.value)
			return {days: this.form.controls.configurationDiary.value?.daysRange, restrictionType: this.form.controls.configurationDiary.value?.configurationType}
		else return {days: null, restrictionType: EDiaryBookingRestrictionType.UNRESTRICTED }
	}

	scrollToDefaultStartingHour(): void {
		this.agendaHorarioService.setAppointmentDuration(this.form.getRawValue().appointmentDuration);

		const scrollbar = document.getElementsByClassName('cal-time-events')[0];
		scrollbar?.scrollTo(0, this.PIXEL_SIZE_HEIGHT * this.TURN_STARTING_HOUR * this.hourSegments);
	}

	setHierarchicalUnits(healthcareProfessionalId: number, diary?: CompleteDiaryDto) {
		this.professionalService.geUserIdByHealthcareProfessional(healthcareProfessionalId)
			.pipe(
				switchMap((userId: number) => this.hierarchicalUnitsService.fetchAllByUserIdAndInstitutionId(userId))
			)
			.subscribe(response => {
				this.hierarchicalUnits = response;
				if (diary?.hierarchicalUnitId) {
					this.updateFormWithDiary(diary);
				} else {
					this.updateFormWithoutDiary();
				}
			});
	}

	setHierarchicalUnitsSync(healthcareProfessionalId: number, diary?: CompleteDiaryDto): Observable<HierarchicalUnitDto[]> {
		return this.professionalService.geUserIdByHealthcareProfessional(healthcareProfessionalId)
			.pipe(
				switchMap((userId: number) => this.hierarchicalUnitsService.fetchAllByUserIdAndInstitutionId(userId))
			)
			.pipe(map(response => {
				if (diary?.hierarchicalUnitId) {
					this.updateFormWithDiary(diary);
				} else {
					this.updateFormWithoutDiary();
				}
				return response
			}));
	}

	private updateFormWithDiary(diary: CompleteDiaryDto) {
		this.hierarchicalUnits = [{
			id: diary.hierarchicalUnitId,
			name: diary.hierarchicalUnitAlias,
			typeId: 0,
		}];

		const controlName = diary.predecessorProfessionalId ? 'hierarchicalUnitTemporary' : 'hierarchicalUnit';
		const control = this.form.controls[controlName];
		control.setValue(diary.hierarchicalUnitId);
		control.updateValueAndValidity();
	}

	private updateFormWithoutDiary() {
		if (this.hierarchicalUnits.length) {
			const targetControl = this.form.value.temporaryReplacement ? 'hierarchicalUnitTemporary' : 'hierarchicalUnit';
			const control = this.form.controls[targetControl];

			control.setValue(this.hierarchicalUnits[0].id);
			control.updateValueAndValidity();
		} else {
			if (this.fieldHierarchicalUnitRequired)
				this.form.markAllAsTouched();
		}
	}


	getProfessionalSpecialties() {
		this.resetValues();
		this.specialtyService.getAllSpecialtyByProfessional(this.contextService.institutionId, this.form.get("healthcareProfessionalId").value)
			.subscribe(response => this.professionalSpecialties = response)
	}

	getProfessionalReplacement() {
		this.specialtyService.getAllSpecialtyByProfessional(this.contextService.institutionId, this.form.get("healthcareProfessionalId").value)
			.subscribe(response => this.professionalSpecialties = response)
	}

	getControl(key: string): any {
		return this.form.get(key);
	}

	addAssociatedProfessional() {
		const currentOtherProfessionals = this.form.controls.otherProfessionals as UntypedFormArray;
		currentOtherProfessionals.push(this.initializeAnotherProfessional());
	}

	setLabelsArray(labels: DiaryLabelDto[]) {
		this.diaryLabels = labels;
	}

	private initializeAnotherProfessional(): UntypedFormGroup {
		return new UntypedFormGroup({
			healthcareProfessionalId: new UntypedFormControl(null, [Validators.required]),
		});
	}

	clear(professionalIndex: number): void {
		const professionalsReference = this.form.controls.otherProfessionals as UntypedFormArray;
		if (professionalsReference.length === 1) {
			professionalsReference.controls[0].setValue({ healthcareProfessionalId: null });
		}
		else {
			professionalsReference.removeAt(professionalIndex);
		}
	}

	updateAssociatedProfessionalsForm() {
		let professionalsReference = this.form.controls.otherProfessionals as UntypedFormArray;
		if (this.form.value.conjointDiary === true) {
			professionalsReference.push(this.initializeAnotherProfessional());
		}
		else {
			professionalsReference.clear();
			this.form.controls.conjointDiary.setValue(false);
		}
	}

	updateTemporaryReplacementForm() {
		if (!this.form.value.temporaryReplacement) {
			this.form.controls.professionalReplacedId.setValue(null);
			this.form.controls.temporaryReplacement.setValue(false);
		}
	}

	private otherPossibleProfessionals(): ValidatorFn {
		return (control: UntypedFormArray): ValidationErrors | null => {
			return control.valid ? null : { validProfessionals: { valid: false } };
		};
	}

	validExtraProfessionalsList(): boolean {
		const professionalsReference = this.form.controls.otherProfessionals as UntypedFormArray;
		return professionalsReference.valid;
	}

	isProfessionalNonSelectable(professionalId: number): boolean {
		const professionalsReference = this.form.controls.otherProfessionals as UntypedFormArray;
		return professionalsReference.value.map(professional => professional.healthcareProfessionalId).includes(professionalId);
	}

	getNonResponsibleProfessional(): ProfessionalDto[] {
		return this.professionals?.filter(professional => professional.id !== this.form.controls.healthcareProfessionalId.value);
	}

	setCarelinesBySpecialty() {
		const specialtyId = this.form.get("healthcareProfessionalSpecialtyId").value;
		if (this.form.controls.diaryType.value === this.CONSULTATION) {
			this.diaryCareLine.getPossibleCareLinesForDiary(specialtyId).subscribe(careLines => {
				this.careLines = careLines;
				this.checkCareLinesSelected();
			});
			if (specialtyId && specialtyId !== this.specialityId) {
				this.form.controls.alias.setValue('');
			} else {
				this.form.controls.alias.setValue(this.alias);
			}
		}
		else{
			this.getCarelinesByPracticesAndSpecialty(this.form.value.practices,specialtyId);
		}
	}

	validateLineOfCare() {
		if (!this.careLinesSelected.length) {
			this.lineOfCareAndPercentageOfProtectedAppointmentsValid = false;
		} else {
			this.lineOfCareAndPercentageOfProtectedAppointmentsValid = true;
		}
	}

	setCareLines(careLines: string[]) {
		if (careLines.length) {
			this.careLinesSelected = careLines.map(careLine => ({
				id: this.careLines.find(c => c.description === careLine).id,
				description: careLine,
				clinicalSpecialties: this.careLines.find(c => c.description === careLine).clinicalSpecialties
			}));
		}
		else {
			this.careLinesSelected = [];
		}
		this.validateLineOfCare();
	}

	setPractices() {
		this.practicesService.get().subscribe(s => {
			this.practices = s.map(p => { return { id: p.id, sctid: p.sctid, pt: p.pt } });
			this.practicesOptions = this.practices.map(p => {
				return this.toChipsOptions(p)
			});
		});
	}

	setDiaryPracticesAndGetCarelines(selected: ChipsOption<SnomedDto>[]) {
		const result: number[] = selected.map(s => s.value.id);
		this.form.controls.practices.setValue(result);
		this.getCarelinesByPracticesAndSpecialty(result, this.form.controls.healthcareProfessionalSpecialtyId.value);
	}

	getCarelinesByPracticesAndSpecialty(practicesId: number[], clinicalSpecialtyId: number) {
		if (practicesId.length) {
			this.diaryCareLine.getPossibleCareLinesForDiaryByPracticesAndSpecialty(practicesId, clinicalSpecialtyId).subscribe(carelines => {
				this.careLines = carelines;
				this.checkCareLinesSelected();
			});
		}
		else
			this.careLines = [];
	}

	setSelectedStartDate(selectDate: Date) {
		this.form.controls.startDate.setValue(selectDate);
		this.setAllWeeklyDoctorsOfficeOcupation();
	}

	setSelectedEndDate(selectDate: Date) {
		this.form.controls.endDate.setValue(selectDate);
		this.setAllWeeklyDoctorsOfficeOcupation();
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

	private resetValues() {
		this.form.controls.healthcareProfessionalSpecialtyId.reset();
		this.form.controls.careLines.reset();
	}

	private toChipsOptions(p: SnomedDto): ChipsOption<SnomedDto> {
		return { value: p, compareValue: p.pt, identifier: p.id }
	}

	private clearCareLines() {
		this.careLines = [];
		this.careLinesSelected = [];
	}

	private clearPractices() {
		this.practices = [];
		this.practicesOptions = [];
		this.practicesSelected = [];
		this.form.controls.practices.setValue([]);
	}

	private validationsConsultation() {
		this.form.controls.practices.removeValidators([Validators.required]);
		this.form.controls.practices.updateValueAndValidity();
		this.form.controls.practices.reset();
		this.form.controls.healthcareProfessionalSpecialtyId.addValidators([Validators.required]);
		this.form.controls.healthcareProfessionalSpecialtyId.updateValueAndValidity();
	}

	private validationsPractices() {
		this.form.controls.practices.addValidators([Validators.required]);
		this.form.controls.practices.updateValueAndValidity();
		this.form.controls.healthcareProfessionalSpecialtyId.removeValidators([Validators.required]);
		this.form.controls.healthcareProfessionalSpecialtyId.updateValueAndValidity();
	}

	private filterSectorsWithoutDoctorsOffice(sectors: SectorDto[]) {
		return sectors.filter(sector => sector.hasDoctorsOffice);
	}
}

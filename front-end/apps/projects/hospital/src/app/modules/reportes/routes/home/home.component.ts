import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ProfessionalLicenseService } from "@api-rest/services/professional-license.service";
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

import { isSameOrAfter, newDate } from '@core/utils/moment.utils';
import { hasError } from '@core/utils/form.utils';
import { MIN_DATE, datePlusDays } from '@core/utils/date.utils';

import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

import {
	AppFeature,
	ERole,
	HierarchicalUnitDto,
	HierarchicalUnitTypeDto, ImageNetworkProductivityFilterDto, LicenseNumberTypeDto,
	ProfessionalLicenseNumberDto, ProfessionalRegistrationNumbersDto,
	ProfessionalsByClinicalSpecialtyDto
} from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { ReportsService } from '@api-rest/services/reports.service';

import { REPORT_TYPES, REPORT_TYPES_ID } from '../../constants/report-types';
import { UIComponentDto } from '@extensions/extensions-model';
import { anyMatch } from '@core/utils/array.utils';
import { PermissionsService } from '@core/services/permissions.service';
import { HierarchicalUnitsService } from "@api-rest/services/hierarchical-units.service";
import { HierarchicalUnitTypeService } from "@api-rest/services/hierarchical-unit-type.service";
import { APPOINTMENT_STATES_DESCRIPTION, APPOINTMENT_STATES_ID, AppointmentState } from "@turnos/constants/appointment";
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { fixDate } from '@core/utils/date/format';
import { isBefore, subMonths } from 'date-fns';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	form: FormGroup<ReportForm>;
	public submitted = false;

	public hasError = hasError;

	professionalsTypeahead: TypeaheadOption<ProfessionalRegistrationNumbersDto>[];
	professionals: ProfessionalRegistrationNumbersDto[] = [];
	hierarchicalUnitTypesTypeahead: TypeaheadOption<HierarchicalUnitTypeDto>[];
	hierarchicalUnitsTypeahead: TypeaheadOption<HierarchicalUnitDto>[];
	hierarchicalUnits: HierarchicalUnitDto[];
	appointmentStates: AppointmentState[];

	specialtiesTypeaheadOptions$: Observable<TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[]>;

	idProfessional: number;
	idSpecialty: number;

	REPORT_TYPES = REPORT_TYPES;
	REPORT_TYPES_ID = REPORT_TYPES_ID;

	minDate = MIN_DATE;
	maxEndDate: Date;
	minEndDate: Date;
	maxFixedEndDate: Date;
	oneWeekRange = 7;

	cubeReportData: UIComponentDto;

	hasToShowHierarchicalUnitSection = false;
	hasToShowAppointmentStateFilter = false;
	hasDateRangeFilterWithFixedEndDate = false;
	isLoadingRequestReport = false;
	private nameSelfDeterminationFF = false;
	private licensesTypeMasterData: LicenseNumberTypeDto[];

	constructor(
		private readonly professionalLicenseService: ProfessionalLicenseService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly reportsService: ReportsService,
		private readonly permissionsService: PermissionsService,
		private readonly hierarchicalUnitsService: HierarchicalUnitsService,
		private readonly hierarchicalUnitTypeService: HierarchicalUnitTypeService,
		private readonly featureFlagService: FeatureFlagService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => { this.nameSelfDeterminationFF = isOn });
	}

	ngOnInit(): void {
		this.form = new FormGroup<ReportForm>({
			reportType: new FormControl(null, Validators.required),
			startDate: new FormControl(this.firstDayOfThisMonth(), Validators.required),
			endDate: new FormControl(this.lastDayOfThisMonth(), Validators.required),
			specialtyId: new FormControl(null),
			professionalId: new FormControl(null),
			hierarchicalUnitTypeId: new FormControl(null),
			hierarchicalUnitId: new FormControl(null),
			includeHierarchicalUnitDescendants: new FormControl(null),
			appointmentStateId: new FormControl(null)
		});
		this.professionalLicenseService.getAllProfessionalRegistrationNumbers().subscribe(professionals => {
			this.professionals = professionals;
			this.specialtiesTypeaheadOptions$ = this.getSpecialtiesTypeaheadOptions$(professionals);
			this.professionalsTypeahead = professionals.map(d => this.toProfessionalTypeahead(d));
		});
		this.featureFlagService.isActive(AppFeature.HABILITAR_REPORTE_DETALLE_NOMINAL_GUARDIA_EN_DESARROLLO).subscribe(isOn => {
			if (!isOn) this.REPORT_TYPES = this.REPORT_TYPES.filter(report => report.id != REPORT_TYPES_ID.GUARD_ATTENTION_DETAIL_REPORT);
		})
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			if (!anyMatch<ERole>(userRoles, [ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, ERole.PERSONAL_DE_ESTADISTICA]))
				this.REPORT_TYPES = this.REPORT_TYPES.filter(report => report.id != REPORT_TYPES_ID.MONTHLY
					&& report.id != REPORT_TYPES_ID.OUTPATIENT_SUMMARY_REPORT
					&& report.id != REPORT_TYPES_ID.NOMINAL_APPOINTMENTS_DETAIL
					&& report.id != REPORT_TYPES_ID.NOMINAL_DIAGNOSTIC_IMAGING
					&& report.id != REPORT_TYPES_ID.GUARD_ATTENTION_DETAIL_REPORT);
		});
		this.hierarchicalUnitsService.getByInstitution().subscribe(hierarchicalUnits => {
			this.hierarchicalUnits = hierarchicalUnits;
			this.hierarchicalUnitsTypeahead = hierarchicalUnits.map(hu => this.toHierarchicalUnitTypeahead(hu));
		});
		this.hierarchicalUnitTypeService.getByInstitution().subscribe(hierarchicalUnitTypes => this.hierarchicalUnitTypesTypeahead = hierarchicalUnitTypes.map(hut => this.toHierarchicalUnitTypeTypeahead(hut)));

		this.professionalLicenseService.getLicensesType().subscribe(licensesTypeMasterData => {
			this.licensesTypeMasterData = licensesTypeMasterData;
		});

		this.appointmentStates = this.getAppointmentStates();

		this.setMaxFixedEndDate();

		this.onSelectionReportTypeChange();
	}

	private onSelectionReportTypeChange() {
		this.form.controls.reportType.valueChanges.subscribe(
			reportType => {
				this.hasToShowHierarchicalUnitSection = this.showHierarchicalUnitSection(reportType);
				this.hasToShowAppointmentStateFilter = this.showAppointmentStateFilter(reportType);
				this.hasDateRangeFilterWithFixedEndDate = this.showDateRangeFilterWithFixedEndDate(reportType);
			});
	}

	private showHierarchicalUnitSection(reportType: number): boolean {
		return (reportType === REPORT_TYPES_ID.MONTHLY ||
			reportType === REPORT_TYPES_ID.OUTPATIENT_SUMMARY_REPORT ||
			reportType === REPORT_TYPES_ID.MONTHLY_SUMMARY_OF_EXTERNAL_CLINIC_APPOINTMENTS ||
			reportType === REPORT_TYPES_ID.GUARD_ATTENTION_DETAIL_REPORT ||
			reportType === REPORT_TYPES_ID.NOMINAL_APPOINTMENTS_DETAIL   ||
			reportType === REPORT_TYPES_ID.NOMINAL_DIAGNOSTIC_IMAGING);
	}

	private showAppointmentStateFilter(reportType: number): boolean {
		return (reportType === REPORT_TYPES_ID.MONTHLY_SUMMARY_OF_EXTERNAL_CLINIC_APPOINTMENTS ||
			reportType === REPORT_TYPES_ID.NOMINAL_APPOINTMENTS_DETAIL);
	}

	private showDateRangeFilterWithFixedEndDate(reportType: number): boolean {
		return (reportType === REPORT_TYPES_ID.MONTHLY_SUMMARY_OF_EXTERNAL_CLINIC_APPOINTMENTS ||
			reportType === REPORT_TYPES_ID.GUARD_ATTENTION_DETAIL_REPORT);
	}

	private firstDayOfThisMonth(): Date {
		const today = newDate();
		return new Date(today.getUTCFullYear(), today.getUTCMonth(), 1);
	}

	private lastDayOfThisMonth(): Date {
		const today = newDate();
		return new Date(today.getUTCFullYear(), today.getUTCMonth() + 1, 0);
	}

	private setMaxFixedEndDate() {
		this.maxFixedEndDate = newDate();
		this.maxFixedEndDate = datePlusDays(newDate(), this.oneWeekRange);
	}

	getInitialDateRange(): DateRange {
		const today = newDate();
		return {start: today, end: this.maxFixedEndDate};
	}

	onDateRangeChange(dateRange: DateRange) {
		if (dateRange) {
		  this.form.patchValue({
			startDate: dateRange.start,
			endDate: dateRange.end
		  });
		  this.checkValidDates(false);
		}
	}

	maxStartDate(endDate) {
		const today = newDate();
		return !endDate ? today : isBefore(today, endDate) ? today : endDate;
	}

	private getSpecialtiesTypeaheadOptions$(doctors: ProfessionalRegistrationNumbersDto[]) {
		return this.clinicalSpecialtyService.getClinicalSpecialties(doctors.map(d => d.healthcareProfessionalId))
			.pipe(map(toTypeaheadOptionList));

		function toTypeaheadOptionList(prosBySpecialtyList: ProfessionalsByClinicalSpecialtyDto[]):
			TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[] {
			return prosBySpecialtyList.map(toTypeaheadOption);

			function toTypeaheadOption(s: ProfessionalsByClinicalSpecialtyDto): TypeaheadOption<ProfessionalsByClinicalSpecialtyDto> {
				return {
					compareValue: s.clinicalSpecialty.name,
					value: s
				};
			}
		}
	}

	setSpecialty(professionalsByClinicalSpecialtyDto: ProfessionalsByClinicalSpecialtyDto) {
		this.idSpecialty = professionalsByClinicalSpecialtyDto?.clinicalSpecialty?.id;
		this.form.controls.specialtyId.setValue(professionalsByClinicalSpecialtyDto?.clinicalSpecialty?.id);

		const professionalsFilteredBy = this.getProfessionalsFilteredBy(professionalsByClinicalSpecialtyDto);
		this.professionalsTypeahead = professionalsFilteredBy.map(d => this.toProfessionalTypeahead(d));
	}

	public setHierarchicalUnitType(hierarchicalUnitTypeDto: HierarchicalUnitTypeDto) {
		this.form.controls.hierarchicalUnitTypeId.setValue(hierarchicalUnitTypeDto?.id);
		const hierarchicalUnitsFilteredBy = this.getHierarchicalUnitsFilteredBy(hierarchicalUnitTypeDto);
		this.hierarchicalUnitsTypeahead = hierarchicalUnitsFilteredBy.map(hu => this.toHierarchicalUnitTypeahead(hu));

		if (!hierarchicalUnitTypeDto) {
			this.form.controls.includeHierarchicalUnitDescendants.setValue(false);
			this.form.controls.hierarchicalUnitId.setValue(null);
		}
	}

	public setHierarchicalUnit(hierarchicalUnitDto: HierarchicalUnitDto) {
		if (hierarchicalUnitDto)
			this.form.controls.hierarchicalUnitId.setValue(hierarchicalUnitDto?.id);
		else {
			this.form.controls.includeHierarchicalUnitDescendants.setValue(false);
			this.form.controls.hierarchicalUnitId.setValue(null);
		}
	}

	setProfessional(professional: ProfessionalRegistrationNumbersDto) {
		this.idProfessional = professional?.healthcareProfessionalId;
		this.form.controls.professionalId.setValue(this.idProfessional);
	}

	private getProfessionalsFilteredBy(specialty: ProfessionalsByClinicalSpecialtyDto): ProfessionalRegistrationNumbersDto[] {
		if (specialty?.professionalsIds) {
			return this.professionals.filter(p => specialty.professionalsIds.find(e => e === p.healthcareProfessionalId));
		}
		return this.professionals;
	}

	private getHierarchicalUnitsFilteredBy(hierarchicalUnitTypeDto: HierarchicalUnitTypeDto): HierarchicalUnitDto[] {
		if (hierarchicalUnitTypeDto?.id) {
			return this.hierarchicalUnits.filter(hu => hu.typeId === hierarchicalUnitTypeDto.id);
		}
		return this.hierarchicalUnits;
	}

	private toProfessionalTypeahead(professionalRegistrationNumbersDto: ProfessionalRegistrationNumbersDto): TypeaheadOption<ProfessionalRegistrationNumbersDto> {
		return {
			compareValue: this.getFullNameLicense(professionalRegistrationNumbersDto),
			value: professionalRegistrationNumbersDto
		};
	}

	private toHierarchicalUnitTypeTypeahead(hierarchicalUnitTypeDto: HierarchicalUnitTypeDto): TypeaheadOption<HierarchicalUnitTypeDto> {
		return {
			compareValue: hierarchicalUnitTypeDto.description,
			value: hierarchicalUnitTypeDto
		};
	}

	private toHierarchicalUnitTypeahead(hierarchicalUnitDto: HierarchicalUnitDto): TypeaheadOption<HierarchicalUnitDto> {
		return {
			compareValue: hierarchicalUnitDto.name,
			value: hierarchicalUnitDto
		};
	}

	getFullNameLicense(professional: ProfessionalRegistrationNumbersDto): string {
		return `${this.getFullName(professional)} ${professional.license.length ? '-' : ''} ${this.getFullLicense(professional.license)}`;
	}

	getFullName(professional: ProfessionalRegistrationNumbersDto): string {
		const nameSelfDetermination = professional.nameSelfDetermination;
		return `${professional.lastName}, ${this.nameSelfDeterminationFF && nameSelfDetermination ? nameSelfDetermination : professional.firstName}`;
	}

	getFullLicense(license: ProfessionalLicenseNumberDto[]): string {
		const licenseUnique = license.filter((item, index, arr) => {
			return !arr.slice(0, index).some(other => (other.typeId === item.typeId && other.licenseNumber === item.licenseNumber));
		});

		return `${licenseUnique.map((l) =>
			this.licensesTypeMasterData?.find(item => item.id === l.typeId).description + ' ' + l.licenseNumber)
			.join(' - ')}`;
	}

	checkValidDates(isStartDateChange: boolean) {
		const fixStartDate = fixDate(this.form.value.startDate);
		const fixEndDate = fixDate(this.form.value.endDate);

		this.form.controls.startDate.setValue(fixStartDate);
		this.form.controls.endDate.setValue(fixEndDate);
		// if both are present, check that the end date is not after the start date
		if (this.form.value.startDate && this.form.value.endDate) {
			if (isBefore(fixEndDate, fixStartDate)) {
				this.form.controls.endDate.setErrors({ min: true });
				this.form.controls.startDate.setErrors({ max: true });
			} else {
				this.form.controls.endDate.setErrors(null);
				this.checkStartDateIsSameOrBeforeToday();
			}
			if (this.form.controls.reportType.value === REPORT_TYPES_ID.NOMINAL_DIAGNOSTIC_IMAGING && isStartDateChange ) {
					this.form.controls.endDate.setValue(null);
					this.maxEndDate = new Date(fixStartDate.getUTCFullYear(), fixStartDate.getUTCMonth() + 1, 0);
					this.minEndDate = fixStartDate;
			}
		} else if (fixStartDate) {
			this.checkStartDateIsSameOrBeforeToday();
		} else if (fixEndDate) {
			this.form.controls.endDate.setErrors(null);
		}
	}

	private checkStartDateIsSameOrBeforeToday() {
		const today = newDate();
		const startDate = this.form.value.startDate;
		(isSameOrAfter(today, startDate))
			? this.form.controls.startDate.setErrors(null)
			: this.form.controls.startDate.setErrors({ afterToday: true });
	}

	generateReport() {
		this.submitted = true;
		if (this.form.valid) {
			this.isLoadingRequestReport = true;
			const reportFilters = this.getReportFilters();
			const reportId = this.form.value.reportType;
			const reportDescription = this.REPORT_TYPES.find(reportType => reportType.id === reportId).description;

			const getReportById = {
				[REPORT_TYPES_ID.MONTHLY]: this.reportsService.getMonthlyReport(reportFilters, `${reportDescription}.xls`),
				[REPORT_TYPES_ID.OUTPATIENT_SUMMARY_REPORT]: this.reportsService.getOutpatientSummaryReport(reportFilters, `${reportDescription}.xls`),
				[REPORT_TYPES_ID.MONTHLY_SUMMARY_OF_EXTERNAL_CLINIC_APPOINTMENTS]:
					this.reportsService.getMonthlySummaryOfExternalClinicAppointmentsReport(reportFilters, `${reportDescription}.xls`),
				[REPORT_TYPES_ID.DIABETIC_PATIENTS]: this.reportsService.getDiabetesReport(),
				[REPORT_TYPES_ID.HYPERTENSIVE_PATIENTS]: this.reportsService.getHypertensionReport(),
				[REPORT_TYPES_ID.WEEKLY_EPIDEMIOLOGICAL_REPORT]: this.reportsService.getEpidemiologicalWeekReport(),
				[REPORT_TYPES_ID.NOMINAL_APPOINTMENTS_DETAIL]:
					this.reportsService.getNominalAppointmentsDetail(reportFilters, `${reportDescription}.xls`),
				[REPORT_TYPES_ID.NOMINAL_DIAGNOSTIC_IMAGING]:
					this.reportsService.getImageNetworkProductivityReport(this.prepareImageNetworkProductivityFilterDto(), `${reportDescription}.xls`),
				[REPORT_TYPES_ID.GUARD_ATTENTION_DETAIL_REPORT]: this.reportsService.getNominalEmergencyCareEpisodeDetail(reportFilters, `${reportDescription}.xls`)
			};

			const selectedReport = getReportById[reportId];
			if (selectedReport) {
				selectedReport.subscribe(() => this.isLoadingRequestReport = false);
			}
		}
	}


	resetCubeReport() {
		this.cubeReportData = null;
		this.resetForm();
		if (this.form.controls.reportType.value === REPORT_TYPES_ID.NOMINAL_DIAGNOSTIC_IMAGING) {
			this.setDatesForNominalDiagnosticImaging();
		}
	}

	prepareImageNetworkProductivityFilterDto(): ImageNetworkProductivityFilterDto {
		return {
			clinicalSpecialtyId: this.form.controls.specialtyId.value,
			from: dateToDateDto(fixDate(this.form.value.startDate)),
			healthcareProfessionalId: this.form.controls.professionalId.value,
			to: dateToDateDto(fixDate(this.form.value.endDate)),
		}
	}

	setDatesForNominalDiagnosticImaging() {
		this.form.controls.endDate.setValue(this.getDateWithPreviousMonth(false));
		this.form.controls.startDate.setValue(this.getDateWithPreviousMonth(true));
		this.maxEndDate = this.form.value.endDate;
	}

	resetForm() {
		this.form.controls.endDate.setValue(this.lastDayOfThisMonth());
		this.form.controls.startDate.setValue(this.firstDayOfThisMonth());
		this.form.controls.professionalId.setValue(null);
		this.clearAppointmentStateId();
		this.setHierarchicalUnitType(null);
		this.setHierarchicalUnit(null);
		this.setSpecialty(null);
		this.setProfessional(null);
		this.specialtiesTypeaheadOptions$ = this.getSpecialtiesTypeaheadOptions$(this.professionals);
		this.maxEndDate = null;
		this.minEndDate = null;
	}

	getDateWithPreviousMonth(isStartDate: boolean): Date {
		const today = new Date();
		if (isStartDate) {
			today.setDate(1);
			return subMonths(today, 1)
		} else {
			today.setDate(0);
			return today;
		}
	}

	isLastDayOfTheMonth(date: Date): boolean {
		date.setDate(date.getDate() + 1);
		const proximoDia = date;
		return date.getMonth() !== proximoDia.getMonth();
	}

	clearAppointmentStateId(): void {
		this.form.controls.appointmentStateId.setValue(null);
	}

	private getAppointmentStates(): AppointmentState[] {
		return [
			{
				id: APPOINTMENT_STATES_ID.ASSIGNED,
				description: APPOINTMENT_STATES_DESCRIPTION.ASSIGNED
			},
			{
				id: APPOINTMENT_STATES_ID.CONFIRMED,
				description: APPOINTMENT_STATES_DESCRIPTION.CONFIRMED
			},
			{
				id: APPOINTMENT_STATES_ID.ABSENT,
				description: APPOINTMENT_STATES_DESCRIPTION.ABSENT
			},
			{
				id: APPOINTMENT_STATES_ID.CANCELLED,
				description: APPOINTMENT_STATES_DESCRIPTION.CANCELLED
			},
			{
				id: APPOINTMENT_STATES_ID.SERVED,
				description: APPOINTMENT_STATES_DESCRIPTION.SERVED
			},
			{
				id: APPOINTMENT_STATES_ID.BOOKED,
				description: APPOINTMENT_STATES_DESCRIPTION.BOOKED
			}]
	}

	private getReportFilters(): ReportFilters {
		return {
			fromDate: this.form.controls.startDate.value,
			toDate: this.form.controls.endDate.value,
			clinicalSpecialtyId: this.form.controls.specialtyId.value,
			doctorId: this.form.controls.professionalId.value,
			hierarchicalUnitTypeId: this.form.controls.hierarchicalUnitTypeId.value,
			hierarchicalUnitId: this.form.controls.hierarchicalUnitId.value,
			includeHierarchicalUnitDescendants: this.form.controls.includeHierarchicalUnitDescendants.value,
			appointmentStateId: this.form.controls.appointmentStateId.value
		}
	}
}

interface ReportForm {
	reportType: FormControl<number>,
	startDate: FormControl<Date>,
	endDate: FormControl<Date>,
	specialtyId: FormControl<number>,
	professionalId: FormControl<number>,
	hierarchicalUnitTypeId: FormControl<number>,
	hierarchicalUnitId: FormControl<number>,
	includeHierarchicalUnitDescendants: FormControl<boolean>,
	appointmentStateId: FormControl<number>
}

export interface ReportFilters {
	fromDate: Date;
	toDate: Date;
	clinicalSpecialtyId?: number;
	doctorId?: number;
	hierarchicalUnitTypeId?: number;
	hierarchicalUnitId?: number;
	includeHierarchicalUnitDescendants?: boolean;
	appointmentStateId?: number;
}

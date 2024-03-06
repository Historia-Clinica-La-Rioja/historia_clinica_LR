import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ProfessionalLicenseService } from "@api-rest/services/professional-license.service";
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Moment } from 'moment';

import { dateToMoment, newMoment } from '@core/utils/moment.utils';
import { hasError } from '@core/utils/form.utils';
import { MIN_DATE } from '@core/utils/date.utils';

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

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	form: FormGroup<ReportForm>;
	public submitted = false;

	public hasError = hasError;
	showErrorMonth = false;

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
	maxEndDate?: Moment;

	cubeReportData: UIComponentDto;

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
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn =>{this.nameSelfDeterminationFF = isOn});
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
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			if (!anyMatch<ERole>(userRoles, [ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, ERole.PERSONAL_DE_ESTADISTICA]))
				this.REPORT_TYPES = this.REPORT_TYPES.filter(report => report.id != REPORT_TYPES_ID.MONTHLY
					&& report.id != REPORT_TYPES_ID.OUTPATIENT_SUMMARY_REPORT
					&& report.id != REPORT_TYPES_ID.NOMINAL_APPOINTMENTS_DETAIL
					&& report.id != REPORT_TYPES_ID.NOMINAL_DIAGNOSTIC_IMAGING);
		});
		this.hierarchicalUnitsService.getByInstitution().subscribe(hierarchicalUnits => {
			this.hierarchicalUnits = hierarchicalUnits;
			this.hierarchicalUnitsTypeahead = hierarchicalUnits.map(hu => this.toHierarchicalUnitTypeahead(hu));
		});
		this.hierarchicalUnitTypeService.getByInstitution().subscribe( hierarchicalUnitTypes => this.hierarchicalUnitTypesTypeahead = hierarchicalUnitTypes.map(hut => this.toHierarchicalUnitTypeTypeahead(hut)));

		this.professionalLicenseService.getLicensesType().subscribe(licensesTypeMasterData => {
			this.licensesTypeMasterData = licensesTypeMasterData;
		});

		this.appointmentStates = this.getAppointmentStates();
	}

	private firstDayOfThisMonth(): Moment {
		const today = newMoment();
		return dateToMoment(new Date(today.year(), today.month(), 1));
	}

	private lastDayOfThisMonth(): Moment {
		const today = newMoment();
		return dateToMoment(new Date(today.year(), today.month() + 1, 0));
	}

	maxStartDate(endDate) {
		const today = newMoment();
		if (endDate) {
			return (today.isBefore(endDate)) ? today : endDate;
		}
		return today;
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

	checkValidDates() {
		// if both are present, check that the end date is not after the start date
		if (this.form.value.startDate && this.form.value.endDate) {
			const endDate: Moment = this.form.value.endDate;
			const startDate: Moment = this.form.value.startDate;
			if (endDate.isBefore(this.form.value.startDate)) {
				this.form.controls.endDate.setErrors({ min: true });
				this.form.controls.startDate.setErrors({ max: true });
			} else {
				this.form.controls.endDate.setErrors(null);
				this.checkStartDateIsSameOrBeforeToday();
			}
			if (this.form.controls.reportType.value === REPORT_TYPES_ID.NOMINAL_DIAGNOSTIC_IMAGING) {
				if(endDate.month() !== startDate.month()){
					this.showErrorMonth = true;
				}else{
					this.showErrorMonth = false;
				}
			}
		} else if (this.form.value.startDate) {
			this.checkStartDateIsSameOrBeforeToday();
		} else if (this.form.value.endDate) {
			this.form.controls.endDate.setErrors(null);
		}
	}

	private checkStartDateIsSameOrBeforeToday() {
		const today = newMoment();
		(today.isSameOrAfter(this.form.value.startDate))
			? this.form.controls.startDate.setErrors(null)
			: this.form.controls.startDate.setErrors({ afterToday: true });
	}

	generateReport() {
		this.submitted = true;
		if (this.form.valid) {
			this.isLoadingRequestReport = true;
			let params: ReportFilters = {
				startDate: this.form.controls.startDate.value,
				endDate: this.form.controls.endDate.value,
				specialtyId: this.form.controls.specialtyId.value,
				professionalId: this.form.controls.professionalId.value,
				hierarchicalUnitTypeId: this.form.controls.hierarchicalUnitTypeId.value,
				hierarchicalUnitId: this.form.controls.hierarchicalUnitId.value,
				includeHierarchicalUnitDescendants: this.form.controls.includeHierarchicalUnitDescendants.value,
				appointmentStateId: this.form.controls.appointmentStateId.value
			}
			const reportId = this.form.value.reportType;
			switch (reportId) {
				case REPORT_TYPES_ID.MONTHLY:
					this.reportsService.getMonthlyReport(params, `${this.REPORT_TYPES[0].description}.xls`).subscribe(() => this.isLoadingRequestReport = false);
					break;
				case REPORT_TYPES_ID.OUTPATIENT_SUMMARY_REPORT:
					this.reportsService.getOutpatientSummaryReport(params, `${this.REPORT_TYPES[1].description}.xls`).subscribe(() => this.isLoadingRequestReport = false);
					break;
				case REPORT_TYPES_ID.DIABETIC_PATIENTS:
					this.reportsService.getDiabetesReport().subscribe(result => {
						this.cubeReportData = result
						this.isLoadingRequestReport = false
					});
					break;
				case REPORT_TYPES_ID.HYPERTENSIVE_PATIENTS:
					this.reportsService.getHypertensionReport().subscribe(result => {
						this.cubeReportData = result
						this.isLoadingRequestReport = false
					});
					break;
				case REPORT_TYPES_ID.WEEKLY_EPIDEMIOLOGICAL_REPORT:
					this.reportsService.getEpidemiologicalWeekReport().subscribe(result => {
						this.cubeReportData = result
						this.isLoadingRequestReport = false
					});
					break;
				case REPORT_TYPES_ID.NOMINAL_APPOINTMENTS_DETAIL:
					this.reportsService.getNominalAppointmentsDetail(params, `${this.REPORT_TYPES[5].description}.xls`).subscribe(() => this.isLoadingRequestReport = false);
					break;
				case REPORT_TYPES_ID.NOMINAL_DIAGNOSTIC_IMAGING:
					this.reportsService.getImageNetworkProductivityReport(this.prepareImageNetworkProductivityFilterDto(), `${this.REPORT_TYPES[6].description}.xls`).subscribe(() => this.isLoadingRequestReport = false);
					break;
				default:
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

	prepareImageNetworkProductivityFilterDto(): ImageNetworkProductivityFilterDto{
		return {
			clinicalSpecialtyId: this.form.controls.specialtyId.value,
			from: dateToDateDto(new Date(this.form.controls.startDate.value.year(),this.form.controls.startDate.value.month(),this.form.controls.startDate.value.date())),
			healthcareProfessionalId: this.form.controls.professionalId.value,
			to: dateToDateDto(new Date(this.form.controls.endDate.value.year(),this.form.controls.endDate.value.month(),this.form.controls.endDate.value.date())),
		}
	}

	setDatesForNominalDiagnosticImaging() {
		this.form.controls.endDate.setValue(this.getDateWithPreviousMonth(false));
		this.form.controls.startDate.setValue(this.getDateWithPreviousMonth(true));
		this.maxEndDate = this.form.controls.endDate.value;
	}

	resetForm(){
		this.form.controls.endDate.setValue(this.lastDayOfThisMonth());
		this.form.controls.startDate.setValue(this.firstDayOfThisMonth());
		this.form.controls.professionalId.setValue(null);
		this.clearAppointmentStateId();
		this.setHierarchicalUnitType(null);
		this.setHierarchicalUnit(null);
		this.setSpecialty(null);
		this.setProfessional(null);
		this.specialtiesTypeaheadOptions$ = this.getSpecialtiesTypeaheadOptions$(this.professionals);
		this.maxEndDate= null;
	}

	getDateWithPreviousMonth(isStartDate: boolean) {
		const today = newMoment();
		if (this.isLastDayOfTheMonth(today) && !isStartDate) {
			return dateToMoment(new Date(today.year(), today.month(), 0));
		}
		return dateToMoment(new Date(today.year(), today.month() - 1, 1));
	}

	isLastDayOfTheMonth(date: Moment): boolean {
		const proximoDia = new Date(date.year(), date.month() - 1, date.date())
		proximoDia.setDate(proximoDia.getDate() + 1);
		return date.month() !== proximoDia.getMonth();
	}

	clearAppointmentStateId(): void {
		this.form.controls.appointmentStateId.setValue(null);
	}

	private getAppointmentStates() : AppointmentState[] {
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
}

interface ReportForm {
	reportType: FormControl<number>,
	startDate: FormControl<Moment>,
	endDate: FormControl<Moment>,
	specialtyId: FormControl<number>,
	professionalId: FormControl<number>,
	hierarchicalUnitTypeId: FormControl<number>,
	hierarchicalUnitId: FormControl<number>,
	includeHierarchicalUnitDescendants: FormControl<boolean>,
	appointmentStateId: FormControl<number>
}

export interface ReportFilters {
	startDate: Moment;
	endDate: Moment;
	specialtyId?: number;
	professionalId?: number;
	hierarchicalUnitTypeId?: number;
	hierarchicalUnitId?: number;
	includeHierarchicalUnitDescendants?: boolean;
	appointmentStateId?: number;
}

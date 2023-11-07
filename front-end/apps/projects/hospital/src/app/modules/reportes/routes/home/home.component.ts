import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
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
	HierarchicalUnitTypeDto, LicenseNumberTypeDto, ProfessionalDto,
	ProfessionalLicenseNumberDto, ProfessionalRegistrationNumbersDto,
	ProfessionalsByClinicalSpecialtyDto
} from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { ReportsService } from '@api-rest/services/reports.service';

import { REPORT_TYPES } from '../../constants/report-types';
import { UIComponentDto } from '@extensions/extensions-model';
import { anyMatch } from '@core/utils/array.utils';
import { PermissionsService } from '@core/services/permissions.service';
import { HierarchicalUnitsService } from "@api-rest/services/hierarchical-units.service";
import { HierarchicalUnitTypeService } from "@api-rest/services/hierarchical-unit-type.service";

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	form: UntypedFormGroup;
	public submitted = false;

	public hasError = hasError;

	professionalsTypeahead: TypeaheadOption<ProfessionalRegistrationNumbersDto>[];
	professionalInitValue: TypeaheadOption<ProfessionalDto>;
	professionals: ProfessionalRegistrationNumbersDto[] = [];
	hierarchicalUnitTypesTypeahead: TypeaheadOption<HierarchicalUnitTypeDto>[];
	hierarchicalUnitsTypeahead: TypeaheadOption<HierarchicalUnitDto>[];
	hierarchicalUnits: HierarchicalUnitDto[];

	specialtiesTypeaheadOptions$: Observable<TypeaheadOption<ProfessionalsByClinicalSpecialtyDto>[]>;

	idProfessional: number;
	idSpecialty: number;

	REPORT_TYPES = REPORT_TYPES;

	minDate = MIN_DATE;

	cubeReportData: UIComponentDto;

	isLoadingRequestReport = false;
	private nameSelfDeterminationFF = false;
	private licensesTypeMasterData: LicenseNumberTypeDto[];

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
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
		this.form = this.formBuilder.group({
			reportType: [null, Validators.required],
			startDate: [this.firstDayOfThisMonth(), Validators.required],
			endDate: [this.lastDayOfThisMonth(), Validators.required],
			specialtyId: [null],
			professionalId: [null],
			hierarchicalUnitTypeId: [null],
			hierarchicalUnitId: [null],
			includeHierarchicalUnitDescendants: [null]
		});
		this.professionalLicenseService.getAllProfessionalRegistrationNumbers().subscribe(professionals => {
			this.professionals = professionals;
			this.specialtiesTypeaheadOptions$ = this.getSpecialtiesTypeaheadOptions$(professionals);
			this.professionalsTypeahead = professionals.map(d => this.toProfessionalTypeahead(d));
		});
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			if (!anyMatch<ERole>(userRoles, [ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, ERole.PERSONAL_DE_ESTADISTICA]))
				this.REPORT_TYPES = this.REPORT_TYPES.filter(report => report.id != 1 && report.id != 2);
		});
		this.hierarchicalUnitsService.getByInstitution().subscribe(hierarchicalUnits => {
			this.hierarchicalUnits = hierarchicalUnits;
			this.hierarchicalUnitsTypeahead = hierarchicalUnits.map(hu => this.toHierarchicalUnitTypeahead(hu));
		});
		this.hierarchicalUnitTypeService.getByInstitution().subscribe( hierarchicalUnitTypes => this.hierarchicalUnitTypesTypeahead = hierarchicalUnitTypes.map(hut => this.toHierarchicalUnitTypeTypeahead(hut)));

		this.professionalLicenseService.getLicensesType().subscribe(licensesTypeMasterData => {
			this.licensesTypeMasterData = licensesTypeMasterData;
		});
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
		this.professionalInitValue = null;
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
			this.form.controls.hierarchicalUnitId.setValue(null);		}
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
			if (endDate.isBefore(this.form.value.startDate)) {
				this.form.controls.endDate.setErrors({ min: true });
				this.form.controls.startDate.setErrors({ max: true });
			} else {
				this.form.controls.endDate.setErrors(null);
				this.checkStartDateIsSameOrBeforeToday();
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
			const params = {
				startDate: this.form.controls.startDate.value,
				endDate: this.form.controls.endDate.value,
				specialtyId: this.form.controls.specialtyId.value,
				professionalId: this.form.controls.professionalId.value,
				hierarchicalUnitTypeId: this.form.controls.hierarchicalUnitTypeId.value,
				hierarchicalUnitId: this.form.controls.hierarchicalUnitId.value,
				includeHierarchicalUnitDescendants: this.form.controls.includeHierarchicalUnitDescendants.value
			}
			const reportId = this.form.controls.reportType.value;
			switch (reportId) {
				case 1:
					this.reportsService.getMonthlyReport(params, `${this.REPORT_TYPES[0].description}.xls`).subscribe(() => this.isLoadingRequestReport = false);
					break;
				case 2:
					this.reportsService.getOutpatientSummaryReport(params, `${this.REPORT_TYPES[1].description}.xls`).subscribe(() => this.isLoadingRequestReport = false);
					break;
				case 3:
					this.reportsService.getDiabetesReport().subscribe(result => {
						this.cubeReportData = result
						this.isLoadingRequestReport = false
					});
					break;
				case 4:
					this.reportsService.getHypertensionReport().subscribe(result => {
						this.cubeReportData = result
						this.isLoadingRequestReport = false
					});
					break;
				case 5:
					this.reportsService.getEpidemiologicalWeekReport().subscribe(result => {
						this.cubeReportData = result
						this.isLoadingRequestReport = false
					});
					break;
				default:
			}
		}
	}

	resetCubeReport() {
		this.cubeReportData = null;
	}

}

import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ClinicalSpecialtyDto, DepartmentDto, DiaryAvailableAppointmentsDto, InstitutionBasicInfoDto, ProfessionalDto, SharedSnomedDto } from '@api-rest/api-model';
import { ProfessionalFilters } from '@api-rest/services/healthcare-professional.service';
import { SearchAppointmentsForThirdPartyDataService } from '@call-center/services/search-appointments-for-third-party-data.service';
import { getFiltersToSearch } from '@call-center/utils/search-appointments-third-party';
import { datePlusDays } from '@core/utils/date.utils';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import { DiaryAvailableAppointmentsSearchService, ThirdPartyAppointmentFilter } from '@turnos/services/diary-available-appointments-search.service';
import differenceInDays from 'date-fns/differenceInDays';

@Component({
	selector: 'app-search-appointments-for-third-party',
	templateUrl: './search-appointments-for-third-party.component.html',
	styleUrls: ['./search-appointments-for-third-party.component.scss'],
	providers: [SearchAppointmentsForThirdPartyDataService]
})
export class SearchAppointmentsForThirdPartyComponent implements OnInit {
	readonly MAX_DAYS = 10;

	form: FormGroup<ThirdPartyAppointmentSearchForm>;

	initialDateRange: DateRange;
	showResults = false;
	showValidations = {
		noDate: false,
		periodRange: false,
		criteriaSearch: false,
		department: false
	};

	professionalFilters: ProfessionalFilters;

	availableAppointments: DiaryAvailableAppointmentsDto[] = [];
	today = new Date();

	constructor(
		readonly searchAppointmentsForThirdPartyDataService: SearchAppointmentsForThirdPartyDataService,
		private readonly diaryAvailableAppointmentsSearchService: DiaryAvailableAppointmentsSearchService,
	) { }

	ngOnInit(): void {
		this.createForm();
		this.setInitialDateRange();
		this.searchAppointmentsForThirdPartyDataService.setDepartmentsTypeahead();
	}

	verifyDateRangeNotExceedMaxiumDaysAndSetDate(dateRange: DateRange) {
		this.resetResults();
		this.showValidations.periodRange = false;
		this.showValidations.noDate = false;
		if (dateRange === null){
			this.showValidations.noDate = true;
			return
		}
		if (differenceInDays(dateRange.end, dateRange.start) > this.MAX_DAYS) {
			this.showValidations.periodRange = true;
			return;
		}
		this.setDateRange(dateRange);
	}

	submit() {
		if (this.invalidSearch()) {
			this.showResults = false;
			return;
		}

		this.showValidations.criteriaSearch = false;
		this.showValidations.department = false;
		this.setAvailableAppointments();

	}

	setDepartment(department: DepartmentDto) {
		this.professionalFilters = { departmentId: department?.id};
		this.showValidations.department = false;
		this.form.controls.departmentId.setValue(department?.id);
		this.resetResults();
		this.form.controls.professionalId.setValue(null);
		this.searchAppointmentsForThirdPartyDataService.setDepartmentIdAndTypeaheadOptions(department?.id);
		this.searchAppointmentsForThirdPartyDataService.setProfessionalTypeaheadOptions(this.professionalFilters);
		if (!department) {
			const excludeControls = ['startDate', 'endDate'];
			this.clearForm(this.form.controls, excludeControls);
		}
	}

	setInstitution(institution: InstitutionBasicInfoDto) {
		this.professionalFilters = {
			departmentId: this.professionalFilters.departmentId,
			institutionId: institution?.id
		};
		this.form.controls.institutionId.setValue(institution?.id);
		this.resetResults();
		this.form.controls.professionalId.setValue(null);
		this.searchAppointmentsForThirdPartyDataService.setInstitutionIdAndTypeaheadOptions(institution?.id);
		this.searchAppointmentsForThirdPartyDataService.setProfessionalTypeaheadOptions(this.professionalFilters);
		if (!institution) {
			const excludeControls = ['startDate', 'endDate', 'departmentId'];
			this.clearForm(this.form.controls, excludeControls);
		}
	}

	setSpecialty(specialty: ClinicalSpecialtyDto) {
		this.professionalFilters = {
			departmentId: this.professionalFilters.departmentId,
			institutionId: this.professionalFilters?.institutionId,
			practiceId: this.professionalFilters?.practiceId,
			clinicalSpecialtyId: specialty?.id
		};
		this.resetResults();
		this.form.controls.professionalId.setValue(null);
		this.form.controls.specialtyId.setValue(specialty?.id);
		this.showValidations.criteriaSearch = false;
		this.searchAppointmentsForThirdPartyDataService.setProfessionalTypeaheadOptions(this.professionalFilters);
	}

	setProfessional(professional: ProfessionalDto) {
		this.resetResults();
		this.form.controls.professionalId.setValue(professional?.id);
		this.showValidations.criteriaSearch = false;
	}

	setPractice(practice: SharedSnomedDto) {
		this.professionalFilters = {
			departmentId: this.professionalFilters.departmentId,
			institutionId: this.professionalFilters?.institutionId,
			clinicalSpecialtyId: this.professionalFilters?.clinicalSpecialtyId,
			practiceId: practice?.id
		};
		this.resetResults();
		this.form.controls.professionalId.setValue(null);
		this.form.controls.practiceId.setValue(practice?.id);
		this.showValidations.criteriaSearch = false;
		this.searchAppointmentsForThirdPartyDataService.setProfessionalTypeaheadOptions(this.professionalFilters);
	}

	resetResults() {
		this.availableAppointments = [];
		this.showResults = false;
	}

	private invalidSearch(): boolean {
		const hasCriteriaSearch = this.form.value.specialtyId || this.form.value.professionalId || this.form.value.practiceId;

		if (!hasCriteriaSearch)
			this.showValidations.criteriaSearch = true;

		if (!this.form.valid)
			this.showValidations.department = true;

		return !hasCriteriaSearch || this.showValidations.periodRange || this.showValidations.noDate || !this.form.valid;
	}

	private setAvailableAppointments() {

		const filters: ThirdPartyAppointmentFilter = getFiltersToSearch(this.form);

		this.diaryAvailableAppointmentsSearchService.getAvailableAppointmentsToThirdPartyBooking(filters).subscribe(
			(availableAppointments: DiaryAvailableAppointmentsDto[]) => {
				this.availableAppointments = availableAppointments;
				this.showResults = true;
			}
		);
	}

	private setInitialDateRange() {
		this.initialDateRange = {
			start: this.today,
			end: datePlusDays(this.today, this.MAX_DAYS)
		}
		this.setDateRange(this.initialDateRange);
	}

	private setDateRange(dateRange: DateRange) {
		this.form.controls.startDate.setValue(dateRange.start);
		this.form.controls.endDate.setValue(dateRange.end);
	}

	private clearForm(controls: ThirdPartyAppointmentSearchForm, excludeControlsName?: string[]) {
		Object.keys(controls).forEach(key => {
			if (!excludeControlsName.find(excludeControlName => excludeControlName == key)) {
				controls[key].setValue(null);
			}
		});
	}

	private createForm() {
		this.form = new FormGroup<ThirdPartyAppointmentSearchForm>({
			departmentId: new FormControl(null, Validators.required),
			institutionId: new FormControl(null),
			specialtyId: new FormControl(null),
			professionalId: new FormControl(null),
			practiceId: new FormControl(null),
			startDate: new FormControl(null, Validators.required),
			endDate: new FormControl(null, Validators.required),
		});
	}
}

export interface ThirdPartyAppointmentSearchForm {
	departmentId: FormControl<number>;
	institutionId: FormControl<number>;
	specialtyId: FormControl<number>;
	professionalId: FormControl<number>;
	practiceId: FormControl<number>;
	startDate: FormControl<Date>;
	endDate: FormControl<Date>;
}

import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AddressDto, AppFeature, CareLineDto, ClinicalSpecialtyDto, DepartmentDto, DiaryAvailableAppointmentsDto, EAppointmentModality, InstitutionBasicInfoDto, ProvinceDto, ReferenceInstitutionDto, SharedSnomedDto, SnomedDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { CareLineService } from '@api-rest/services/care-line.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { SpecialtyService } from '@api-rest/services/specialty.service';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { datePlusDays } from '@core/utils/date.utils';
import { DEFAULT_COUNTRY_ID } from '@core/utils/form.utils';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { DiaryAvailableAppointmentsSearchService, ProtectedAppointmentsFilter } from '@turnos/services/diary-available-appointments-search.service';
import { SearchCriteria } from '../search-criteria/search-criteria.component';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { CareLineInstitutionPracticeService } from '@api-rest/services/care-line-institution-practice.service';
import { TabsService } from '@turnos/services/tabs.service';
import { TabsLabel } from '@turnos/constants/tabs';
import { SearchAppointmentInformation, SearchAppointmentsInfoService } from '@access-management/services/search-appointment-info.service';
import { listToTypeaheadOptions } from '@presentation/utils/typeahead.mapper.utils';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { toApiFormat } from '@api-rest/mapper/date.mapper';

const PERIOD_DAYS = 7;
const PAGE_SIZE_OPTIONS = [5, 10, 25, 100];
const ONE_ELEMENT = 1;

@Component({
	selector: 'app-search-appointments-in-care-network',
	templateUrl: './search-appointments-in-care-network.component.html',
	styleUrls: ['./search-appointments-in-care-network.component.scss']
})
export class SearchAppointmentsInCareNetworkComponent implements OnInit {

	searchForm: UntypedFormGroup;
	provinces: ProvinceDto[] = [];
	departments: DepartmentDto[] = [];
	institutions: InstitutionBasicInfoDto[] = [];
	careLines: CareLineDto[] = [];
	specialties: ClinicalSpecialtyDto[] = [];
	allSpecialties: ClinicalSpecialtyDto[] = [];
	readonly today = new Date();

	showAppointmentsNotFoundMessage = false;
	showAppointmentResults = false;
	showInvalidFormMessage = false;

	showSpecialtyError = false;
	showDepartmentError = false;
	showProvinceError = false;
	showCareLineError = false;

	careLineTypeaheadOptions: TypeaheadOption<CareLineDto>[] = [];
	specialtyTypeaheadOptions: TypeaheadOption<ClinicalSpecialtyDto>[] = [];
	departmentTypeaheadOptions: TypeaheadOption<DepartmentDto>[] = [];
	institutionTypeaheadOptions: TypeaheadOption<InstitutionBasicInfoDto>[] = [];
	provinceTypeaheadOptions: TypeaheadOption<ProvinceDto>[] = [];

	initialProvinceTypeaheadOptionSelected: TypeaheadOption<ProvinceDto>;
	initialDepartmentTypeaheadOptionSelected: TypeaheadOption<DepartmentDto>;
	initialInstitutionTypeaheadOptionSelected: TypeaheadOption<InstitutionBasicInfoDto>;
	externalSpecialty: TypeaheadOption<ClinicalSpecialtyDto>;

	protectedAvaibleAppointments: DiaryAvailableAppointmentsDto[] = [];

	appointmentsCurrentPage: DiaryAvailableAppointmentsDto[] = [];
	readonly pageSizeOptions = PAGE_SIZE_OPTIONS;
	pageSize: Observable<number>;
	patientId: number;
	careLineId: number;
	showModalityError: boolean = false;
	MODALITY_ON_SITE_ATTENTION = EAppointmentModality.ON_SITE_ATTENTION;
	MODALITY_PATIENT_VIRTUAL_ATTENTION = EAppointmentModality.PATIENT_VIRTUAL_ATTENTION;
	MODALITY_SECOND_OPINION_VIRTUAL_ATTENTION = EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION
	isEnableTelemedicina: boolean;
	searchCriteria = SearchCriteria;
	selectedTypeAttention = SearchCriteria.CONSULTATION;
	private practicesBehavior = new BehaviorSubject<SnomedDto[]>([]);
	practices$ = this.practicesBehavior.asObservable();
	showPracticeError = false;
	searchAppointmentCriteria: SearchAppointmentCriteria;
	externalInformation: SearchAppointmentInformation;
	showSectionToSearchAppointmentsInInstitution = false;

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private institutionService: InstitutionService,
		private contextService: ContextService,
		private addressMasterDataService: AddressMasterDataService,
		private careLineService: CareLineService,
		private specialtyService: SpecialtyService,
		private diaryAvailableAppointmentsSearchService: DiaryAvailableAppointmentsSearchService,
		private changeDetectorRef: ChangeDetectorRef,
		private readonly route: ActivatedRoute,
		private readonly featureFlagService: FeatureFlagService,
		private readonly careLineInstitutionPracticeService: CareLineInstitutionPracticeService,
		private readonly searchAppointmentsInfoService: SearchAppointmentsInfoService,
		private readonly tabsService: TabsService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_TELEMEDICINA).subscribe(isEnabled => this.isEnableTelemedicina = isEnabled);
	}

	ngOnInit(): void {
		this.initForm();

		this.route.queryParams.subscribe(qp => {
			this.patientId = Number(qp.idPaciente);
		});

		this.setInformationToSearchAppointments();
		this.setSearchCriteriaAppointment();
		const referenceinstitutionDestination: ReferenceInstitutionDto = this.externalInformation != null ? this.externalInformation.referenceCompleteData.institutionDestination : null;
		const institutionIdToLoaded = referenceinstitutionDestination?.id ? referenceinstitutionDestination.id : this.contextService.institutionId;

		this.institutionService.getInstitutionAddress(institutionIdToLoaded).subscribe(
			(institutionAddres: AddressDto) => {

				this.addressMasterDataService.getByCountry(DEFAULT_COUNTRY_ID).subscribe(
					provinces => {
						this.provinces = provinces;
						this.loadProvinceTypeaheadOptions();

						const foundState = this.provinces.find((province: ProvinceDto) => { return (province.id === institutionAddres.provinceId) });
						this.initialProvinceTypeaheadOptionSelected = foundState ? provinceToTypeaheadOption(foundState) : undefined;

						if (institutionAddres?.provinceId) {
							this.addressMasterDataService.getDepartmentsByProvince(institutionAddres.provinceId).subscribe(
								departments => {
									this.departments = departments;
									this.loadDepartmentTypeaheadOptions();

									const foundDepartment = departments.find((department: DepartmentDto) => { return (department.id === institutionAddres.departmentId) });
									this.initialDepartmentTypeaheadOptionSelected = departmentToTypeaheadOption(foundDepartment);

									this.institutionService.findByDepartmentId(foundDepartment.id).subscribe(
										(institutions) => {
											this.institutions = institutions;
											this.loadInstitutionTypeaheadOptions();

											const foundInstitution = this.institutions.find((institution: InstitutionBasicInfoDto) => { return (institution.id === institutionIdToLoaded) });
											this.initialInstitutionTypeaheadOptionSelected = institutionToTypeaheadOption(foundInstitution)
										}
									);
								}
							);
						}
					}
				);

			}
		);
	}

	setCareLine(careLine: CareLineDto) {
		this.resetResults();
		this.searchForm.controls.careLine.setValue(careLine);
		this.showCareLineError = false;
		if (careLine) {
			this.specialties = careLine.clinicalSpecialties;
			if (!this.externalInformation?.formInformation?.careLine)
				this.careLineInstitutionPracticeService.getPracticesByCareLine(careLine.id).subscribe(practices => this.practicesBehavior.next(practices))
		}
		else {
			this.specialties = this.allSpecialties;
			this.practicesBehavior.next([]);
		}

		if (!this.externalInformation?.formInformation.clinicalSpecialties) {
			this.searchForm.controls.specialty.reset();
			this.loadSpecialtyTypeaheadOptions();
		}
	}

	setClinicalSpecialty(clinicalSpecialty: ClinicalSpecialtyDto) {
		this.resetResults();
		this.searchForm.controls.specialty.setValue(clinicalSpecialty);
		this.showSpecialtyError = false;
	}

	setDepartment(department: DepartmentDto) {
		this.resetResults();
		this.searchForm.controls.department.setValue(department);
		this.showDepartmentError = false;

		this.institutions = [];
		this.institutionTypeaheadOptions = [];
		this.initialInstitutionTypeaheadOptionSelected = null;
		this.searchForm.controls.institution.reset();

		if (department) {
			this.institutionService.findByDepartmentId(department.id).subscribe(
				(institutions) => {
					this.institutions = institutions;
					this.loadInstitutionTypeaheadOptions();
				}
			);
		}
	}

	setInstitution(institution: InstitutionBasicInfoDto) {
		this.resetResults();
		this.searchForm.controls.institution.setValue(institution);
	}

	setProvince(province) {
		this.resetResults();
		this.searchForm.controls.state.setValue(province);
		this.showProvinceError = false;

		this.departments = [];
		this.departmentTypeaheadOptions = [];
		this.initialDepartmentTypeaheadOptionSelected = null;
		this.searchForm.controls.department.reset();
		this.setDepartment(null);

		if (province) {
			this.addressMasterDataService.getDepartmentsByProvince(province.id).subscribe(
				departments => {
					this.departments = departments;
					this.loadDepartmentTypeaheadOptions();
				}
			);
		}
	}

	updateEndDate(selectedDate: Date) {
		this.resetResults();
		this.searchForm.controls.startDate.setValue(selectedDate);
		this.searchForm.controls.endDate.setValue(datePlusDays(selectedDate, PERIOD_DAYS));
	}

	searchAppointments() {
		if (this.searchForm.valid) {
			this.showInvalidFormMessage = false;

			const filters: ProtectedAppointmentsFilter = {
				careLineId: this.searchForm.value.careLine.id,
				clinicalSpecialtyIds: this.searchForm.value.specialty?.id ? [this.searchForm.value.specialty.id] : null,
				departmentId: this.searchForm.value.department.id,
				endSearchDate: toApiFormat(this.searchForm.controls.endDate.value),
				initialSearchDate: toApiFormat(this.searchForm.controls.startDate.value),
				institutionId: this.searchForm.value.institution ? this.searchForm.value.institution.id : null,
				modality: this.searchForm.controls.modality.value,
				practiceId: this.searchForm.controls.practiceId.value
			};

			this.searchAppointmentCriteria = {
				careLineId: this.searchForm.value.careLine.id,
				practiceId: this.searchForm.controls.practiceId.value,
				specialtyId: this.searchForm.value.specialty?.id
			}

			this.diaryAvailableAppointmentsSearchService.getAvailableProtectedAppointments(filters).subscribe(
				(availableAppointments: DiaryAvailableAppointmentsDto[]) => {
					this.protectedAvaibleAppointments = availableAppointments;
					this.showAppointmentsNotFoundMessage = !this.protectedAvaibleAppointments?.length
					this.showAppointmentResults = !this.showAppointmentsNotFoundMessage;
					this.careLineId = this.searchForm.value.careLine.id;
					this.showSectionToSearchAppointmentsInInstitution = this.externalInformation?.enableSectionToSearchAppointmentInOtherTab;
					this.pageSize = of(this.pageSizeOptions[0]);
					if (this.showAppointmentResults) {
						this.loadFirstPage();
					}
				}
			);
		}
		else {
			this.showInvalidFormMessage = true;
			this.showAppointmentResults = this.showAppointmentsNotFoundMessage = false;
			this.showCareLineError = !this.searchForm.value.careLine;
			this.showDepartmentError = !this.searchForm.value.department;
			this.showProvinceError = !this.searchForm.value.state;
			this.showModalityError = !this.searchForm.value.modality;

			if (!this.searchForm.value.specialty && this.searchForm.controls.specialty.hasValidator(Validators.required)) {
				this.showSpecialtyError = true;
			}

			if (!this.searchForm.value.praticeId && this.searchForm.controls.practiceId.hasValidator(Validators.required)) {
				this.showPracticeError = true;
			}

		}

	}

	onPageChange($event: any): void {
		const startPage = $event.pageIndex * $event.pageSize;
		this.appointmentsCurrentPage = this.protectedAvaibleAppointments.slice(startPage, $event.pageSize + startPage);
	}

	clearForm() {
		this.externalSpecialty = null;
		this.externalInformation = null;
		this.patientId = null;
		this.setAllSpecialtiesAndCareLines();
		this.resetForm();
		this.showSectionToSearchAppointmentsInInstitution = false;
		this.searchAppointmentsInfoService.clearInfo();
		this.setCriteria(SearchCriteria.CONSULTATION);
		this.searchForm.controls.modality.setValue(this.MODALITY_ON_SITE_ATTENTION);
	}

	resetResults(): void {
		this.protectedAvaibleAppointments = [];
		this.appointmentsCurrentPage = [];
		this.showAppointmentResults = false;
	}

	setCriteria(selectedCriteria: SearchCriteria) {
		this.selectedTypeAttention = selectedCriteria;
		if (this.selectedTypeAttention === SearchCriteria.CONSULTATION)
			this.searchForm.controls.practiceId.setValue(null);
		this.resetResults();
		this.setValidators();
	}

	setPractice(practice: SharedSnomedDto) {
		if (practice)
			this.searchForm.controls.practiceId.setValue(practice.id);
		else {
			this.searchForm.controls.practiceId.setValue(null);
			this.resetResults();
		}
		this.showPracticeError = false;
	}

	searchAppointmentsInstitution() {
		this.searchAppointmentsInfoService.loadInformation(this.patientId, this.externalInformation.referenceCompleteData);
		this.tabsService.setTab(TabsLabel.INSTITUTION);
	}

	private setValidators() {
		if (this.selectedTypeAttention === SearchCriteria.CONSULTATION) {
			this.searchForm.controls.specialty.addValidators(Validators.required);
			this.searchForm.controls.specialty.updateValueAndValidity();
			this.searchForm.controls.practiceId.removeValidators(Validators.required);
			this.searchForm.controls.practiceId.updateValueAndValidity();
		}
		else {
			this.searchForm.controls.practiceId.addValidators(Validators.required);
			this.searchForm.controls.practiceId.updateValueAndValidity();
			this.searchForm.controls.specialty.removeValidators(Validators.required);
			this.searchForm.controls.specialty.updateValueAndValidity();
		}
		this.showSpecialtyError = false;
		this.showPracticeError = false;
	}

	private loadFirstPage(): void {
		this.appointmentsCurrentPage = this.protectedAvaibleAppointments.slice(0, PAGE_SIZE_OPTIONS[0]);
	}

	private loadSpecialtyTypeaheadOptions(): void {
		this.specialtyTypeaheadOptions = this.specialties?.map(specialtyToTypeaheadOption);
		this.changeDetectorRef.detectChanges();
	}

	private loadCareLineTypeaheadOptions(): void {
		this.careLineTypeaheadOptions = this.careLines?.map(careLineToTypeaheadOption);
		this.changeDetectorRef.detectChanges();
	}

	private loadDepartmentTypeaheadOptions(): void {
		this.departmentTypeaheadOptions = this.departments?.map(departmentToTypeaheadOption);
		this.changeDetectorRef.detectChanges();
	}

	private loadInstitutionTypeaheadOptions(): void {
		this.institutionTypeaheadOptions = this.institutions?.map(institutionToTypeaheadOption);
		this.changeDetectorRef.detectChanges();
	}

	private loadProvinceTypeaheadOptions(): void {
		this.provinceTypeaheadOptions = this.provinces?.map(provinceToTypeaheadOption);
		this.changeDetectorRef.detectChanges();
	}

	private initForm(): void {
		const endDate = datePlusDays(this.today, PERIOD_DAYS);
		this.searchForm = this.formBuilder.group({
			careLine: [null, Validators.required],
			specialty: [null, Validators.required],
			state: [null],
			department: [null, Validators.required],
			institution: [null],
			startDate: [this.today, Validators.required],
			endDate: [{ value: endDate, disabled: true }, Validators.required],
			modality: [this.MODALITY_ON_SITE_ATTENTION, Validators.required],
			practiceId: [null],
		});
	}

	private initCareLines(): void {
		this.careLineService.getCareLinesAttachedToInstitutions(this.contextService.institutionId).subscribe(
			(careLines: CareLineDto[]) => {
				this.careLines = careLines;
				this.loadCareLineTypeaheadOptions();
			}
		);
	}

	private initSpecialties(): void {
		this.specialtyService.getAll().subscribe(
			(specialties: ClinicalSpecialtyDto[]) => {
				this.allSpecialties = specialties;
				this.specialties = specialties;
				this.loadSpecialtyTypeaheadOptions();
			}
		);
	}

	private setReferenceInformation(): void {
		const { patientId, formInformation } = this.externalInformation;

		this.patientId = patientId;

		const { searchCriteria, careLine, clinicalSpecialties, practice } = formInformation;
		this.setCriteria(searchCriteria);
		this.setCareLineTypeaheadOptions(careLine);

		if (clinicalSpecialties?.length) {
			this.specialtyTypeaheadOptions = listToTypeaheadOptions(clinicalSpecialties, 'name');
			if (clinicalSpecialties.length === ONE_ELEMENT)
				this.externalSpecialty = this.specialtyTypeaheadOptions[0];
		}

		if (practice) {
			this.practicesBehavior.next([practice]);
			this.setPractice(practice);
		}

		this.searchAppointmentsInfoService.clearInfo();
	}

	private resetForm() {
		const formControls = this.searchForm.controls;
		formControls.specialty.setValue(null);
		formControls.specialty.enable();
		formControls.practiceId.setValue(null);
		formControls.practiceId.enable();
		formControls.careLine.setValue(null);
		formControls.careLine.enable();
	}

	private setCareLineTypeaheadOptions(careLine: TypeaheadOption<CareLineDto>) {
		this.careLineTypeaheadOptions = [careLine];
	}

	private setInformationToSearchAppointments() {
		this.externalInformation = this.searchAppointmentsInfoService.getSearchAppointmentInfo();

		if (!this.externalInformation)
			this.setAllSpecialtiesAndCareLines();
		else
			this.setReferenceInformation();
	}

	private setSearchCriteriaAppointment() {
		let values = this.searchAppointmentsInfoService.getSearchCriteriaValues();
		if (values) {
			this.setCriteria(values.searchCriteria);
			this.searchForm.controls.modality.setValue(values.careModality);
			const startDate = dateDtoToDate(values.startDate);
			this.searchForm.controls.startDate.setValue(startDate);
			this.updateEndDate(startDate);
		}
	}

	private setAllSpecialtiesAndCareLines() {
		this.initSpecialties();
		this.initCareLines();
	}

}

function departmentToTypeaheadOption(department: DepartmentDto): TypeaheadOption<DepartmentDto> {
	return {
		compareValue: department.description,
		value: department,
		viewValue: department.description
	};
}

function provinceToTypeaheadOption(province: ProvinceDto): TypeaheadOption<ProvinceDto> {
	return {
		compareValue: province.description,
		value: province,
		viewValue: province.description
	};
}

function institutionToTypeaheadOption(institution: InstitutionBasicInfoDto): TypeaheadOption<InstitutionBasicInfoDto> {
	return {
		compareValue: institution.name,
		value: institution,
		viewValue: institution.name
	};
}

function careLineToTypeaheadOption(careLine: CareLineDto): TypeaheadOption<CareLineDto> {
	return {
		compareValue: careLine.description,
		value: careLine,
		viewValue: careLine.description
	};
}

function specialtyToTypeaheadOption(specialty: ClinicalSpecialtyDto): TypeaheadOption<ClinicalSpecialtyDto> {
	return {
		compareValue: specialty.name,
		value: specialty,
		viewValue: specialty.name
	};
}

export interface SearchAppointmentCriteria {
	careLineId: number;
	practiceId?: number;
	specialtyId?: number;
}

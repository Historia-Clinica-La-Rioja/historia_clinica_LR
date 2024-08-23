import { SearchAppointmentInformation, SearchAppointmentsInfoService } from '@access-management/services/search-appointment-info.service';
import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, of, map } from 'rxjs';
import { Observable } from 'rxjs/internal/Observable';
import { Validators, AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { ProvinceDto, DepartmentDto, InstitutionBasicInfoDto, CareLineDto, ClinicalSpecialtyDto, DiaryAvailableAppointmentsDto, EAppointmentModality, AppFeature, SharedSnomedDto, SnomedDto, AddressDto } from '@api-rest/api-model';
import { AddressMasterDataService } from '@api-rest/services/address-master-data.service';
import { CareLineService } from '@api-rest/services/care-line.service';
import { InstitutionService } from '@api-rest/services/institution.service';
import { PracticesService } from '@api-rest/services/practices.service';
import { SpecialtyService } from '@api-rest/services/specialty.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { DiaryAvailableAppointmentsSearchService, ProtectedAppointmentsFilter } from '@turnos/services/diary-available-appointments-search.service';
import { datePlusDays } from '@core/utils/date.utils';
import { DEFAULT_COUNTRY_ID } from '@core/utils/form.utils';
import { listToTypeaheadOptions, objectToTypeaheadOption } from '@presentation/utils/typeahead.mapper.utils';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SearchCriteria } from '@turnos/components/search-criteria/search-criteria.component';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

const PERIOD_DAYS = 7;
const PAGE_SIZE_OPTIONS = [5, 10, 25, 100];
const ONE_ELEMENT = 1;

@Component({
	selector: 'app-search-appointments-for-regulation',
	templateUrl: './search-appointments-for-regulation.component.html',
	styleUrls: ['./search-appointments-for-regulation.component.scss']
})
export class SearchAppointmentsForRegulationComponent implements OnInit {

	searchForm: FormGroup<SearchAppointmentForRegulationForm>;
	allPractices: SnomedDto[] = [];
	allSpecialties: ClinicalSpecialtyDto[] = [];
	readonly today = new Date();

	showAppointmentsNotFoundMessage = false;
	showAppointmentResults = false;
	showInvalidFormMessage = false;

	showTypeaheadErrors = {
		specialty: false,
		department: false,
		province: false,
		practice: false
	}

	externalInformation: SearchAppointmentInformation;
	externalSpecialty: TypeaheadOption<ClinicalSpecialtyDto>;
	initialProvinceTypeaheadOptionSelected: TypeaheadOption<ProvinceDto>;
	initialDepartmentTypeaheadOptionSelected: TypeaheadOption<DepartmentDto>;
	initialInstitutionTypeaheadOptionSelected: TypeaheadOption<InstitutionBasicInfoDto>;

	careLineTypeaheadOptions$: Observable<TypeaheadOption<CareLineDto>[]>;
	specialtyTypeaheadOptions: TypeaheadOption<ClinicalSpecialtyDto>[] = [];
	departmentTypeaheadOptions$: Observable<TypeaheadOption<DepartmentDto>[]>;
	institutionTypeaheadOptions$: Observable<TypeaheadOption<InstitutionBasicInfoDto>[]>;
	provinceTypeaheadOptions$: Observable<TypeaheadOption<ProvinceDto>[]>;
	clearTypeaheadOption: TypeaheadOption<any>;

	protectedAvaibleAppointments: DiaryAvailableAppointmentsDto[] = [];

	appointmentsCurrentPage: DiaryAvailableAppointmentsDto[] = [];
	readonly pageSizeOptions = PAGE_SIZE_OPTIONS;
	pageSize: Observable<number>;
	showModalityError: boolean = false;
	appointmentModality = {
		onSiteAttetion: EAppointmentModality.ON_SITE_ATTENTION,
		vitualAttention: EAppointmentModality.PATIENT_VIRTUAL_ATTENTION,
		secondOpinionVirtualAttention: EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION
	}
	isEnableTelemedicina: boolean;
	searchCriteria = SearchCriteria;
	selectedTypeAttention = SearchCriteria.CONSULTATION;
	private practicesBehavior = new BehaviorSubject<SnomedDto[]>([]);
	practices$ = this.practicesBehavior.asObservable();
	searchAppointmentCriteria: RegulationSearchAppointmentCriteria;

	constructor(
		private institutionService: InstitutionService,
		private addressMasterDataService: AddressMasterDataService,
		private careLineService: CareLineService,
		private specialtyService: SpecialtyService,
		private diaryAvailableAppointmentsSearchService: DiaryAvailableAppointmentsSearchService,
		private readonly dateFormatPipe: DateFormatPipe,
		private readonly featureFlagService: FeatureFlagService,
		private readonly practicesService: PracticesService,
		private readonly searchAppointmentsInfoService: SearchAppointmentsInfoService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_TELEMEDICINA).subscribe(isEnabled => this.isEnableTelemedicina = isEnabled);
	}

	ngOnInit(): void {
		this.initSpecialties();
		this.initPractices();
		this.initForm();

		this.careLineTypeaheadOptions$ = this.careLineService.getCareLines().pipe(map(careLines => listToTypeaheadOptions(careLines, 'description')));

		this.provinceTypeaheadOptions$ = this.addressMasterDataService.getByCountry(DEFAULT_COUNTRY_ID).pipe(map(provinces => listToTypeaheadOptions(provinces, 'description')));

		this.setReferenceInformation();
	}

	setCareLine(careLine: CareLineDto) {
		this.resetResults();
		this.searchForm.controls.careLine.setValue(careLine);
		if (careLine) {
			if (!this.externalInformation){
				this.searchForm.controls.specialty.reset();
				this.specialtyTypeaheadOptions = listToTypeaheadOptions(careLine.clinicalSpecialties, 'name');
			}
			this.practicesService.getAll(careLine.id).subscribe(practices => this.practicesBehavior.next(practices));
		}
		else {
			if (!this.externalInformation) this.specialtyTypeaheadOptions = listToTypeaheadOptions(this.allSpecialties, 'name');
			this.practicesBehavior.next(this.allPractices);
		}
	}

	setClinicalSpecialty(clinicalSpecialty: ClinicalSpecialtyDto) {
		this.resetResults();
		this.searchForm.controls.specialty.setValue(clinicalSpecialty);
		this.showTypeaheadErrors.specialty = false;
		if (this.externalInformation && !clinicalSpecialty) {
			this.externalSpecialty = null;
			this.specialtyTypeaheadOptions = listToTypeaheadOptions(this.externalInformation.formInformation.clinicalSpecialties, 'name');
		}
	}

	setDepartment(department: DepartmentDto) {
		this.resetResults();
		this.searchForm.controls.department.setValue(department);
		this.showTypeaheadErrors.department = false;
		this.searchForm.controls.institution.reset();

		if (department)
			this.institutionTypeaheadOptions$ = this.institutionService.findByDepartmentId(department.id).pipe(map(
				institutions => listToTypeaheadOptions(institutions, 'name')));

		else this.initialDepartmentTypeaheadOptionSelected = null;
	}

	setInstitution(institution: InstitutionBasicInfoDto) {
		this.resetResults();
		this.searchForm.controls.institution.setValue(institution);
		if (!institution) this.initialInstitutionTypeaheadOptionSelected = undefined;
	}

	setProvince(province) {
		this.resetResults();
		this.searchForm.controls.state.setValue(province);
		this.showTypeaheadErrors.province = false;
		this.searchForm.controls.department.reset();
		this.setDepartment(null);

		if (province)
			this.departmentTypeaheadOptions$ = this.addressMasterDataService.getDepartmentsByProvince(province.id).pipe(map(
				departments => listToTypeaheadOptions(departments, 'description')));

		else this.initialProvinceTypeaheadOptionSelected = undefined;
	}

	updateEndDate(initialDate: Date) {
		this.resetResults();
		if (initialDate) {
			this.searchForm.controls.endDate.setValue(datePlusDays(initialDate, PERIOD_DAYS));
		}
	}

	searchAppointments() {
		if (this.searchForm.valid) {
			this.showInvalidFormMessage = false;

			const startDate = new Date(this.searchForm.controls.startDate.value);
			const endDate = new Date(this.searchForm.controls.endDate.value);
			const endDateString = this.dateFormatPipe.transform(endDate, 'date');
			const startDateString = this.dateFormatPipe.transform(startDate, 'date');

			const filters: ProtectedAppointmentsFilter = {
				careLineId: this.searchForm.value.careLine?.id,
				clinicalSpecialtyIds: this.searchForm.value.specialty?.id ? [this.searchForm.value.specialty.id] : null,
				departmentId: this.searchForm.value.department.id,
				endSearchDate: endDateString,
				initialSearchDate: startDateString,
				institutionId: this.searchForm.value.institution?.id,
				modality: this.searchForm.controls.modality.value,
				practiceId: this.searchForm.value.practiceId,
			};

			this.searchAppointmentCriteria = {
				careLineId: this.searchForm.value.careLine?.id,
				practiceId: this.searchForm.value.practiceId,
				specialtyId: this.searchForm.value.specialty?.id
			}

			this.diaryAvailableAppointmentsSearchService.getAvailableProtectedAppointments(filters).subscribe(
				(availableAppointments: DiaryAvailableAppointmentsDto[]) => {
					this.protectedAvaibleAppointments = availableAppointments;
					this.showAppointmentsNotFoundMessage = !this.protectedAvaibleAppointments?.length
					this.showAppointmentResults = !this.showAppointmentsNotFoundMessage;
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
			this.showTypeaheadErrors = {
				...this.showTypeaheadErrors,
				department: !this.searchForm.value.department,
				province: !this.searchForm.value.state
			}
			this.showModalityError = !this.searchForm.value.modality;
			if (!this.searchForm.value.specialty && this.searchForm.controls.specialty.hasValidator(Validators.required)) {
				this.showTypeaheadErrors.specialty = true;
			}

			if (!this.searchForm.value.practiceId && this.searchForm.controls.practiceId.hasValidator(Validators.required)) {
				this.showTypeaheadErrors.practice = true;
			}

		}

	}

	onPageChange($event: any): void {
		const startPage = $event.pageIndex * $event.pageSize;
		this.appointmentsCurrentPage = this.protectedAvaibleAppointments.slice(startPage, $event.pageSize + startPage);
	}

	clearForm() {
		this.selectedTypeAttention = SearchCriteria.CONSULTATION;
		this.setValidators();
		this.resetResults();
		this.clearTypeaheadOption = {
			...this.clearTypeaheadOption,
			value: null
		}
		this.resetForm();
		this.resetExternalInfo();
	}

	resetExternalInfo(): void {
		this.externalInformation = null;
		this.externalSpecialty = null;
		this.initialProvinceTypeaheadOptionSelected = null;
		this.initialDepartmentTypeaheadOptionSelected = null;
		this.initialInstitutionTypeaheadOptionSelected = null;
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
		this.showTypeaheadErrors.practice = false;
	}

	setReferenceInformation() {
		this.externalInformation = this.searchAppointmentsInfoService.getSearchAppointmentInfo();

		if (!this.externalInformation) return;

		const { formInformation } = this.externalInformation;
		const { searchCriteria, careLine, clinicalSpecialties, practice } = formInformation;

		this.setCriteria(searchCriteria);
		this.setCareLine(careLine.value);
		this.setExternalClincalSpecialtie(clinicalSpecialties);
		
		if (practice) {
			this.practicesBehavior.next([practice]);
			this.setPractice(practice);
		}

		this.getProvinceDepartamentInstitutionReference();

		this.searchAppointmentsInfoService.clearInfo();
	}

	setExternalClincalSpecialtie(clinicalSpecialties: ClinicalSpecialtyDto[]) {
		if (clinicalSpecialties?.length) {
			this.specialtyTypeaheadOptions = listToTypeaheadOptions(clinicalSpecialties, 'name');
			if (clinicalSpecialties.length > ONE_ELEMENT) this.externalInformation.disabledInput.specialty = false;
			else this.externalSpecialty = this.specialtyTypeaheadOptions[0];
		}
	}

	getProvinceDepartamentInstitutionReference() {
		this.institutionService.getInstitutionAddress(this.externalInformation.referenceCompleteData.institutionDestination.id).subscribe(
			(institutionAddress: AddressDto) => {
				
				this.findProvince(institutionAddress);
			}
		);
	}

	private findProvince(institutionAddress: AddressDto) {
		this.addressMasterDataService.getByCountry(DEFAULT_COUNTRY_ID).subscribe(
			provinces => {

				const foundState = provinces.find((province: ProvinceDto) => { return (province.id === institutionAddress.provinceId) });
				this.initialProvinceTypeaheadOptionSelected = foundState ? objectToTypeaheadOption(foundState, 'description') : undefined;

				if (institutionAddress.provinceId) {
					this.findDepartament(institutionAddress);
				}
			}
		);
	}

	private findDepartament(institutionAddress: AddressDto) {
		this.addressMasterDataService.getDepartmentsByProvince(institutionAddress.provinceId).subscribe(
			departaments => {
				const foundDepartament = departaments.find((departament: DepartmentDto) => { return (departament.id === institutionAddress.departmentId) });
				this.initialDepartmentTypeaheadOptionSelected = objectToTypeaheadOption(foundDepartament, 'description');
				this.findInstitution(foundDepartament);
			}
		);
	}

	private findInstitution(foundDepartament: any) {
		this.institutionService.findByDepartmentId(foundDepartament.id).subscribe(
			institutions => {

				const foundInstitution = institutions.find((institution: InstitutionBasicInfoDto) => {
					return (institution.id === this.externalInformation.referenceCompleteData.institutionDestination.id)
				});
				this.initialInstitutionTypeaheadOptionSelected = objectToTypeaheadOption(foundInstitution, 'name');
			}
		);
	}

	private setValidators() {
		if (this.selectedTypeAttention === SearchCriteria.CONSULTATION) {
			this.searchForm.controls.specialty.addValidators(Validators.required);
			this.searchForm.controls.practiceId.clearValidators();
		}
		else {
			this.searchForm.controls.practiceId.addValidators(Validators.required);
			this.searchForm.controls.specialty.clearValidators();
		}
		this.searchForm.controls.specialty.updateValueAndValidity();
		this.searchForm.controls.practiceId.updateValueAndValidity();
		this.showTypeaheadErrors.practice = false;
		this.showTypeaheadErrors.specialty = false;
	}

	private loadFirstPage(): void {
		this.appointmentsCurrentPage = this.protectedAvaibleAppointments.slice(0, PAGE_SIZE_OPTIONS[0]);
	}

	private initForm(): void {
		const endDate = datePlusDays(this.today, PERIOD_DAYS);

		this.searchForm = new FormGroup<SearchAppointmentForRegulationForm>({
			careLine: new FormControl(null),
			specialty: new FormControl(null, Validators.required),
			state: new FormControl(null, Validators.required),
			department: new FormControl(null, Validators.required),
			institution: new FormControl(null),
			startDate: new FormControl(this.today, Validators.required),
			endDate: new FormControl({ value: endDate, disabled: true }, Validators.required),
			modality: new FormControl(this.appointmentModality.onSiteAttetion, Validators.required),
			practiceId: new FormControl(null),
		});
	}

	private initSpecialties() {
		this.specialtyService.getAll().subscribe(
			(specialties: ClinicalSpecialtyDto[]) => {
				this.allSpecialties = specialties;
				if(!this.externalInformation?.referenceCompleteData.destinationClinicalSpecialties)
					this.specialtyTypeaheadOptions = listToTypeaheadOptions(this.allSpecialties, 'name');
			}
		);
	}

	private initPractices() {
		this.practicesService.getAll().subscribe(practices => {
			this.allPractices = practices;
			this.practicesBehavior.next(this.allPractices);
		})
	}

	private resetForm() {
		const formControls = this.searchForm.controls;
		this.resetAndEnableControls(formControls.specialty);
		this.resetAndEnableControls(formControls.practiceId);
		this.resetAndEnableControls(formControls.careLine);
		this.showTypeaheadErrors = {
			specialty: false,
			department: false,
			province: false,
			practice: false
		}
	}

	private resetAndEnableControls(control: AbstractControl) {
		control.setValue(null);
		control.enable();
	}

}

export interface RegulationSearchAppointmentCriteria {
	careLineId?: number;
	practiceId?: number;
	specialtyId?: number;
}


interface SearchAppointmentForRegulationForm {
	careLine: FormControl<CareLineDto>,
	modality: FormControl<EAppointmentModality>,
	specialty: FormControl<ClinicalSpecialtyDto>,
	practiceId: FormControl<number>,
	state: FormControl<ProvinceDto>,
	department: FormControl<DepartmentDto>,
	institution: FormControl<InstitutionBasicInfoDto>,
	startDate: FormControl<Date>,
	endDate: FormControl<Date>,
}


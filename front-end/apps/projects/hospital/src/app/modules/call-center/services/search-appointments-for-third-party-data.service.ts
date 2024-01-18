import { Injectable } from "@angular/core";
import { InstitutionBasicInfoDto, ClinicalSpecialtyDto, ProfessionalDto, SharedSnomedDto } from "@api-rest/api-model";
import { AddressProjection, AddressMasterDataService } from "@api-rest/services/address-master-data.service";
import { ClinicalSpecialtyService } from "@api-rest/services/clinical-specialty.service";
import { HealthcareProfessionalByInstitutionService } from "@api-rest/services/healthcare-professional-by-institution.service";
import { HealthcareProfessionalService } from "@api-rest/services/healthcare-professional.service";
import { InstitutionService } from "@api-rest/services/institution.service";
import { PracticesService } from "@api-rest/services/practices.service";
import { TypeaheadOption } from "@presentation/components/typeahead/typeahead.component";
import { listToTypeaheadOptions } from "@presentation/utils/typeahead.mapper.utils";
import { Observable, map } from "rxjs";

@Injectable()
export class SearchAppointmentsForThirdPartyDataService {

	departmentTypeaheadOptions$: Observable<TypeaheadOption<AddressProjection>[]>;
	institutionTypeaheadOptions: TypeaheadOption<InstitutionBasicInfoDto>[] = [];
	specialtyTypeaheadOptions: TypeaheadOption<ClinicalSpecialtyDto>[] = [];
	professionalTypeaheadOptions: TypeaheadOption<ProfessionalDto>[] = [];
	practiceTypeaheadOptions: TypeaheadOption<SharedSnomedDto>[] = [];

	departmentId: number;
	institutionId: number;

	constructor(
		private readonly addressMasterDataService: AddressMasterDataService,
		private readonly institutionService: InstitutionService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly practicesService: PracticesService,
		private readonly healthCareProfessionalByInstitutionService: HealthcareProfessionalByInstitutionService,
		private readonly healthCareProfessionalService: HealthcareProfessionalService,
	) { }

	setDepartmentIdAndTypeaheadOptions(departmentId: number) {
		this.departmentId = departmentId;
		if (this.departmentId) {
			this.setInstitutionsTypeaheadByDepartment();
			this.setTypeaheadsOptions(departmentId, FILTER_BY.DEPARTMENT);
		}
		else
			this.clearTypeaheads();
	}

	setInstitutionIdAndTypeaheadOptions(institutionId: number) {
		this.institutionId = institutionId;
		this.institutionId ? this.setTypeaheadsOptions(institutionId, FILTER_BY.INSTITUTION) : this.setTypeaheadsOptions(this.departmentId, FILTER_BY.DEPARTMENT);
	}

	setDepartmentsTypeahead() {
		this.departmentTypeaheadOptions$ = this.addressMasterDataService.getDepartmentsByInstitutions().pipe(
			map(departments => listToTypeaheadOptions(departments, 'description')));
	}

	private setTypeaheadsOptions(id: number, filterBy: FILTER_BY) {
		this.setSpecialtyTypeaheadOption(id, filterBy);
		this.setProfessionalTypeaheadOptions(id, filterBy);
		this.setPracticesTypeaheadOptions(id, filterBy);
	}

	private setInstitutionsTypeaheadByDepartment() {
		this.getTypeaheadOptionsObservable<InstitutionBasicInfoDto>(this.institutionService, METHOD_NAMES.INSTITUTIONS, this.departmentId, 'name')
			.subscribe(institutions => this.institutionTypeaheadOptions = institutions);
	}

	private setSpecialtyTypeaheadOption(id: number, filterBy: FILTER_BY) {
		const methodName = filterBy === FILTER_BY.DEPARTMENT ? METHOD_NAMES.CLINICAL_SPECIALTIES_BY_DEPARTMENT : METHOD_NAMES.CLINICAL_SPECIALTIES_BY_INSTITUTION;
		this.getTypeaheadOptionsObservable<ClinicalSpecialtyDto>(this.clinicalSpecialtyService, methodName, id, 'name')
			.subscribe(specialties => this.specialtyTypeaheadOptions = specialties);
	}

	private setProfessionalTypeaheadOptions(id: number, filterBy: FILTER_BY) {
		const methodName = filterBy === FILTER_BY.DEPARTMENT ? METHOD_NAMES.PROFESSIONALS_BY_DEPARTMENT : METHOD_NAMES.PROFESSIONALS_BY_INSTITUTION;
		const service = filterBy === FILTER_BY.DEPARTMENT ? this.healthCareProfessionalService : this.healthCareProfessionalByInstitutionService;

		this.getTypeaheadOptionsObservable<ProfessionalDto>(service, methodName, id, 'fullName')
			.subscribe(healthCareProfessionals => this.professionalTypeaheadOptions = healthCareProfessionals);
	}

	private setPracticesTypeaheadOptions(id: number, filterBy: FILTER_BY) {
		const methodName = filterBy === FILTER_BY.DEPARTMENT ? METHOD_NAMES.PRACTICES_BY_DEPARTMENT : METHOD_NAMES.PRACTICES_BY_INSTITUTION;
		this.getTypeaheadOptionsObservable<SharedSnomedDto>(this.practicesService, methodName, id, 'pt')
			.subscribe(practices => this.practiceTypeaheadOptions = practices);
	}

	private getTypeaheadOptionsObservable<T>(service: Service, methodName: METHOD_NAMES, id: number, key: string): Observable<TypeaheadOption<T>[]> {
		return service[methodName](id).pipe(map((list: T[]) => listToTypeaheadOptions(list, key)));
	}

	private clearTypeaheads() {
		this.practiceTypeaheadOptions = [];
		this.specialtyTypeaheadOptions = [];
		this.professionalTypeaheadOptions = [];
		this.institutionTypeaheadOptions = [];
	}
}

enum FILTER_BY {
	DEPARTMENT, INSTITUTION
}

enum METHOD_NAMES {
	CLINICAL_SPECIALTIES_BY_INSTITUTION = 'getClinicalSpecialtyByInstitution',
	CLINICAL_SPECIALTIES_BY_DEPARTMENT = 'getClinicalSpecialtiesByDepartmentId',
	PRACTICES_BY_INSTITUTION = 'get',
	PRACTICES_BY_DEPARTMENT = 'getAllByDepartment',
	PROFESSIONALS_BY_INSTITUTION = 'getAllByDestinationInstitution',
	PROFESSIONALS_BY_DEPARTMENT = 'getAllByDepartment',
	INSTITUTIONS = "findByDepartmentId"
}

type Service = ClinicalSpecialtyService | PracticesService | HealthcareProfessionalByInstitutionService | HealthcareProfessionalService | InstitutionService;
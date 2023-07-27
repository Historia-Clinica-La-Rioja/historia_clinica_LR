import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DuplicatePatientDto, IdentificationTypeDto } from '@api-rest/api-model';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { ContextService } from '@core/services/context.service';
import { Observable, of } from 'rxjs';
import { capitalize } from "@core/utils/core.utils";
import { REMOVE_SUBSTRING_DNI } from '@core/constants/validation-constants';
import { UntypedFormGroup } from '@angular/forms';

const ROUTE_PATIENTS_FUSION = "auditoria/fusionar-pacientes"
const PAGE_SIZE_OPTIONS = [5, 10, 25];
const PAGE_MIN_SIZE = 5;
const PAGE_SIZE_MIN = [5];

@Component({
	selector: 'app-list-card-patient-duplicate',
	templateUrl: './list-card-patient-duplicate.component.html',
	styleUrls: ['./list-card-patient-duplicate.component.scss']
})
export class ListCardPatientDuplicateComponent implements OnInit {
	pageSizeOptions: number[] = PAGE_SIZE_OPTIONS;
	listPatientDuplicate: DuplicatePatientDto[]
	pageSliceObs$: Observable<DuplicatePatientDto[]>;
	numberOfPatients = 0;
	pageSlice: DuplicatePatientDto[];
	identificationTypeList: IdentificationTypeDto[];
	initialSize: Observable<any>;
	routePrefix: string;
	formFilter: UntypedFormGroup;
	panelOpenState = false;
	patientFilter:string;
	private applySearchFilter = '';
	private sizePageSelect = PAGE_MIN_SIZE;
	@Input() set setPatientDuplicate(listPatientDuplicate: DuplicatePatientDto[]) {
		this.listPatientDuplicate = listPatientDuplicate;
		this.pageSlice = this.listPatientDuplicate?.slice(0, PAGE_MIN_SIZE);
		this.numberOfPatients = this.listPatientDuplicate?.length || 0;
		this.initialSize = of(PAGE_MIN_SIZE);
		this.patientFilter = null;
	}

	constructor(private personMasterDataService: PersonMasterDataService, private router: Router, private contextService: ContextService) { this.routePrefix = 'institucion/' + this.contextService.institutionId + '/' }

	ngOnInit(): void {
		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				this.identificationTypeList = identificationTypes;
			});
	}

	onPageChange($event: any): void {
		const page = $event;
		const startPage = page.pageIndex * page.pageSize;
		this.pageSlice = this.listPatientDuplicate.slice(startPage, $event.pageSize + startPage);
	}

	getIdentificationType(value: number) {
		return this.identificationTypeList?.find(type => type.id === value).description
	}

	goToPatientFusion(patientToAudit: DuplicatePatientDto) {
		localStorage.setItem("patientToAudit", JSON.stringify(patientToAudit));
		this.router.navigate([this.routePrefix + ROUTE_PATIENTS_FUSION]);
	}

	getFullName(patient: DuplicatePatientDto): string {
		const names = [
			patient?.firstName,
			patient?.middleNames,
			patient?.lastName,
			patient?.otherLastNames
		].filter(name => name !== undefined && name.trim() !== '');

		const capitalizedNames = names.map(name => capitalize(name));
		return capitalizedNames.join(' ');
	}

	applyFilter($event: any): void {
		this.applySearchFilter = ($event.target as HTMLInputElement).value?.replace(REMOVE_SUBSTRING_DNI, '');
		this.upDateFilters();
	}

	upDateFilters(): void {
		this.applyFiltes();
		this.pageSlice = this.pageSlice.slice(0, this.sizePageSelect);
	}

	private applyFiltes(): void {
		this.pageSlice = this.filter();
		this.setPageSizeOptions();
	}

	private setPageSizeOptions(): void {
		this.pageSizeOptions = (this.pageSlice.length < PAGE_MIN_SIZE) ? PAGE_SIZE_MIN : PAGE_SIZE_OPTIONS;
		this.numberOfPatients = this.pageSlice.length;
		this.pageSlice.length = this.numberOfPatients;
	}

	private filter(): DuplicatePatientDto[] {
		let listFilter = this.listPatientDuplicate;
		if (this.applySearchFilter) {
			listFilter = listFilter.filter((e: DuplicatePatientDto) => this.getFullName(e).toLowerCase().includes(this.applySearchFilter.toLowerCase())
				|| e?.identificationNumber?.toString().includes(this.applySearchFilter))
		}
		return listFilter;
	}

}

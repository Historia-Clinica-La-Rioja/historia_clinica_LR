import { Component, OnInit } from '@angular/core';
import { DuplicatePatientDto, MasterDataDto } from '@api-rest/api-model';
import { AuditPatientService } from '@api-rest/services/audit-patient.service';


@Component({
	selector: 'app-patients-fusion',
	templateUrl: './patients-fusion.component.html',
	styleUrls: ['./patients-fusion.component.scss']
})
export class PatientsFusionComponent implements OnInit {
	filters=Filters;
	searchsFilters: MasterDataDto[];
	filterSelected: Filters;
	listDuplicatePatient: DuplicatePatientDto[];
	constructor(private auditPatientService: AuditPatientService) {
		this.filterSelected = this.filters.FILTER_FULLNAME_DNI;
	}

	ngOnInit(): void {
		this.getListByFilter(this.filterSelected)
	}

	getListByFilter(value) {
		let filter = this.prepareCustomFilter(value);
		this.auditPatientService.getDuplicatePatientSearchFilter(filter).subscribe((listDuplicatePatient: DuplicatePatientDto[]) => {
			this.listDuplicatePatient = listDuplicatePatient;
		})
	}

	prepareCustomFilter(value) {
		let filter: AuditPatientSearch;
		switch (value) {
			case this.filters.FILTER_FULLNAME_DNI:
				filter = {
					name: true,
					birthdate: false,
					identify: true,
				}
				break;
			case this.filters.FILTER_FULLNAME_BIRTHDATE:
				filter = {
					name: true,
					birthdate: true,
					identify: false,
				}
				break;
			case this.filters.FILTER_FULLNAME_BIRTHDATE_DNI:
				filter = {
					name: true,
					birthdate: true,
					identify: true,
				}
				break;
			case this.filters.FILTER_DNI:
				filter = {
					name: false,
					birthdate: false,
					identify: true,
				}
				break;
		}
		return filter;
	}
}
export interface AuditPatientSearch {
	name: boolean;
	birthdate: boolean;
	identify: boolean;
}
export enum Filters{
	FILTER_FULLNAME_DNI,
	FILTER_FULLNAME_BIRTHDATE,
	FILTER_FULLNAME_BIRTHDATE_DNI,
	FILTER_DNI,
}

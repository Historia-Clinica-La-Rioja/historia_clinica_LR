import { Component, OnInit } from '@angular/core';
import { AuditPatientSearchDto , MasterDataDto } from '@api-rest/api-model';


@Component({
	selector: 'app-patients-fusion',
	templateUrl: './patients-fusion.component.html',
	styleUrls: ['./patients-fusion.component.scss']
})
export class PatientsFusionComponent implements OnInit {

	searchsFilters: MasterDataDto[];
	filterSelected: string;
	constructor() {
		this.filterSelected='1';
	}

	ngOnInit(): void {
	}

	getListByFilter(event) {
		let filter: AuditPatientSearchDto ;
		switch (event.value) {
			case '1':
				filter = {
					name: true,
					birthdate: true,
					identify:false,
				}
				break;
			case '2':
				filter = {
					name: true,
					birthdate: true,
					identify:false,
				}
				break;
			case '3':
			filter= {
				name:true,
				birthdate:true,
				identify:true,
			}
			break;
			case '4':
			filter= {
				name:false,
				birthdate:false,
				identify:true,
			}
			break;
		}
	}

}

import { Component, OnInit, Input } from '@angular/core';
import { MatTableDataSource } from "@angular/material/table";
import { PatientSearchDto } from '@api-rest/api-model';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';

@Component({
	selector: 'app-partial-matches-table',
	templateUrl: './partial-matches-table.component.html',
	styleUrls: ['./partial-matches-table.component.scss']
})
export class PartialMatchesTableComponent implements OnInit {

	public genderOptions={};
	@Input('matchingPatient') matchingPatient: PatientSearchDto[];

	displayedColumns: string[] = ['ID', 'Nombres', 'Apellidos', 'Sexo', 'F. Nac', 'DNI', 'Estado', 'Coincidencia'];

	constructor(
		private personMasterDataService: PersonMasterDataService
		) { }

	ngOnInit(): void {
		this.personMasterDataService.getGenders().subscribe(
			genders => { 
				genders.forEach(gender => {
					this.genderOptions[gender.id]=gender.description
				});
		});
	}

	getPatient(patient): void {
		//TODO display popup
	}

}

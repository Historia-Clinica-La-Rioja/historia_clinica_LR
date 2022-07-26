import { Component, Input, OnInit } from '@angular/core';
import { HealthcareProfessionalSpecialtyDto } from '@api-rest/api-model';

@Component({
	selector: 'app-card-professions',
	templateUrl: './card-professions.component.html',
	styleUrls: ['./card-professions.component.scss']
})
export class CardProfessionsComponent implements OnInit {

	@Input() ownProfessionsAndSpecialties: HealthcareProfessionalSpecialtyDto[];


	constructor() { }

	ngOnInit(): void {
	}
	hasProfessions(): boolean {
		return this.ownProfessionsAndSpecialties?.length > 0;
	}

}

import { Component, Input, OnInit } from '@angular/core';
import { ProfessionAndSpecialtyDto } from '@pacientes/routes/profile/profile.component';

@Component({
	selector: 'app-card-professions',
	templateUrl: './card-professions.component.html',
	styleUrls: ['./card-professions.component.scss']
})
export class CardProfessionsComponent implements OnInit {

	@Input() ownProfessionsAndSpecialties: ProfessionAndSpecialtyDto[];

	@Input() license: string;

	constructor() { }

	ngOnInit(): void {
	}

	hasProfessions(): boolean {
		return this.ownProfessionsAndSpecialties?.length > 0;
	}

}

import { Component, Input, OnInit } from '@angular/core';
import { ProfessionalDto } from '@api-rest/api-model';

@Component({
	selector: 'app-professional-and-description',
	templateUrl: './professional-and-description.component.html',
	styleUrls: ['./professional-and-description.component.scss']
})
export class ProfessionalAndDescriptionComponent implements OnInit {

	@Input() title: string;
	@Input() professionalTitle: string;
	@Input() professionals: ProfessionalDto[];
	@Input() icon: string;

	constructor() { }

	ngOnInit(): void {
	}

}

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
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

	@Output() professionalChange = new EventEmitter();

	constructor() { }

	ngOnInit(): void {
	}

	changeProfessional (professional: ProfessionalDto): void {
		this.professionalChange.emit(professional);
	}
}

import { Component, Input, OnInit } from '@angular/core';
import { DiagnosesGeneralStateDto } from '@api-rest/api-model';

@Component({
	selector: 'app-elemento-diagnostico',
	templateUrl: './elemento-diagnostico.component.html',
	styleUrls: ['./elemento-diagnostico.component.scss']
})
export class ElementoDiagnosticoComponent implements OnInit {

	@Input()
	diagnosis: DiagnosesGeneralStateDto;

	constructor() { }

  	ngOnInit(): void {
  	}

}

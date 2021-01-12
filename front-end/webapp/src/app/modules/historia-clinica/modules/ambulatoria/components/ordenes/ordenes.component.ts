import { Component, Input, OnInit } from '@angular/core';
@Component({
	selector: 'app-ordenes',
	templateUrl: './ordenes.component.html',
	styleUrls: ['./ordenes.component.scss']
})
export class OrdenesComponent implements OnInit {

	@Input('patientId') patientId: number;

	constructor(
	) { }

	ngOnInit(): void {
	}

}

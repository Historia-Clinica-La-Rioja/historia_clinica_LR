import { Component, Input, OnInit } from '@angular/core';
import { INDICACIONES } from 'src/app/modules/historia-clinica/constants/summaries';

@Component({
  selector: 'app-card-indicaciones',
  templateUrl: './card-indicaciones.component.html',
  styleUrls: ['./card-indicaciones.component.scss']
})
export class CardIndicacionesComponent implements OnInit {

  public readonly indicaciones = INDICACIONES;

	@Input('patientId') patientId: number;

	constructor() { }

	ngOnInit(): void {
	}

  	openDialogNewRecommendation() {
		//TODO completar con pop-up nueva recomendacion
	}

}

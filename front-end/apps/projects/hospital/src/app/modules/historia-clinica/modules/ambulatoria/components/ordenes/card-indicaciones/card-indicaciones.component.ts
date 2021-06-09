import { Component, Input } from '@angular/core';
import { INDICACIONES } from '@historia-clinica/constants/summaries';

@Component({
  selector: 'app-card-indicaciones',
  templateUrl: './card-indicaciones.component.html',
  styleUrls: ['./card-indicaciones.component.scss']
})
export class CardIndicacionesComponent {

  public readonly indicaciones = INDICACIONES;

	@Input() patientId: number;

	constructor() { }


  	openDialogNewRecommendation() {
		// TODO completar con pop-up nueva recomendacion
	}

}

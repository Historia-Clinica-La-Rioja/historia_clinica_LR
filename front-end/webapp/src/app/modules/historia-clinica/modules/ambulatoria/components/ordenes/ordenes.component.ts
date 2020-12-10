import { Component, OnInit } from '@angular/core';
import { ESTUDIOS, INDICACIONES, ORDENES_MEDICACION } from 'src/app/modules/historia-clinica/constants/summaries';

@Component({
  selector: 'app-ordenes',
  templateUrl: './ordenes.component.html',
  styleUrls: ['./ordenes.component.scss']
})
export class OrdenesComponent implements OnInit {

  public readonly medicacion = ORDENES_MEDICACION;
  public readonly estudios = ESTUDIOS;
  public readonly indicaciones = INDICACIONES;

  constructor() { }

  ngOnInit(): void {
  }

  openDialogNewMedication() {
	//TODO completar con pop-up nuevo medicamento
	console.log("Nuevo medicamento");
  }

  openDialogNewstudies() {
	//TODO completar con pop-up nuevo estudio
	console.log("Nuevo estudio");
  }

  openDialogNewRecommendations() {
	//TODO completar con pop-up nueva recomendacion
	console.log("Nueva recomendacion");
  }

}

import { Component, OnInit } from '@angular/core';
import { ESTUDIOS, ORDENES_MEDICACION, RECOMENDACIONES } from 'src/app/modules/historia-clinica/constants/summaries';

@Component({
  selector: 'app-ordenes',
  templateUrl: './ordenes.component.html',
  styleUrls: ['./ordenes.component.scss']
})
export class OrdenesComponent implements OnInit {

  public readonly medicacion = ORDENES_MEDICACION;
  public readonly estudios = ESTUDIOS;
  public readonly recomendaciones = RECOMENDACIONES;

  constructor() { }

  ngOnInit(): void {
  }

}

import { Component, Input, OnInit } from '@angular/core';
import { isNumberOrDot } from '@core/utils/core.utils';
import { DatosAntropometricosNuevaConsultaService } from '../../services/datos-antropometricos-nueva-consulta.service';

@Component({
  selector: 'app-datos-antropometricos-nueva-consulta',
  templateUrl: './datos-antropometricos-nueva-consulta.component.html',
  styleUrls: ['./datos-antropometricos-nueva-consulta.component.scss']
})
export class DatosAntropometricosNuevaConsultaComponent implements OnInit {

  @Input() datosAntropometricosNuevaConsultaService: DatosAntropometricosNuevaConsultaService;
  @Input() showPreloadData: boolean = false;
  readonly isNumberOrDot = isNumberOrDot;

  constructor() { }

  ngOnInit(): void {
  }

}

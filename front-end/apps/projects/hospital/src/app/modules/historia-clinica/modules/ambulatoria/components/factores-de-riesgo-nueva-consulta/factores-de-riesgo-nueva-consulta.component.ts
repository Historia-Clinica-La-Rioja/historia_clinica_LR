import { Component, Input, OnInit } from '@angular/core';
import { isNumberOrDot } from '@core/utils/pattern.utils';
import { FactoresDeRiesgoNuevaConsultaService } from '../../services/factores-de-riesgo-nueva-consulta.service';

@Component({
  selector: 'app-factores-de-riesgo-nueva-consulta',
  templateUrl: './factores-de-riesgo-nueva-consulta.component.html',
  styleUrls: ['./factores-de-riesgo-nueva-consulta.component.scss']
})
export class FactoresDeRiesgoNuevaConsultaComponent implements OnInit {

  @Input() factoresDeRiesgoNuevaConsultaService: FactoresDeRiesgoNuevaConsultaService;
  @Input() showPreloadData: boolean = false;
  readonly isNumberOrDot = isNumberOrDot;

  constructor() { }

  ngOnInit(): void {
  }

}

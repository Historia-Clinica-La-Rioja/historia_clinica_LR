import { Component, Input, OnInit } from '@angular/core';
import { isNumberOrDot } from '@core/utils/pattern.utils';
import { FactoresDeRiesgoNuevaConsultaService } from '../../modules/ambulatoria/services/factores-de-riesgo-nueva-consulta.service';

@Component({
  selector: 'app-factores-de-riesgo-form',
  templateUrl: './factores-de-riesgo-form.component.html',
  styleUrls: ['./factores-de-riesgo-form.component.scss']
})
export class FactoresDeRiesgoFormComponent implements OnInit {

  @Input() factoresDeRiesgoNuevaConsultaService: FactoresDeRiesgoNuevaConsultaService;
  @Input() showPreloadData: boolean = false;
  readonly isNumberOrDot = isNumberOrDot;

  constructor() { }

  ngOnInit(): void {
  }

}

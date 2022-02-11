import { Component, Input, OnInit } from '@angular/core';
import { SignosVitalesNuevaConsultaService } from '../../services/signos-vitales-nueva-consulta.service';

@Component({
  selector: 'app-factores-de-riesgo-nueva-consulta',
  templateUrl: './factores-de-riesgo-nueva-consulta.component.html',
  styleUrls: ['./factores-de-riesgo-nueva-consulta.component.scss']
})
export class FactoresDeRiesgoNuevaConsultaComponent implements OnInit {

  @Input() signosVitalesNuevaConsultaService: SignosVitalesNuevaConsultaService;
  @Input() showPreloadData: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

}

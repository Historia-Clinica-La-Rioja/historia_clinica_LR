import { Component, Input, OnInit } from '@angular/core';
import { isNumberOrDot } from '@core/utils/pattern.utils';
import { FactoresDeRiesgoFormService } from '../../services/factores-de-riesgo-form.service';

@Component({
  selector: 'app-factores-de-riesgo-form',
  templateUrl: './factores-de-riesgo-form.component.html',
  styleUrls: ['./factores-de-riesgo-form.component.scss']
})
export class FactoresDeRiesgoFormComponent implements OnInit {

  @Input() factoresDeRiesgoFormService: FactoresDeRiesgoFormService;
  @Input() showPreloadData: boolean = false;
  readonly isNumberOrDot = isNumberOrDot;

  constructor() { }

  ngOnInit(): void {
  }

}

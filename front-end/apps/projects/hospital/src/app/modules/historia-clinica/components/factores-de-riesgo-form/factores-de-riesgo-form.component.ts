import { Component, Input, OnInit } from '@angular/core';
import { isNumberOrDot } from '@core/utils/pattern.utils';
import { FactoresDeRiesgoFormService } from '../../services/factores-de-riesgo-form.service';
import { BoxMessageInformation } from '../box-message/box-message.component';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-factores-de-riesgo-form',
  templateUrl: './factores-de-riesgo-form.component.html',
  styleUrls: ['./factores-de-riesgo-form.component.scss']
})
export class FactoresDeRiesgoFormComponent implements OnInit {

  @Input() factoresDeRiesgoFormService: FactoresDeRiesgoFormService;
  @Input() showPreloadData: boolean = false;
  @Input() showRecomendation: boolean = false;
  @Input() showTitle: boolean = false;

  boxMessageInfo: BoxMessageInformation;
  readonly isNumberOrDot = isNumberOrDot;

  constructor( private readonly translateService: TranslateService ) {
    this.boxMessageInfo= {
      title: "historia-clinica.include-previous-data-question.TITLE",
      question: "historia-clinica.include-previous-data-question.QUESTION",
      message: '',
      showButtons: true
    }
  }

  ngOnInit() {
    this.translateService.get('historia-clinica.include-previous-data-question.DESCRIPTION',
        { dataName: 'historia-clinica.factores-de-riesgo-form.NAME', date: this.factoresDeRiesgoFormService.getDate() }).subscribe(
          message => this.boxMessageInfo.message = message
        );
  }

  savePreloadData(save: boolean): void {
    if (save) {
      this.factoresDeRiesgoFormService.savePreloadedRiskFactorsData();
    }
    else {
      this.factoresDeRiesgoFormService.discardPreloadedRiskFactorsData();
    }
  }

}

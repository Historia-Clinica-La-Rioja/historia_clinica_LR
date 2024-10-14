import { Component, Input, OnChanges } from '@angular/core';
import { isNumberOrDot } from '@core/utils/pattern.utils';
import { FactoresDeRiesgoFormService } from '../../services/factores-de-riesgo-form.service';
import { TranslateService } from '@ngx-translate/core';
import { BoxMessageInformation } from '@presentation/components/box-message/box-message.component';

@Component({
  selector: 'app-factores-de-riesgo-form',
  templateUrl: './factores-de-riesgo-form.component.html',
  styleUrls: ['./factores-de-riesgo-form.component.scss']
})
export class FactoresDeRiesgoFormComponent implements OnChanges {

  @Input() factoresDeRiesgoFormService: FactoresDeRiesgoFormService;
  @Input() showPreloadData: boolean = false;
  @Input() showRecomendation: boolean = false;
  @Input() showTitle: boolean = false;

  boxMessageInfo: BoxMessageInformation;
  readonly isNumberOrDot = isNumberOrDot;

  constructor( private readonly translateService: TranslateService ) { }

  ngOnChanges() {
    this.boxMessageInfo= {
      title: "historia-clinica.include-previous-data-question.TITLE",
      question: "historia-clinica.include-previous-data-question.QUESTION",
      message: this.translateService.instant('historia-clinica.include-previous-data-question.DESCRIPTION',
        { dataName: this.translateService.instant('historia-clinica.factores-de-riesgo-form.NAME'),
          date: this.factoresDeRiesgoFormService.getDate() }),
      showButtons: true
    }
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

import { Component, Input } from '@angular/core';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { AntecedentesFamiliaresNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/antecedentes-familiares-nueva-consulta.service';
import { PersonalHistoriesNewConsultationService } from '@historia-clinica/modules/ambulatoria/services/personal-histories-new-consultation.service';

@Component({
  selector: 'app-background-list',
  templateUrl: './background-list.component.html',
  styleUrls: ['./background-list.component.scss']
})
export class BackgroundListComponent {
  @Input() service: AntecedentesFamiliaresNuevaConsultaService | PersonalHistoriesNewConsultationService;
  momentFormat = momentFormat;
  readonly DateFormat = DateFormat;
}

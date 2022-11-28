import { Component, Input } from '@angular/core';
import { AlergiasNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/alergias-nueva-consulta.service';

@Component({
  selector: 'app-allergy-list',
  templateUrl: './allergy-list.component.html',
  styleUrls: ['./allergy-list.component.scss']
})
export class AllergyListComponent {
  @Input() service: AlergiasNuevaConsultaService;
}
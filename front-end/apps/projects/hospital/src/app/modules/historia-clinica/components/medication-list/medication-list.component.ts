import { Component, Input } from '@angular/core';
import { MedicacionesNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service';
import { Color } from '@presentation/colored-label/colored-label.component';

@Component({
  selector: 'app-medication-list',
  templateUrl: './medication-list.component.html',
  styleUrls: ['./medication-list.component.scss']
})
export class MedicationListComponent {
  @Input() service: MedicacionesNuevaConsultaService;
  Color = Color;
}

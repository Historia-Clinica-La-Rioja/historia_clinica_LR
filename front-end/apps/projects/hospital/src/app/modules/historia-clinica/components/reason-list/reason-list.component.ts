import { Component, Input } from '@angular/core';
import { MotivoNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';

@Component({
  selector: 'app-reason-list',
  templateUrl: './reason-list.component.html',
  styleUrls: ['./reason-list.component.scss']
})
export class ReasonListComponent {
  @Input() service: MotivoNuevaConsultaService;
}

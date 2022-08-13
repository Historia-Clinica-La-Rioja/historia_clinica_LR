import { Component, Input } from '@angular/core';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';

@Component({
  selector: 'app-procedure-list',
  templateUrl: './procedure-list.component.html',
  styleUrls: ['./procedure-list.component.scss']
})
export class ProcedureListComponent {
  @Input() service: ProcedimientosService;
}

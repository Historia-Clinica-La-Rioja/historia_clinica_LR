import { Component, Input } from '@angular/core';
import { isNumberOrDot } from '@core/utils/pattern.utils';
import { DatosAntropometricosNuevaConsultaService } from '../../services/datos-antropometricos-nueva-consulta.service';

@Component({
  selector: 'app-datos-antropometricos-nueva-consulta',
  templateUrl: './datos-antropometricos-nueva-consulta.component.html',
  styleUrls: ['./datos-antropometricos-nueva-consulta.component.scss']
})
export class DatosAntropometricosNuevaConsultaComponent {

  @Input() datosAntropometricosNuevaConsultaService: DatosAntropometricosNuevaConsultaService;
  @Input() showPreloadData: boolean = false;
  readonly isNumberOrDot = isNumberOrDot;

  savePreloadData(save: boolean): void {
    if (save) {
      this.datosAntropometricosNuevaConsultaService.savePreloadedAnthropometricData()
    }
    else {
      this.datosAntropometricosNuevaConsultaService.discardPreloadedAnthropometricData();
    }
  }

}

import { Component, Input } from '@angular/core';
import { AmbulatoryConsultationReferenceService } from '@historia-clinica/modules/ambulatoria/services/ambulatory-consultation-reference.service';
import { OdontologyReferenceService } from '@historia-clinica/modules/odontologia/services/odontology-reference.service';

@Component({
  selector: 'app-reference-request-list',
  templateUrl: './reference-request-list.component.html',
  styleUrls: ['./reference-request-list.component.scss']
})
export class ReferenceRequestListComponent {
  @Input() service: AmbulatoryConsultationReferenceService | OdontologyReferenceService;

  trackByRequest(index, request) {
    return request.index;
  }
}

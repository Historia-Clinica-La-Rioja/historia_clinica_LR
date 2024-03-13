import { Component, Input } from '@angular/core';
import { ProfessionalService } from '@historia-clinica/services/professionals.service';
import { IDENTIFIER_CASES } from '../../../hsi-components/identifier-cases/identifier-cases.component';
import { Position } from '@presentation/components/identifier/identifier.component';

@Component({
  selector: 'app-professional-list',
  templateUrl: './professional-list.component.html',
  styleUrls: ['./professional-list.component.scss']
})
export class ProfessionalListComponent {
  @Input() service : ProfessionalService;
  Position = Position;
	identiferCases = IDENTIFIER_CASES;

  constructor() { }
}

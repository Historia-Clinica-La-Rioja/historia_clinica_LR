import { Component, Input } from '@angular/core';
import { DiaryAvailableAppointmentsDto } from '@api-rest/api-model';
import { IDENTIFIER_CASES, IdentifierCasesComponent } from '../../../hsi-components/identifier-cases/identifier-cases.component';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
  selector: 'app-available-appointment-data',
  templateUrl: './available-appointment-data.component.html',
  styleUrls: ['./available-appointment-data.component.scss'],
  standalone: true,
	imports: [IdentifierCasesComponent, PresentationModule],
})
export class AvailableAppointmentDataComponent {

  identiferCases = IDENTIFIER_CASES;

	@Input() appointment: DiaryAvailableAppointmentsDto;

  constructor() { }

}

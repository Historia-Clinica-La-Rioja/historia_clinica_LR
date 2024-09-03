import { Component, Input } from '@angular/core';
import { EstadosEpisodio } from '@historia-clinica/modules/guardia/constants/masterdata';
import { Color, ColoredLabel } from '@presentation/colored-label/colored-label.component';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
	selector: 'app-emergency-care-status-labels',
	templateUrl: './emergency-care-status-labels.component.html',
	styleUrls: ['./emergency-care-status-labels.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class EmergencyCareStatusLabelsComponent {

	coloredLabel: ColoredLabel;

	private states = {
		[EstadosEpisodio.EN_ESPERA]: WAITING,
		[EstadosEpisodio.EN_ATENCION]: IN_ATTETION,
		[EstadosEpisodio.CON_ALTA_MEDICA]: DISCHARGE,
		[EstadosEpisodio.AUSENTE]: ABSENT,
		[EstadosEpisodio.LLAMADO]: CALLED
	}

	@Input() set statusLabel(statusLabel: EmergencyCareStatusLabels) {
		if (statusLabel)
			this.buildStatusLabel(statusLabel);
	}

	constructor() { }

	private buildStatusLabel(statusLabel: EmergencyCareStatusLabels) {
		let state = this.states[statusLabel.stateId];
		if (state) {
			state = { ...state, description: statusLabel.description }
			this.coloredLabel = state;
		}

	}

}

export interface EmergencyCareStatusLabels {
	stateId: EstadosEpisodio,
	description: string,
}

const WAITING = {
	color: Color.GREY,
	icon: "timer",
}

const IN_ATTETION = {
	color: Color.GREEN,
	icon: "check_circle_outline"
}

const DISCHARGE = {
	color: Color.BLUE,
	icon: "done_all"
}

const ABSENT = {
	color: Color.RED,
	icon: "person_off"
}

const CALLED = {
	color: Color.YELLOW,
	icon: "phone"
}

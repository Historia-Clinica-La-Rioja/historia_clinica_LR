import { Meta, StoryObj } from "@storybook/angular";
import { EmergencyCareStatusLabelsComponent } from "./emergency-care-status-labels.component";
import { EstadosEpisodio } from "@historia-clinica/modules/guardia/constants/masterdata";

const meta: Meta<EmergencyCareStatusLabelsComponent> = {
	title: 'HSI/EmergencyCareStatusLabelsComponent',
	component: EmergencyCareStatusLabelsComponent,
	tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<EmergencyCareStatusLabelsComponent>;

export const En_espera: Story = {
	args: {
		statusLabel: {
			stateId: EstadosEpisodio.EN_ESPERA,
			description: "En espera de atención"
		} 
	},
};

export const En_atencion: Story = {
	args: {
		statusLabel: {
			stateId: EstadosEpisodio.EN_ATENCION,
			description: "En atención"
		} 
	},
};

export const Llamado: Story = {
	args: {
		statusLabel: {
			stateId: EstadosEpisodio.LLAMADO,
			description: "Llamando (2)"
		}
	},
};

export const Ausente: Story = {
	args: {
		statusLabel: {
			stateId: EstadosEpisodio.AUSENTE,
			description: "Ausente"
		}
	},
};

export const Alta_de_paciente: Story = {
	args: {
		statusLabel: {
			stateId: EstadosEpisodio.CON_ALTA_MEDICA,
			description: "Alta de paciente"
		}		
	},
};
import type { Meta, StoryObj } from '@storybook/angular';
import { NewConsultationExpansionSectionComponent } from './new-consultation-expansion-section.component';

const meta: Meta<NewConsultationExpansionSectionComponent> = {
	title: 'HC/NewConsultationExpansionSectionComponent',
	component: NewConsultationExpansionSectionComponent,
	tags: ['autodocs'],
	argTypes: {
		icon: {
			description: 'Icono a mostrar en el header',
		},
		title: {
			description: 'Titulo a mostrar en el header',
		},
        hideBorder: {
			description: 'Oculta el mat-divider',
		},
        recommend: {
            description: 'Recomienda agregar el concepto'
        },
        collapsed: {
            description: 'Colapsa la seccion'
        },
        isEmpty: {
            description: 'Indica si el contenido es vacio. Si no lo esta, marca con negrita el header',
        }
	},
	parameters: {
        docs: {
            description: {
                component: "Muestra secciones colapsables con icono y titulo."
            }
        }
    }
};

export default meta;
type Story = StoryObj<NewConsultationExpansionSectionComponent>;

export const Minimun: Story = {
	args: {
		icon: 'add',
        title: 'Titulo',
	}
}

export const RecommendSection: Story = {
	args: {
		icon: 'add',
        title: 'Titulo',
        recommend: true
	}
}

export const HideBorderSection: Story = {
	args: {
		icon: 'add',
        title: 'Titulo',
        hideBorder: true
	}
}

export const CollapsedSectionWithConcepts: Story = {
	args: {
		icon: 'add',
        title: 'Titulo',
        isEmpty: false,
        collapsed: true,
	}
}

export const WithoutCollapsedSectionWithConcepts: Story = {
	args: {
		icon: 'add',
        title: 'Titulo',
        isEmpty: false,
        collapsed: false,
	}
}

export const WithAllParameters: Story = {
	args: {
		icon: 'add',
        title: 'Titulo',
        hideBorder: true,
        recommend: true, 
        collapsed: true,
        isEmpty: false
	}
}




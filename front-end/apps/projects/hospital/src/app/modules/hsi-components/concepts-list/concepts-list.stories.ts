import type { Meta, StoryObj } from '@storybook/angular';
import { ConceptsListComponent } from './concepts-list.component';

const meta: Meta<ConceptsListComponent> = {
	title: 'HSI/ConceptsListComponent',
	component: ConceptsListComponent,
	tags: ['autodocs'],
	argTypes: {
		content: {
			description: 'Objeto con todo el texto para mostrar',
		},
		hasConcepts: {
			description: 'Indica si tiene conceptos',
			options: [true, false],
		},
	}
};

export default meta;
type Story = StoryObj<ConceptsListComponent>;

export const Minimun: Story = {
	args: {
		content: {
			id: 'allergy-checkbox-concepts-list',
            header: {
                text: 'Alergias',
                icon: 'cancel'
            },
            titleList: '',
            actions: {
                button: 'Agregar alergia',
            }
        },
	}
}

export const WithNoReferCheckbox: Story = {
	args: {
		content: {
			id: 'allergy-checkbox-concepts-list',
            header: {
                text: 'Alergias',
                icon: 'cancel'
            },
            titleList: '',
            actions: {
                button: 'Agregar alergia',
                checkbox: 'No refiere'
            }
        },
	},
}

export const WithConceptListAndNoReferCheckbox: Story = {
	args: {
		content: {
			id: 'allergy-checkbox-concepts-list',
            header: {
                text: 'Alergias',
                icon: 'cancel'
            },
            titleList: 'Alergias registradas',
            actions: {
                button: 'Agregar alergia',
                checkbox: 'No refiere'
            }
        },
        hasConcepts: true
	},
}

export const WithoutConceptListAndNoReferCheckboxClicked: Story = {
	args: {
		content: {
			id: 'allergy-checkbox-concepts-list',
            header: {
                text: 'Alergias',
                icon: 'cancel'
            },
            titleList: 'Alergias registradas',
            actions: {
                button: 'Agregar alergia',
                checkbox: 'No refiere'
            }
        },
        hasConcepts: false,
		initialCheckboxState: true
	},
}


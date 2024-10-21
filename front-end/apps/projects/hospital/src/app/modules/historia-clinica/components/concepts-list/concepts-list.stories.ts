import { moduleMetadata, type Meta, type StoryObj } from '@storybook/angular';
import { ConceptsListComponent } from './concepts-list.component';
import { NewConsultationExpansionSectionComponent } from '../new-consultation-expansion-section/new-consultation-expansion-section.component';

const meta: Meta<ConceptsListComponent> = {
    title: 'HC/ConceptsListComponent',
    component: ConceptsListComponent,
    tags: ['autodocs'],
    decorators: [
        moduleMetadata({
          declarations: [NewConsultationExpansionSectionComponent],
        }),
      ],
    argTypes: {
        content: {
            description: 'Objeto con todo el texto para mostrar',
        },
        hasConcepts: {
            description: 'Indica si tiene conceptos',
            options: [true, false],
        },
        isEmpty: {
            description: 'Indica si el contenido es vacio. Si no lo esta, marca con negrita el header',
            options: [true, false],
        },
    },
    parameters: {
        docs: {
            description: {
                component: "Componente que muestra secciones colapsables con header y acciones. Para visualizar las secciones colapsables usa el componente NewConsultationExpansionSection"
            }
        }
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
            },
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
            },
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
        hasConcepts: true,
        isEmpty: false
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
        initialCheckboxState: true,
        isEmpty: false,
    },
}


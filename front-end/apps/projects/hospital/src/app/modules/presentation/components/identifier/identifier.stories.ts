import { type Meta, type StoryObj } from '@storybook/angular';
import { IdentifierComponent, Position } from './identifier.component';

const meta: Meta<IdentifierComponent> = {
    title: 'Presentation/IdentifierComponent',
    component: IdentifierComponent,
    tags: ['autodocs'],
    argTypes: {
		position: {
			options: Object.values(Position),
			control: {
				type: 'select',
			},
			description: 'Usar enum Position',
		},
		showLegend: {
			description: 'Indica si va leyenda o tooltip',
            options: [true, false],
            control: {
				type: 'select',
			},
		}
	}
};

export default meta;

type Story = StoryObj<IdentifierComponent>;

export const min: Story = {
    args: {
        identifier: {
            iconLegend: {
                icon: "library_add",
                legend: "Ejemplo"
            },
            description: "Descripcion de ejemplo"
        },
        
    }
};

export const full: Story = {
    args: {
        identifier: {
            iconLegend: {
                icon: "library_add",
                legend: "Ejemplo"
            },
            description: "Descripcion de ejemplo"
        },
        showLegend: true,
        position: Position.COLUMN
    }
};

export const fullPositionRow: Story = {
    args: {
        identifier: {
            iconLegend: {
                icon: "library_add",
                legend: "Ejemplo"
            },
            description: "Descripcion de ejemplo",
        },
        showLegend: true,
    }
}

export const fullPositionColumn: Story = {
    args: {
        identifier: {
            iconLegend: {
                icon: "library_add",
                legend: "Ejemplo"
            },
            description: "Descripcion de ejemplo",
        },
        showLegend: true,
        position: Position.COLUMN
    }
}


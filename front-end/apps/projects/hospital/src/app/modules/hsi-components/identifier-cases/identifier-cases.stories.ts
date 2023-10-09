import type { Meta, StoryObj } from '@storybook/angular';
import { IDENTIFIER_CASES, IdentifierCasesComponent } from './identifier-cases.component';
import { Position } from '@presentation/components/identifier/identifier.component';

const meta: Meta<IdentifierCasesComponent> = {
	title: 'HSI/IdentifierCasesComponent',
	component: IdentifierCasesComponent,
	tags: ['autodocs'],
	argTypes: {
		identifierCase: {
			options: Object.values(IDENTIFIER_CASES),
			control: {
				type: 'select',
			},
			description: 'Usar enum Identifier Cases',
		},
		showLegend: {
			description: 'Indica si va leyenda o tooltip',
			options: [true, false],
            control: {
				type: 'select',
			},
		},
		position: {
			options: Object.values(Position),
			control: {
				type: 'select',
			},
			description: 'Usar enum Position',
		}
	}
};

export default meta;
type Story = StoryObj<IdentifierCasesComponent>;

export const institutionMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.INSTITUTION,
		description: "Ejemplo de institucion",
	}
}

export const institutionFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.INSTITUTION,
		showLegend: true,
		description: "Ejemplo de institucion",
		position: Position.COLUMN
	}
};

export const careLineMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.CARE_LINE,
		description: "Ejemplo de Linea de cuidado",
	}
};

export const careLineFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.CARE_LINE,
		showLegend: true,
		description: "Ejemplo de Linea de cuidado",
		position: Position.COLUMN
	}
};

export const problemMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.PROBLEM,
		description: "Ejemplo de institucion",
	}
};

export const problemFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.PROBLEM,
		showLegend: true,
		description: "Ejemplo de problema",
		position: Position.COLUMN
	}
};

export const professionalMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.PROFESSIONAL,
		description: "Ejemplo de profesional",
	}
};

export const professionalFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.PROFESSIONAL,
		showLegend: true,
		description: "Ejemplo de profesional",
		position: Position.COLUMN
	}
};

export const practiceMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.PRACTICE,
		description: "Ejemplo de practica",
	}
};

export const practiceFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.PRACTICE,
		showLegend: true,
		description: "Ejemplo de practica",
		position: Position.COLUMN
	}
};

export const specialtyMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.SPECIALTY,
		description: "Ejemplo de especialidad",
	}
};

export const specialtyFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.SPECIALTY,
		showLegend: true,
		description: "Ejemplo de especialidad",
		position: Position.COLUMN
	}
};

export const dateMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.DATE,
		description: "Ejemplo de fecha 06/10/2023",
	}
};

export const dateFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.DATE,
		showLegend: true,
		description: "Ejemplo de fecha 06/10/2023",
		position: Position.COLUMN
	}
};

export const hourMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.HOUR,
		description: "Ejemplo de hora 10:20",
	}
};

export const hourFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.INSTITUTION,
		showLegend: true,
		description: "Ejemplo de hora 10:20",
		position: Position.COLUMN
	}
};

export const districtMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.DISTRICT,
		description: "Ejemplo de partido",
	}
};

export const districtFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.DISTRICT,
		showLegend: true,
		description: "Ejemplo de partido",
		position: Position.COLUMN
	}
};
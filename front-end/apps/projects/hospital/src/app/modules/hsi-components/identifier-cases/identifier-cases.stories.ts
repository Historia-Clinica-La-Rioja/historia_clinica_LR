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
		identifierCase: IDENTIFIER_CASES.HOUR,
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

export const serviceMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.SERVICE,
		description: "Ejemplo de servicio",
	}
};

export const serviceFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.SERVICE,
		showLegend: true,
		description: "Ejemplo de servicio",
		position: Position.COLUMN
	}
};

export const roomMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.ROOM,
		description: "Ejemplo de sala de reuniones",
	}
};

export const roomFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.ROOM,
		showLegend: true,
		description: "Ejemplo de sala de reuniones",
		position: Position.COLUMN
	}
};

export const doctorOfficeMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.DOCTOR_OFFICE,
		description: "Ejemplo de consultorio",
	}
};

export const doctorOfficeFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.DOCTOR_OFFICE,
		showLegend: true,
		description: "Ejemplo de consultorio",
		position: Position.COLUMN
	}
};

export const sectorMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.SECTOR,
		description: "Ejemplo de linea de cuidado",
	}
};

export const sectorFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.SECTOR,
		showLegend: true,
		description: "Ejemplo de linea de cuidado",
		position: Position.COLUMN
	}
};

export const hierarchicalUnitMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.HIERARCHICAL_UNIT,
		description: "EjemplIDENTIFIER_CAo de información de profesionales asociados",
	}
};

export const hierarchicalUnitFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.HIERARCHICAL_UNIT,
		showLegend: true,
		description: "Ejemplo de información de profesionales asociados",
		position: Position.COLUMN
	}
};

export const bedMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.BED,
		description: "Ejemplo cama",
	}
};

export const bedFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.BED,
		showLegend: true,
		description: "Ejemplo cama",
		position: Position.COLUMN
	}
};

export const scopeMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.SCOPE,
		description: "Ejemplo ambito",
	}
};

export const scopeFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.SCOPE,
		showLegend: true,
		description: "Ejemplo ambito",
		position: Position.COLUMN
	}
};

export const patientMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.PATIENT,
		description: "Ejemplo paciente",
	}
};

export const patientFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.PATIENT,
		showLegend: true,
		description: "Ejemplo paciente",
		position: Position.COLUMN
	}
};

export const reasonMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.REASON,
		description: "Ejemplo motivo",
	}
};

export const reasonFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.REASON,
		showLegend: true,
		description: "Ejemplo motivo",
		position: Position.COLUMN
	}
};

export const emergencyCareTypeMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.EMERGENCY_CARE_TYPE,
		description: "Ejemplo tipo de guardia",
	}
};

export const emergencyCareTypeFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.EMERGENCY_CARE_TYPE,
		showLegend: true,
		description: "Ejemplo tipo de guardia",
		position: Position.COLUMN
	}
};

export const shockroomMin: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.SHOCKROOM,
		description: "Ejemplo de shockroom",
	}
};

export const shockroomFull: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.SHOCKROOM,
		showLegend: true,
		description: "Ejemplo de shockroom",
		position: Position.COLUMN
	}
};

export const roomV2Min: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.ROOM_V2,
		description: "Ejemplo de habitación",
	}
};

export const roomV2Full: Story = {
	args: {
		identifierCase: IDENTIFIER_CASES.ROOM_V2,
		showLegend: true,
		description: "Ejemplo de habitación",
		position: Position.COLUMN
	}
};

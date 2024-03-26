import type { Meta, StoryObj } from '@storybook/angular';
import { Position, RadioGroupComponent } from './radio-group.component';

const meta: Meta<RadioGroupComponent> = {
	title: 'Presentation/RadioGroupComponent',
	component: RadioGroupComponent,
	tags: ['autodocs'],
	argTypes: { 
		radioGroupInputData: {
			description: '<strong>Title:</strong> string para definir el titulo del radiogroup <br> <strong>data:</strong> array de opciones. Objeto con atributos: value y description <br><strong>color:</strong> themePallete para definir el color del radiobutton <br> <strong>position:</strong> Flex layout para el title y el contenedor de los radiobuttons <br> <strong>optionsPosition:</strong> Flex layout para los radiobuttons',
			table: { 
				defaultValue: { summary: 'color: primary, data:[{value: 1, description: "Si"}, {value: 0, description: "No"}], position: row, optionsPosition: row' } 
			}      
		}
	}
};

export default meta;
type Story = StoryObj<RadioGroupComponent>;

export const rowRowRadiobutton: Story = {
	args: {
		radioGroupInputData: {
			presentation: {
				color: 'primary',
				title: 'Row row radiobutton',
				data: [
					{value: 1, description: "Si"},
					{value: 0, description: "No"}
				]
			},
			alignments: {
				position: Position.ROW,
				optionsPosition: Position.ROW,
			}        
		}
	},
};

export const rowColumnRadiobutton: Story = {
	args: {
		radioGroupInputData: {
			presentation: {
				color: 'primary',
				title: 'Row column radiobutton',
				data: [
					{value: 1, description: "Si"},
					{value: 0, description: "No"}
				]
			},
			alignments: {
				position: Position.ROW,
				optionsPosition: Position.COLUMN,
			}        
		}
	},
};

export const columnColumnRadiobutton: Story = {
	args: {
		radioGroupInputData: {
			presentation: {
				color: 'primary',
				title: 'Column column radiobutton',
				data: [
					{value: 1, description: "Si"},
					{value: 0, description: "No"}
				]
			},
			alignments: {
				position: Position.COLUMN,
				optionsPosition: Position.COLUMN,
			}        
		}
	},
};

export const columnRowRadiobutton: Story = {
	args: {
		radioGroupInputData: {
			presentation: {
				color: 'primary',
				title: 'Column row radiobutton',
				data: [
					{value: 1, description: "Si"},
					{value: 0, description: "No"}
				]
			},
			alignments: {
				position: Position.COLUMN,
				optionsPosition: Position.ROW,
			}        
		}
	},
};

export const defaultWarnRadiobutton: Story = {
	args: {
		radioGroupInputData: {
			presentation: {
				color: 'warn',
				title: 'Default warn radiobutton',
				data: [
					{value: 1, description: "Si"},
					{value: 0, description: "No"}
				]
			},
			alignments: {
				position: Position.ROW,
				optionsPosition: Position.ROW,
			}        
		}
	},
};

export const defaultAccentRadiobutton: Story = {
	args: {
		radioGroupInputData: {
			presentation: {
				color: 'accent',
				title: 'Default accent radiobutton',
				data: [
					{value: 1, description: "Si"},
					{value: 0, description: "No"}
				]
			},
			alignments: {
				position: Position.ROW,
				optionsPosition: Position.ROW,
			}        
		}
	},
};


export const customOptionsRadioButton: Story = {
	args: {
		radioGroupInputData: {
			presentation: {
				color: 'primary',
				title: 'Custom options radiobutton',
				data: [
					{value: 1, description: "Opcion 1"},
					{value: 2, description: "Opcion 2"},
					{value: 3, description: "Opcion 3"}
				]
			},
			alignments: {
				position: Position.ROW,
				optionsPosition: Position.ROW,
			}        
		}
	},
};
import type { Meta, StoryObj } from '@storybook/angular';
import { Position, RadioGroupComponent } from './radio-group.component';

const meta: Meta<RadioGroupComponent> = {
	title: 'Presentation/RadioGroupComponent',
	component: RadioGroupComponent,
	tags: ['autodocs'],
	argTypes: {
		color: {
			options: ['primary', 'accent', 'warn'],
			control: {
				type: 'select',
			},
            description: 'Usar ThemePallet',
            table: {
                defaultValue: { summary: 'primary' },
            }
		},
        position: {
            options: ['column', 'row'],
			control: {
                type: 'select',
			},
			description: 'Flex layout para el title y el contenedor de los radiobuttons',
            table: { 
                defaultValue: { summary: 'row' } 
            }
        },
        optionsPosition: {
            options: ['column', 'row'],
			control: {
				type: 'select',
			},
			description: 'Flex layout para los radiobuttons',
            table: { 
                defaultValue: { summary: 'row' } 
            }
        },
        data: {
            description: 'Objeto de tipo RadioGroupData { value: number, description: string }',
            table: { 
                defaultValue: { summary: '[{value: 1, description: "Si"}, {value: 2, description: "No"}]' } 
            }
        }
	}
};

export default meta;
type Story = StoryObj<RadioGroupComponent>;

export const rowRowRadiobutton: Story = {
	args: {
		color: 'primary',
		position: Position.ROW,
		optionsPosition: Position.ROW,
		title: 'Row row radiobutton',
        data: [
            {value: 1, description: "Si"},
            {value: 2, description: "No"}
        ]
	},
};

export const rowColumnRadiobutton: Story = {
	args: {
		color: 'primary',
		position: Position.ROW,
		optionsPosition: Position.COLUMN,
		title: 'Row column radiobutton',
        data: [
            {value: 1, description: "Si"},
            {value: 2, description: "No"}
        ]
	},
};

export const columnColumnRadiobutton: Story = {
	args: {
		color: 'primary',
		position: Position.COLUMN,
		optionsPosition: Position.COLUMN,
		title: 'Column column radiobutton',
        data: [
            {value: 1, description: "Si"},
            {value: 2, description: "No"}
        ]
	},
};

export const columnRowRadiobutton: Story = {
	args: {
		color: 'primary',
		position: Position.COLUMN,
		optionsPosition: Position.ROW,
		title: 'Column row radiobutton',
        data: [
            {value: 1, description: "Si"},
            {value: 2, description: "No"}
        ]
	},
};
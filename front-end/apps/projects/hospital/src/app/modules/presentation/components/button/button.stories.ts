import type { Meta, StoryObj } from '@storybook/angular';
import { ButtonComponent, ButtonType, } from './button.component';



const meta: Meta<ButtonComponent> = {
	title: 'Presentation/ButtonComponent',
	component: ButtonComponent,
	tags: ['autodocs'],
	argTypes: {
		color: {
			options: ['primary', 'accent', 'warn', undefined],
			control: {
				type: 'select',
			},
			description: 'Usar ThemePallet',
			defaultValue: 'primary'
		},
		buttonType: {
			options: Object.values(ButtonType),
			control: {
				type: 'select',
			},
			description: 'Usar enum ButtonType',
		},
	}
};

export default meta;
type Story = StoryObj<ButtonComponent>;

export const readyToUse: Story = {
	args: {
		color: 'primary',
		text: 'NUEVA CONSULTA',
		buttonType: ButtonType.RAISED,
		isLoading: false
	},
};

export const Loading: Story = {
	args: {
		color: 'primary',
		text: 'NUEVA CONSULTA',
		buttonType: ButtonType.RAISED,
		isLoading: true
	},
};


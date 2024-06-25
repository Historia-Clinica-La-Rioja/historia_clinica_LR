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
		matIcon: {
			options: ['person_search', 'delete'],
			control: {
				type: 'select',
			},
			description: "Usar mat icons",
		}
	}
};

export default meta;
type Story = StoryObj<ButtonComponent>;

export const readyToUse: Story = {
	args: {
		color: 'primary',
		text: 'NUEVA CONSULTA',
		buttonType: ButtonType.RAISED,
		isLoading: false,
		disabled: false,
	},
};

export const ButtonTypeIcon: Story = {
	args: {
		color: 'warn',
		matIcon: 'delete',
		buttonType: ButtonType.ICON,
		isLoading: false,
		disabled: false,
	},
};


export const ButtonTypeFlat: Story = {
	args: {
		color: 'primary',
		text: 'nueva consulta',
		buttonType: ButtonType.FLAT,
		isLoading: false,
		disabled: false,
	},
};

export const ButtonTypeStroked: Story = {
	args: {
		color: 'primary',
		text: 'nueva consulta',
		buttonType: ButtonType.STROKED,
		isLoading: false,
		disabled: false,
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

export const disabledBeforeUse: Story = {
	args: {
		color: 'primary',
		text: 'NUEVA CONSULTA',
		buttonType: ButtonType.RAISED,
		isLoading: false,
		disabled: true,
	},
};

export const WithMatIcon: Story = {
	args: {
		color: 'primary',
		text: 'buscar',
		buttonType: ButtonType.RAISED,
		isLoading: false,
		disabled: false,
		matIcon: 'person_search'
	},
};
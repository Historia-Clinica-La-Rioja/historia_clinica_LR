import type { Meta, StoryObj } from '@storybook/angular';
import { ButtonAvailabilityComponent, AvailableButtonWidth, AvailableButtonType } from './button-availability.component';

const meta: Meta<ButtonAvailabilityComponent> = {
	title: 'Presentation/ButtonAvailabilityComponent',
	component: ButtonAvailabilityComponent,
	tags: ['autodocs'],
	argTypes: {
		description: {
			control: {
				type: 'text',
			},
			description: 'Descripción del botón',
			defaultValue: '',
		},
		type: {
			options: Object.values(AvailableButtonType),
			control: {
				type: 'select',
			},
			description: 'Usar enum AvailableButtonType',
			defaultValue: AvailableButtonType.AVAILABLE,
		},
		size: {
			options: Object.values(AvailableButtonWidth),
			control: {
				type: 'select',
			},
			description: 'Usar enum AvailableButtonWidth',
			defaultValue: AvailableButtonWidth.LARGE,
		},
		isSelected: {
			control: {
				type: 'boolean',
			},
			description: 'Indica si el botón está seleccionado',
			defaultValue: false,
		},
	}
};

export default meta;
type Story = StoryObj<ButtonAvailabilityComponent>;

export const AvailableButton: Story = {
	args: {
		description: 'Botón',
		type: AvailableButtonType.AVAILABLE,
		size: AvailableButtonWidth.LARGE,
		isSelected: false,
	},
};

export const AvailableButtonSmall: Story = {
	args: {
		...AvailableButton.args,
		description: '135',
		size: AvailableButtonWidth.SMALL,
	},
};

export const AvailableButtonSelected: Story = {
	args: {
		...AvailableButton.args,
		isSelected: true
	},
};

export const NotAvailableButton: Story = {
	args: {
		...AvailableButton.args,
		type: AvailableButtonType.NOT_AVAILABLE
	},
};

export const NotAvailableButtonSmall: Story = {
	args: {
		...NotAvailableButton.args,
		description: '135',
		size: AvailableButtonWidth.SMALL,
	},
};

export const NotAvailableButtonSelected: Story = {
	args: {
		...NotAvailableButton.args,
		isSelected: true
	},
};

export const DisabledButton: Story = {
	args: {
		...AvailableButton.args,
		type: AvailableButtonType.DISABLE,
	},
};

export const DisabledButtonSmall: Story = {
	args: {
		...DisabledButton.args,
		description: '135',
		size: AvailableButtonWidth.SMALL,
	},
};

export const DisabledButtonSelected: Story = {
	args: {
		...DisabledButton.args,
		isSelected: true
	},
};

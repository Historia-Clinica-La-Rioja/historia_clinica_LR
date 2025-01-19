import type { Meta, StoryObj } from '@storybook/angular';

import { Color, ColoredLabelComponent } from './colored-label.component';

const meta: Meta<ColoredLabelComponent> = {
	title: 'Presentation/ColoredLabelComponent',
	component: ColoredLabelComponent,
	tags: ['autodocs'],
	argTypes: {
		color: {
			options: Object.values(Color),
			control: {
				type: 'select',
			},
			description: 'Usar el enum Color'
		}
	}
};

export default meta;
type Story = StoryObj<ColoredLabelComponent>;

export const RED: Story = {
	args: {
		description: 'Label',
		color: Color.RED
	},
};
export const BLUE: Story = {
	args: {
		description: 'Label',
		color: Color.BLUE
	},
};
export const GREEN: Story = {
	args: {
		description: 'Label',
		color: Color.GREEN
	},
};
export const GREY: Story = {
	args: {
		description: 'Label',
		color: Color.GREY
	},
};

export const YELLOW: Story = {
	args: {
		description: 'Label',
		color: Color.YELLOW
	},
};

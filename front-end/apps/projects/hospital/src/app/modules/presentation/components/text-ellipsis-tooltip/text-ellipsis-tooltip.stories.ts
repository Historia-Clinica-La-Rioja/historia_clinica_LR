import type { Meta, StoryObj } from '@storybook/angular';
import { TextEllipsisTooltipComponent } from './text-ellipsis-tooltip.component';

const meta: Meta<TextEllipsisTooltipComponent> = {
	title: 'Presentation/TextEllipsisTooltipComponent',
	component: TextEllipsisTooltipComponent,
	tags: ['autodocs']
};

export default meta;
type Story = StoryObj<TextEllipsisTooltipComponent>;

export const limitGretearThanTextLength: Story = {
	args: {
		text: 'Texto corto',
		limit: 12
	},
};

export const limitMinorThanTextLength: Story = {
	args: {
		text: 'Texto largo para probar como se corta y la visualizacion del tooltip',
		limit: 12
	},
};


import type { Meta, StoryObj } from '@storybook/angular';
import { TitleDescriptionComponent } from './title-description.component';


const meta: Meta<TitleDescriptionComponent> = {
	title: 'Presentation/TitleDescriptionComponent',
	component: TitleDescriptionComponent,
	tags: ['autodocs']
};

export default meta;

type Story = StoryObj<TitleDescriptionComponent>;

export const fullWithoutEllipsisTooltip: Story = {
	args: {
		title: "Observaciones",
		text: "Detalle de las observaciones"
	}
};

export const full: Story = {
	args: {
		title: "Observaciones",
		text: "Detalle de las observaciones",
		limit: 10
	}
};


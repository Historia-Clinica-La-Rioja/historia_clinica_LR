import type { Meta, StoryObj } from '@storybook/angular';
import { TitledContentCardComponent } from './titled-content-card.component';


const meta: Meta<TitledContentCardComponent> = {
	title: 'Presentation/TitledContentCardComponent',
	component: TitledContentCardComponent,
	tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<TitledContentCardComponent>;

export const Full: Story = {
	args:{
		title: "Titulo de ejemplo"
	}
};

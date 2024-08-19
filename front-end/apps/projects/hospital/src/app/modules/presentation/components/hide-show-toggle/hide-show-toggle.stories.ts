import type { Meta, StoryObj } from '@storybook/angular';
import { HideShowToggleComponent } from './hide-show-toggle.component';

const meta: Meta<HideShowToggleComponent> = {
	title: 'Presentation/HideShowToggleComponent',
	component: HideShowToggleComponent,
	tags: ['autodocs'],
	argTypes: {
		header: {
			icon: 'home_health',
			text: 'Historia de salud integrada',
			description: 'Debe contener un icono y un título',
		},
	}, 
};

export default meta;
type Story = StoryObj<HideShowToggleComponent>;

export const complete: Story = {
	args: {
		header: {
			icon: 'home_health',
			text: 'Historia de salud integrada',
			
		},
	}
};
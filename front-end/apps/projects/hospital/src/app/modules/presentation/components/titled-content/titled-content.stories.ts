import type { Meta, StoryObj } from '@storybook/angular';
import { TitledContentComponent } from './titled-content.component';


const meta: Meta<TitledContentComponent> = {
	title: 'Presentation/TitledContentComponent',
	component: TitledContentComponent,
	tags: ['autodocs'],
	args: {
		title: 'Titulo descriptivo',
		icon: 'chat'
	},
	argTypes: {
		icon: {
			options: ['sms_failed', 'report', 'chat', 'accessibility', 'report', 'event_available', 'library_add', 'favorite_outlined', 'cancel'],
			control: {
				type: 'select',
			},
			description: 'use Material Icons',
			defaultValue: 'chat'
		}
	}
};

export default meta;
type Story = StoryObj<TitledContentComponent>;

export const Full: Story = {
	args:{
		content: ['some content', 'some content 2', 'some content 3', 'some content 4', 'some content 5'],
	}
};

export const NoContent: Story = {
	args: {
		content: []
	}
}



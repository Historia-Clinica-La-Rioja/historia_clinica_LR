import type { Meta, StoryObj } from '@storybook/angular';
import { DownloadButtonComponent } from './download-button.component';



const meta: Meta<DownloadButtonComponent> = {
	title: 'Presentation/DownloadButtonComponent',
	component: DownloadButtonComponent,
	tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<DownloadButtonComponent>;

export const readyToUse: Story = {
	args: {
		description: "Adjuntar documentos",
	}
};

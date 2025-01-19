import type { Meta, StoryObj } from '@storybook/angular';
import { ContentComponent } from './content.component';


// More on how to set up stories at: https://storybook.js.org/docs/angular/writing-stories/introduction
const meta: Meta<ContentComponent> = {
	title: 'Presentation/ContentComponent',
	component: ContentComponent,
	tags: ['autodocs'],
	render: (args: ContentComponent) => ({
		props: {
			...args,
		},
		template: `
		<app-content width=${args.width}>
			<div>
				<span>Buenas</span>
			</div>
		</app-content>`
	}),
};

export default meta;

type Story = StoryObj<ContentComponent>;

export const Full: Story = {
	args: {
		width: 'full'
	},
	render: (args: ContentComponent) => ({
		props: {
			...args,
		},
		template: `
		<app-content width=${args.width}>
			<span>Este es un span en full</span>
		</app-content>`
	})

};

export const Narrow: Story = {
	args: {
		width: 'narrow'
	},
	render: (args: ContentComponent) => ({
		props: {
			...args,
		},
		template: `
		<app-content width=${args.width}>
			<span>Este es un span en narrow</span>
		</app-content>`
	})

};

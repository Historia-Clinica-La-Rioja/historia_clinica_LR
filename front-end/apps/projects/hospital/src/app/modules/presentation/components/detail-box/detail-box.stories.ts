import { moduleMetadata, type Meta, type StoryObj } from '@storybook/angular';

import { DetailBoxComponent } from './detail-box.component';
import { DatePipe } from '@angular/common';

const meta: Meta<DetailBoxComponent> = {
	title: 'Presentation/DetailBoxComponent',
	component: DetailBoxComponent,
	tags: ['autodocs'],
	decorators: [
		moduleMetadata({
			providers: [DatePipe]
		}),
	],

};

export default meta;

type Story = StoryObj<DetailBoxComponent>;


export const Full: Story = {
	args: {
		detail: {
			description: 'Input description ',
			registeredValues: [
				{
					date: '2022-12-13',
					value: 2
				},
				{
					date: '2022-12-12',
					value: 1
				}
			]
		}
	}
};

export const NullTitle: Story = {
	args: {
		detail: {
			description: null,
			registeredValues: [
				{
					date: '2022-12-13',
					value: 2
				},
				{
					date: '2022-12-12',
					value: 1
				}
			]
		}
	}
};

export const OneValue: Story = {
	args: {
		detail: {
			description: 'Input description ',
			registeredValues: [
				{
					date: '2022-12-13',
					value: 2
				},

			]
		}
	}
};


export const TwoValues: Story = {
	args: {
		detail: {
			description: 'Input description ',
			registeredValues: [
				{
					date: '2022-12-13',
					value: 2
				},
				{
					date: '2022-12-12',
					value: 1
				},

			]
		}
	}
};


export const ThreeValues: Story = {
	args: {
		detail: {
			description: 'Input description ',
			registeredValues: [
				{
					date: '2022-12-13',
					value: 3
				},
				{
					date: '2022-12-12',
					value: 2
				},
				{
					date: '2022-12-11',
					value: 1
				},

			]
		}
	}
};

export const RandomDateFormat: Story = {
	args: {
		detail: {
			description: 'Input description ',
			registeredValues: [
				{
					date: '2022-12-13',
					value: 3
				},
				{
					date: '13/12/2022',
					value: 2
				},

			]
		}
	}
};

export const NoDateInString: Story = {
	args: {
		detail: {
			description: 'Input description ',
			registeredValues: [
				{
					date: 'this is not a date',
					value: 2
				},

			]
		}
	}
};




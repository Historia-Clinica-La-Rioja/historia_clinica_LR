import { type Meta, type StoryObj } from '@storybook/angular';

import { DetailBoxComponent } from './detail-box.component';

const meta: Meta<DetailBoxComponent> = {
	title: 'Example/DetailBoxComponent',
	component: DetailBoxComponent,
	tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<DetailBoxComponent>;


export const Full: Story = {
	args: {
		detail: {
			description: 'Input description ',
			registeredValues: [
				{
					date: '13/12/2022',
					value: 2
				},
				{
					date: '12/12/2022',
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
					date: '13/12/2022',
					value: 2
				},
				{
					date: '12/12/2022',
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
					date: '13/12/2022',
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
					date: '13/12/2022',
					value: 2
				},
				{
					date: '12/12/2022',
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
					date: '13/12/2022',
					value: 3
				},
				{
					date: '12/12/2022',
					value: 2
				},
				{
					date: '11/12/2022',
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
					date: '13/12/2022',
					value: 3
				},
				{
					date: '2022-02-02',
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
					date: '13/12/2022',
					value: 3
				},
				{
					date: 'this is not a date',
					value: 2
				},

			]
		}
	}
};




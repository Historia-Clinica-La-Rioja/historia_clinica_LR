import { moduleMetadata, type Meta, type StoryObj } from '@storybook/angular';
import { DatepickerComponent } from './datepicker.component';
import { ReactiveFormsModule } from '@angular/forms';

const meta: Meta<DatepickerComponent> = {
	title: 'Presentation/DatepickerComponent',
	component: DatepickerComponent,
	tags: ['autodocs'],
	decorators: [
		moduleMetadata({
			imports: [ReactiveFormsModule],
		}),
	],
};

export default meta;

type Story = StoryObj<DatepickerComponent>;


export const noInputsSet: Story = {
/* 	args: {  // No funcionan muy bien, algunos si otros no
		enableDelete: true,
		title: 'Titulo',
		dateToSetInDatepicker: new Date(),
		maxDate: new Date(),
		minDate: new Date('2023-01-01T00:00:00'),
		availableDays: [0,1,2,5,6],
		disableDays: [new Date('2023-07-01T00:00:00')]
	}, */
};


export const enableDelete: Story = {
	args: {
		enableDelete: true
	},

};

export const title: Story = {
	args: {
		title: 'Title del datepicker component'
	},};

export const dateToSetInDatepicker: Story = {
	args: {
		dateToSetInDatepicker: new Date()
	},
};

export const MaxDate: Story = {
	args: {
		maxDate: new Date()
	},
};

export const MinDate: Story = {
	args: {
		minDate: new Date()
	},
};

export const availableDays: Story = {
	args: {
		availableDays: [0, 2, 5]
	},
};

export const disableDays: Story = { // Funciona mal !?
	args: {
		disableDays: [new Date('2023-07-01T00:00:00')]
	},
};



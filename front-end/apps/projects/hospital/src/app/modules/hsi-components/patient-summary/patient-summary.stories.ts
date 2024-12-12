import type { Meta, StoryObj } from '@storybook/angular';
import { PatientSummaryComponent } from './patient-summary.component';
import { Size } from '@presentation/components/item-summary/item-summary.component';

const meta: Meta<PatientSummaryComponent> = {
	title: 'HSI/PatientSummaryComponent',
	component: PatientSummaryComponent,
	tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<PatientSummaryComponent>;

export const complete: Story = {
	args: {
		person: {
			fullName: 'Lionel Andrés Messi Cuccittini',
			age: 36,
			gender: 'Masculino',
			id: 10,
			identification: {
				number: '123456',
				type: 'DNI'
			},

		}
	}
};

export const name_age: Story = {
	args: {
		person: {
			fullName: 'Lionel Andrés Messi Cuccittini',
			age: 36
		},

	}
};

export const small_name: Story = {
	args: {
		person: {
			fullName: 'Lionel Andrés Messi Cuccittini',
		},
		size: Size.SMALL
	}
};


export const small_name_gender: Story = {
	args: {
		person: {
			fullName: 'Lionel Andrés Messi Cuccittini',
			gender: 'Masculino'
		},
		size: Size.SMALL
	}
};


export const months_of_life: Story = {
	args: {
		person: {
			fullName: 'Lionel Andrés Messi Cuccittini',
			monthsOfLife: "10 meses y 3 dias",
			gender: 'Masculino',
			id: 10,
			identification: {
				number: '123456',
				type: 'DNI'
			},
		}
	}
};

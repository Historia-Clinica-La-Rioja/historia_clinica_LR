import type { Meta, StoryObj } from '@storybook/angular';
import { PharmarcoDetailComponent } from './pharmarco-detail.component';

const meta: Meta<PharmarcoDetailComponent> = {
	title: 'HSI/PharmarcoDetailComponent',
	component: PharmarcoDetailComponent,
	tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<PharmarcoDetailComponent>;

export const complete: Story = {
	args: {
		pharmaco: {
			pt: 'paracetamol 32 mg/ml, solución oral',
			unitDose: 1,
			dayDose: 1,
			treatmentDays: 31,
			quantity: 2
		}
	}
};

import type { Meta, StoryObj } from '@storybook/angular';
import { PharmacoDetailComponent } from './pharmaco-detail.component';

const meta: Meta<PharmacoDetailComponent> = {
	title: 'HSI/PharmacoDetailComponent',
	component: PharmacoDetailComponent,
	tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<PharmacoDetailComponent>;

export const complete: Story = {
	args: {
		pharmaco: {
			id: 1,
			pt: 'paracetamol 32 mg/ml, solución oral',
			unitDose: 1,
			dayDose: '1',
			treatmentDays: '31',
			quantity: '2'
		}
	}
};

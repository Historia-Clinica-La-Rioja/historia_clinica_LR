import { TemporaryPatientComponent } from "./temporary-patient.component";
import { Meta, StoryObj } from "@storybook/angular";

const meta: Meta<TemporaryPatientComponent> = {
	title: 'HSI/TemporaryPatientComponent',
	component: TemporaryPatientComponent,
	tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<TemporaryPatientComponent>;

export const complete: Story = {
	args: {
		patientDescription: "Usa gorra y anteojos"
	}
};

export const withoutPatientDescription: Story = {
	args: {
		patientDescription: ""
	}
};
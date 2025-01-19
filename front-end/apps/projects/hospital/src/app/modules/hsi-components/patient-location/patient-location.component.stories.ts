import type { Meta, StoryObj } from '@storybook/angular';
import { PatientLocationComponent } from "./patient-location.component";

const meta: Meta<PatientLocationComponent> = {
	title: 'HSI/PatientLocationComponent',
	component: PatientLocationComponent,
	tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<PatientLocationComponent>;

export const LocationBasic: Story = {
	args: {
		patientLocation:  {
			sector: 'Pediatria',
			bedNumber: '2',
			roomNumber: '4',
		},
		professionalFullName: "Jose Perez"
	}
}


export const LocationDoctorsOffice: Story = {
	args: {
		patientLocation:  {
			sector: 'Pediatria',
			doctorsOffice: 'Consultorio 1',
		},
		professionalFullName: "Jose Perez"
	}
}

export const LocationShockroom: Story = {
	args: {
		patientLocation:  {
			sector: 'Pediatria',
			shockroom: 'shockroom 2',
		},
		professionalFullName: "Jose Perez"
	}
}


export const NO_DATA: Story = {
	args: {
		patientLocation:  {	}
	}
}

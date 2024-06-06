import { StoryObj, type Meta, moduleMetadata } from '@storybook/angular';
import { ReactiveFormsModule } from '@angular/forms';
import { ToggleAvailabilityComponent } from './toggle-availability.component';


const meta: Meta<ToggleAvailabilityComponent> = {
	title: 'Telemedicina/ToggleAvaiabilityComponent',
	component: ToggleAvailabilityComponent,
	tags: ['autodocs'],
	decorators: [
        moduleMetadata({
            imports: [ReactiveFormsModule],
        }),
    ],
	args: {
		label: "Availability",
		initialValue: true,
	},
};

export default meta;

type Story = StoryObj<ToggleAvailabilityComponent>;

export const Available: Story = {
	args: {
		initialValue: true,
	},
}

export const NotAvailable:Story = {
	args: {
		initialValue: false,
	},
}

import { StoryObj, type Meta, moduleMetadata } from '@storybook/angular';
import { ReactiveFormsModule } from '@angular/forms';
import { NoDataComponent } from './no-data.component';

const meta: Meta<NoDataComponent> = {
	title: 'Presentation/NoDataComponent',
	component: NoDataComponent,
	tags: ['autodocs'],
	decorators: [
        moduleMetadata({
            imports: [ReactiveFormsModule],
        }),
    ]
};

export default meta;

type Story = StoryObj<NoDataComponent>;

export const full: Story = {
	args: {
		message: "No data"
	},
};

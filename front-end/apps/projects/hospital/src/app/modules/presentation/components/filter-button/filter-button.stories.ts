import { StoryObj, type Meta, moduleMetadata } from '@storybook/angular';
import { ReactiveFormsModule } from '@angular/forms';
import { FilterButtonComponent } from './filter-button.component';


const meta: Meta<FilterButtonComponent> = {
	title: 'Presentation/FilterButtonComponent',
	component: FilterButtonComponent,
	tags: ['autodocs'],
	decorators: [
		moduleMetadata({
			imports: [ReactiveFormsModule],
		}),
	],
};

export default meta;

type Story = StoryObj<FilterButtonComponent>;

export const Expanded: Story = {
	args: {
		isExpanded: true
	}
};

export const Not_Expanded: Story = {
	args: {
		isExpanded: false
	}
};

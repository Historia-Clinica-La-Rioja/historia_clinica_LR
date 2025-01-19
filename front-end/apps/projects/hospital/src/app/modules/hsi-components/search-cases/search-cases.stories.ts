import type { Meta, StoryObj } from '@storybook/angular';
import { SEARCH_CASES, SearchCasesComponent } from './search-cases.component';

const meta: Meta<SearchCasesComponent> = {
	title: 'HSI/SearchCasesComponent',
	component: SearchCasesComponent,
	tags: ['autodocs'],
	argTypes: {
		filters: {
			options: Object.values(SEARCH_CASES),
			control: {
				type: 'multi-select',
			},
			description: 'Usar enum Search Cases',
		}
	},
};

export default meta;
type Story = StoryObj<SearchCasesComponent>;

export const complete: Story = {
	args: {
		filters: [SEARCH_CASES.LOWER_CASE],
		label: 'Ejemplo de lebel',
	}
};

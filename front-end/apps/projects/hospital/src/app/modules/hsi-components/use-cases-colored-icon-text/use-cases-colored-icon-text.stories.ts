import type { Meta, StoryObj } from '@storybook/angular';
import { USE_CASES_COLORED_ICON_TEXT, UseCasesColoredIconTextComponent } from './use-cases-colored-icon-text.component';

const meta: Meta<UseCasesColoredIconTextComponent> = {
	title: 'HSI/UseCasesColoredIconText',
	component: UseCasesColoredIconTextComponent,
	tags: ['autodocs'],
	argTypes: {
		useCase: {
			options: Object.values(USE_CASES_COLORED_ICON_TEXT),
			control: {
				type: 'select',
			},
			description: 'Usar enum Use Cases Colored Icon Text'
		}
	}
};

export default meta;
type Story = StoryObj<UseCasesColoredIconTextComponent>;

export const institution: Story = {
	args: {
		useCase: USE_CASES_COLORED_ICON_TEXT.INSTITUTION
	}
};

export const careLine: Story = {
	args: {
		useCase: USE_CASES_COLORED_ICON_TEXT.CARE_LINE
	}
};

export const problem: Story = {
	args: {
		useCase: USE_CASES_COLORED_ICON_TEXT.PROBLEM
	}
};

export const professional: Story = {
	args: {
		useCase: USE_CASES_COLORED_ICON_TEXT.PROFESSIONAL
	}
};

export const practice: Story = {
	args: {
		useCase: USE_CASES_COLORED_ICON_TEXT.PRACTICE
	}
};

export const specialty: Story = {
	args: {
		useCase: USE_CASES_COLORED_ICON_TEXT.SPECIALTY
	}
};

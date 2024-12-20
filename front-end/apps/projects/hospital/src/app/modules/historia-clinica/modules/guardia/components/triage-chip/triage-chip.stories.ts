import type { Meta, StoryObj } from '@storybook/angular';
import { TriageChipComponent } from './triage-chip.component';

const meta: Meta<TriageChipComponent> = {
	title: 'HSI/TriageChipComponent',
	component: TriageChipComponent,
	tags: ['autodocs'],
	argTypes: {
		category: {
			id: 'number',
			name: 'string'
		}
	}
};

export default meta;
type Story = StoryObj<TriageChipComponent>;

export const Nivel1: Story = {
	args: {
		category: {
			id: 1,
            name: 'Nivel 1'
        },
        isFilled: false
	}
}

export const Nivel2: Story = {
	args: {
		category: {
			id: 2,
            name: 'Nivel 2'
        },
        isFilled: false
	}
}

export const Nivel3: Story = {
	args: {
		category: {
			id: 3,
            name: 'Nivel 3'
        },
        isFilled: false
	}
}

export const Nivel4: Story = {
	args: {
		category: {
			id: 4,
            name: 'Nivel 4'
        },
        isFilled: false
	}
}

export const Nivel5: Story = {
	args: {
		category: {
			id: 5,
            name: 'Nivel 5'
        },
        isFilled: false
	}
}

export const Nivel6: Story = {
	args: {
		category: {
			id: 6,
            name: 'Triage pendiente'
        },
        isFilled: false
	}
}

export const TriageFilled: Story = {
	args: {
		category: {
			id: 1,
            name: 'Nivel 1'
        },
        isFilled: true
	}
}
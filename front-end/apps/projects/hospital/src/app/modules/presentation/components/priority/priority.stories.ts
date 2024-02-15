import { type Meta, type StoryObj } from '@storybook/angular';
import { Priority, PriorityComponent } from './priority.component';

const meta: Meta<PriorityComponent> = {
    title: 'hsi/PriorityComponent',
    component: PriorityComponent,
    tags: ['autodocs'],
    argTypes: {
        priority: {
            options: Object.values(Priority),
            control: {
                type: 'select',
            },
            description: 'Usar enum Priority'
        }
    }
};

export default meta;

type Story = StoryObj<PriorityComponent>;

export const high: Story = {
    args: {
        priority: Priority.HIGH
    }
};

export const medium: Story = {
    args: {
        priority: Priority.MEDIUM
    }
};

export const low: Story = {
    args: {
        priority: Priority.LOW
    }
};


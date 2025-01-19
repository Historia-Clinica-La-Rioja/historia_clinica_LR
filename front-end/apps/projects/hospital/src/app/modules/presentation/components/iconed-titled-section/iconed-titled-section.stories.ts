import { type Meta, type StoryObj } from '@storybook/angular';
import { IconedTitledSectionComponent } from './iconed-titled-section.component';

const meta: Meta<IconedTitledSectionComponent> = {
    title: 'Presentation/IconedTitledSectionComponent',
    component: IconedTitledSectionComponent,
    tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<IconedTitledSectionComponent>;

export const iconedTitledSection: Story = {
    args: {
        icon: 'assignment',
        title: 'Diagnósticos'
    }
};


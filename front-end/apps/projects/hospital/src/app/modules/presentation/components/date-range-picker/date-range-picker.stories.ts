import { moduleMetadata, type Meta, type StoryObj } from '@storybook/angular';
import { DateRangePickerComponent } from './date-range-picker.component';
import { ReactiveFormsModule } from '@angular/forms';

const meta: Meta<DateRangePickerComponent> = {
    title: 'Presentation/DateRangePickerComponent',
    component: DateRangePickerComponent,
    tags: ['autodocs'],
    decorators: [
        moduleMetadata({
            imports: [ReactiveFormsModule],
        }),
    ],
};

export default meta;

type Story = StoryObj<DateRangePickerComponent>;

export const withoutMinMax: Story = {
    args: {
        label: "Label del componente",
        dateRange: {
            start: new Date(),
            end: new Date()
        }
    },
};

export const full: Story = {
    args: {
        label: "Label del componente",
        min: new Date('2023-08-01T00:00:00'),
        max: new Date('2023-09-30T00:00:00'),
        dateRange: {
            start: new Date('2023-09-01T00:00:00'),
            end: new Date('2023-09-30T00:00:00')
        }
    },
}


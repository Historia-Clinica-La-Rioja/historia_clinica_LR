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

export const full: Story = {
	args: {
        label: "Label del componente",
        min: new Date('2024-02-01T00:00:00'),
        max: new Date('2024-10-25T00:00:00'),
        dateRange: {
            start: new Date('2024-04-15T00:00:00'),
            end: new Date('2024-04-25T00:00:00')

		},
	}
}

export const OnlyLabel: Story = {
    args: {
        label: "Label, descripcion"
    },
}

export const MinDate: Story = {
    args: {
		...OnlyLabel.args,
        min: new Date('2024-04-09T00:00:00')
    },
}

export const MaxDate: Story = {
    args: {
		...OnlyLabel.args,
        max: new Date('2024-04-25T00:00:00')
    },
}

export const DateRange: Story = {
    args: {
		...OnlyLabel.args,
		dateRange: {
            start: new Date('2024-04-10T00:00:00'),
            end: new Date('2024-04-18T00:00:00')
        }
    },
}

export const MinDateAndMaxDate: Story = {
    args: {
		...MinDate.args,
        ...MaxDate.args
    },
}

export const MinDateMaxDateAndDateRange: Story = {
    args: {
		...MinDateAndMaxDate.args,
		...DateRange.args
    },
}

export const FixedEndDate: Story = {
    args: {
        ...OnlyLabel.args,
		fixedRangeDays: 7,
    },
}

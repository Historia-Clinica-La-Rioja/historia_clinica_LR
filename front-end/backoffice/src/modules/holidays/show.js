import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
    DateField
} from 'react-admin';

const HolidayShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="description" />
            <DateField source="date" locales="es-ES" />
        </SimpleShowLayout>
    </Show>
);

export default HolidayShow;

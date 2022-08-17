import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';

const HolidayShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="date" />
            <TextField source="description" />
        </SimpleShowLayout>
    </Show>
);

export default HolidayShow;

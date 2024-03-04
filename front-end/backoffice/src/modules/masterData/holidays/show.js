import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';
import {
    SgxDateField,
} from '../../components';

const HolidayShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="description" />
            <SgxDateField source="date" />
        </SimpleShowLayout>
    </Show>
);

export default HolidayShow;

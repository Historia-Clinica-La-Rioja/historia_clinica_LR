import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';

const DepartmentShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="description" />
        </SimpleShowLayout>
    </Show>
);

export default DepartmentShow;

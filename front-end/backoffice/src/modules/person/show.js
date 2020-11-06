import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';


const PersonShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="firstName" />
            <TextField source="lastName" />
        </SimpleShowLayout>
    </Show>
);

export default PersonShow;

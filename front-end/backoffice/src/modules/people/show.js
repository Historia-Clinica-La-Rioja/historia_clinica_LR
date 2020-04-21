import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';


const PeopleShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="firstName" />
            <TextField source="lastName" />
        </SimpleShowLayout>
    </Show>
);

export default PeopleShow;

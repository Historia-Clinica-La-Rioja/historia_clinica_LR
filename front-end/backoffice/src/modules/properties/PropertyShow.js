import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';

const PropertyShow = (props) => {
    return (
        <Show {...props}>
            <SimpleShowLayout>
                <TextField source="id" />
                <TextField source="value" />
                <TextField source="origin" />
            </SimpleShowLayout>
        </Show>
    )
};

export default PropertyShow;

import React from 'react';
import {
    DateField,
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';

const PropertyShow = (props) => {
    return (
        <Show {...props}>
            <SimpleShowLayout>
                <TextField source="property" />
                <TextField source="value" />
                <TextField source="origin" />
                <TextField source="description" />
                <TextField source="nodeId"/>
                <DateField source="updatedOn" showTime />
            </SimpleShowLayout>
        </Show>
    )
};

export default PropertyShow;

import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
} from 'react-admin';

const AddressShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="street" />
            <TextField source="number" />
            <TextField source="floor" />
            <TextField source="apartment" />
            <TextField source="quarter" />
            <TextField source="postcode" />
            <TextField source="quarter" />
            <ReferenceField source="cityId" reference="cities">
                <TextField source="description" />
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default AddressShow;

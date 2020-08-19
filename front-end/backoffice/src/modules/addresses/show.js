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
            <ReferenceField source="cityId" reference="cities">
                <TextField source="description" />
            </ReferenceField>
            <ReferenceField source="departmentId" reference="departments">
                <TextField source="description"/>
            </ReferenceField>
            <ReferenceField source="provinceId" reference="provinces">
                <TextField source="description"/>
            </ReferenceField>
            <TextField source="latitude" />
            <TextField source="longitude" />
        </SimpleShowLayout>
    </Show>
);

export default AddressShow;

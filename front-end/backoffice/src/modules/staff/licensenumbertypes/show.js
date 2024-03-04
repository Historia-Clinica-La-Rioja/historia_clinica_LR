import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField, NumberField,
} from 'react-admin';

const LicenseNumberTypesShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <NumberField source="id" />
            <TextField source="description" />
        </SimpleShowLayout>
    </Show>
);

export default LicenseNumberTypesShow;

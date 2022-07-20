import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField
} from 'react-admin';

const BookingInstitutionShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <ReferenceField source="id" reference="institutions" link={'show'}>
                <TextField source="name"/>
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default BookingInstitutionShow;

import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    DateField,
} from 'react-admin';


const RoomShow = props => (
    <Show  {...props}>
        <SimpleShowLayout>
            <TextField source="roomNumber"/>
            <TextField source="description" />
            <TextField source="type" />
            <DateField source="dischargeDate" />
            <ReferenceField source="clinicalSpecialtySectorId" reference="clinicalspecialtysectors">
                <TextField source="description"/>
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default RoomShow;

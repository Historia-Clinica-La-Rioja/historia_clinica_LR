import React from 'react';
import { ReferenceField, Show, SimpleShowLayout, TextField } from 'react-admin';

const ShockRoomShow = (props) => {
    return (
        <Show  {...props}>
            <SimpleShowLayout>
                <TextField source="description" />
                <ReferenceField source="sectorId" reference="sectors" link="show">
                    <TextField source="description" />
                </ReferenceField>

                <ReferenceField source="institutionId" reference="institutions">
                    <TextField source="name" />
                </ReferenceField>
            </SimpleShowLayout>
        </Show>
    )
};

export default ShockRoomShow;
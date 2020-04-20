import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    TopToolbar,
    EditButton,
    DateField,
    TextInput,
} from 'react-admin';


const RoomShowActions = ({ data }) => {
    return (!data || !data.id) ? <TopToolbar></TopToolbar> :
        (
            <TopToolbar>
                <EditButton basePath="/room" record={{ id: data.id }} />
            </TopToolbar>
        )
};
const RoomShow = props => (
    <Show actions={<RoomShowActions />} {...props}>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextInput source="roomNumber"/>
            <TextField source="description" />
            <TextField source="type" />
            <TextField source="sectorId" />
            <DateField source="dischargeDate" />
            <ReferenceField source="sectorId" reference="sectors">
                <TextField source="description"/>
            </ReferenceField>
            <ReferenceField source="specialityId" reference="specialities">
                <TextField source="name"/>
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default RoomShow;

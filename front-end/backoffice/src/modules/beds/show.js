import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    FunctionField,
} from 'react-admin';

const renderRoom = room => `${room.roomNumber} - ${room.description}`;
const BedShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="bedNumber" />
            <ReferenceField source="roomId" reference="rooms">
                <FunctionField render={renderRoom} />
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default BedShow;

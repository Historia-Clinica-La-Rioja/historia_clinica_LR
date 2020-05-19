import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    FunctionField,
    BooleanField
} from 'react-admin';

const renderRoom = room => `${room.roomNumber} - ${room.description}`;
const BedList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
                <TextField source="id" />
                <TextField source="bedNumber" />
                <ReferenceField source="roomId" reference="rooms">
                    <FunctionField render={renderRoom} />
                </ReferenceField>
                <BooleanField source="enabled" />
                <BooleanField source="available" />
                <BooleanField source="free" />
        </Datagrid>
    </List>
);

export default BedList;

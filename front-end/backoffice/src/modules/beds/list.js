import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    FunctionField,
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
        </Datagrid>
    </List>
);

export default BedList;

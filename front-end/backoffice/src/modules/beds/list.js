import React from 'react';
import {
    List,
    Datagrid,
    TextField,
} from 'react-admin';

const BedList = props => (
    <List {...props} bulkActionButtons={false}>
    <Datagrid rowClick="show">
    <TextField source="id" />
    <TextField source="bedNumber" />
    <TextField source="roomId" />
    </Datagrid>
    </List>
);

export default BedList;
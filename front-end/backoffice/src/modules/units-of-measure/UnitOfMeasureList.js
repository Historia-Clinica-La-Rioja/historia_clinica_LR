import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    BooleanField,
    EditButton,
} from 'react-admin';

const UnitOfMeasureList = props => (
    <List 
        {...props}
        exporter={false}
        bulkActionButtons={false}
        sort={{ field: 'code', order: 'ASC' }}
    >
        <Datagrid rowClick="show">
            <TextField source="code"/>
            <BooleanField source="enabled" />
            <EditButton/>
        </Datagrid>
    </List>
);

export default UnitOfMeasureList;
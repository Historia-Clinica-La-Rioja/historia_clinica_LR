import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    NumberField,
    EditButton,
    DeleteButton
} from 'react-admin';

const ProcedureTemplateList = props => (
    <List 
        {...props}
        exporter={false}
        bulkActionButtons={false}
        sort={{ field: 'id', order: 'ASC' }}
        filter={{ deleted: false }}
    >
        <Datagrid rowClick="show">
            <NumberField source="id"/>
            <TextField source="description"/>
            <EditButton/>
            <DeleteButton/>
        </Datagrid>
    </List>
);

export default ProcedureTemplateList;
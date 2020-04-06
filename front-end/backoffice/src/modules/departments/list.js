import React from 'react';
import {
    List,
    Datagrid,
    TextField,
} from 'react-admin';

const DepartmentList = props => (
    <List {...props} bulkActionButtons={false}>
        <Datagrid rowClick={null}>
            <TextField source="id" />
            <TextField source="description" />
        </Datagrid>
    </List>
);

export default DepartmentList;
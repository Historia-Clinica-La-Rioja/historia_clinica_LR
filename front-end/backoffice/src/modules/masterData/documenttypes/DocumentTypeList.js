import React from 'react';
import {
    List,
    Datagrid,
    TextField,
} from 'react-admin';

const DocumentTypeList = props => (
    <List {...props}  bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="description" />
        </Datagrid>
    </List>
);

export default DocumentTypeList;

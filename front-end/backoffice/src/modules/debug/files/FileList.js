import React from 'react';
import {
    List,
    Datagrid,
    TextField,
     Filter, TextInput, NumberField, NumberInput,
} from 'react-admin';
import {
    SgxDateField,
} from '../../components';

const DocumentFileFilter = (props) => (
    <Filter {...props}>
        <NumberInput source="id" />
        <TextInput source="uuidfile" />
        <TextInput source="name" />
        <TextInput source="relativePath" />
        <TextInput source="source" />
    </Filter>
);

const FileList = props => (
    <List {...props} filters={<DocumentFileFilter />} sort={{ field: 'creationable.createdOn', order: 'DESC' }} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <NumberField source="id" />
            <TextField source="uuidfile" />
            <TextField source="name" />
            <TextField source="relativePath" />
            <TextField source="source" />
            <SgxDateField source="creationable.createdOn" showTime/>
        </Datagrid>
    </List>
);

export default FileList;

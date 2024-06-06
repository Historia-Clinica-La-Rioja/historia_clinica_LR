import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField, Filter, TextInput,
} from 'react-admin';

import {
    SgxDateField,
    SgxSelectInput,
} from '../../components';

const DocumentFileFilter = (props) => (
    <Filter {...props}>
        <TextInput source="filename"/>
        <SgxSelectInput source="typeId" element="documenttypes" optionText="description" allowEmpty={false} />
    </Filter>
);

const DocumentFileList = props => (
    <List {...props} filters={<DocumentFileFilter />} sort={{ field: 'creationable.createdOn', order: 'DESC' }} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="filename" />
            <ReferenceField source="typeId" reference="documenttypes" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <SgxDateField source="creationable.createdOn" showTime/>
        </Datagrid>
    </List>
);

export default DocumentFileList;

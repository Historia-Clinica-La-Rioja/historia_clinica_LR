import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
} from 'react-admin';
import SgxDateField from '../../../dateComponents/sgxDateField';

const ReportQueueList = props => (
    <List {...props} sort={{ field: 'createdOn', order: 'DESC' }} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <SgxDateField source="createdOn" showTime />
            <SgxDateField source="generatedOn" showTime />
            <TextField source="generatedError" sortable={false}/>
            <ReferenceField source="fileId"  link="show"  reference="files" sortable={false}>
                <TextField source="name" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export default ReportQueueList;

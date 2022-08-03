import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField
} from 'react-admin';

const BookingInstitutionList = props => (
    <List {...props} bulkActionButtons={false} sort={{ field: 'institutionId', order: 'ASC' }}>
        <Datagrid rowClick="show">
            <ReferenceField source="id" reference="institutions">
                <TextField source="name"/>
            </ReferenceField>
        </Datagrid>
    </List>
);

export default BookingInstitutionList;


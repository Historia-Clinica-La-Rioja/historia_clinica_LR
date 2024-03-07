import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
} from 'react-admin';

const CareLineInstitutionList = props => {
    return (
        <List {...props} bulkActionButtons={false} >
            <Datagrid rowClick="show">
                <TextField source="id"/>
                <ReferenceField source="institutionId" reference="institutions" >
                    <TextField source="name" />
                </ReferenceField>
                <ReferenceField source="careLineId" reference="carelines" >
                    <TextField source="description" />
                </ReferenceField>
            </Datagrid>
        </List>
    );
};

export default CareLineInstitutionList;
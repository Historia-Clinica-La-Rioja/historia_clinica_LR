import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    Filter,
    TextInput
} from 'react-admin';
import SgxSelectInput from '../../../sgxSelectInput/SgxSelectInput';

const HierarchicalUnitFilter = (props) => (
    <Filter {...props}>
        <TextInput source="alias" />
        <SgxSelectInput source="institutionId" element="institutions" optionText="name" allowEmpty={false} />
    </Filter>
);
const HierarchicalUnitList = props => (
    <List {...props} filters={<HierarchicalUnitFilter />} hasCreate={false} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="alias" />
            <ReferenceField source="typeId" reference="hierarchicalunittypes" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export default HierarchicalUnitList;


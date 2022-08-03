import React from 'react';
import { Datagrid, Filter, List, ReferenceField, TextField, TextInput, usePermissions } from 'react-admin';

import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const DoctorsOfficeFilter = props => (
    <Filter {...props}>
        <TextInput source="description" />
        <SgxSelectInput source="institutionId" element="institutions" optionText="name" allowEmpty={false} />
        <SgxSelectInput label="Sector" source="sectorId" element="sectors" optionText="description" allowEmpty={false} />
    </Filter>
);

const DoctorsOfficeList = (props) => {
    const { permissions } = usePermissions();
    return (
        <List {...props} hasCreate={false} filters={<DoctorsOfficeFilter />} >
            <Datagrid rowClick="show">

                <TextField source="description" />

                <ReferenceField source="institutionId" reference="institutions">
                    <TextField source="name" />
                </ReferenceField>

                <ReferenceField source="sectorId" reference="sectors">
                    <TextField source="description" />
                </ReferenceField>

                {permissions && permissions.isOn('HABILITAR_LLAMADO') && <TextField source="topic" />}

            </Datagrid>
        </List>
    )
};

export default DoctorsOfficeList;

import React from 'react';
import { ReferenceField, Show, SimpleShowLayout, TextField, usePermissions } from 'react-admin';

const DoctorsOfficeShow = (props) => {
    const { permissions } = usePermissions();
    return (
        <Show  {...props}>
            <SimpleShowLayout>
                <TextField source="description" />
                <ReferenceField source="sectorId" reference="sectors" link="show">
                    <TextField source="description" />
                </ReferenceField>

                <ReferenceField source="institutionId" reference="institutions">
                    <TextField source="name" />
                </ReferenceField>

                {permissions && permissions.isOn('HABILITAR_LLAMADO') && <TextField source="topic" />}

            </SimpleShowLayout>
        </Show>
    )
};

export default DoctorsOfficeShow;

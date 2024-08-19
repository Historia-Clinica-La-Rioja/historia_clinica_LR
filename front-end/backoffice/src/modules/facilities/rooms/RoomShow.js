import React from 'react';
import {
    Datagrid,
    EditButton,
    ReferenceField,
    ReferenceManyField,
    Show,
    SimpleShowLayout,
    TextField,
    usePermissions
} from 'react-admin';

import {
    CreateRelatedButton,
    SectionTitle,
    SgxDateField,
} from '../../components';

const RoomShow = props => {
    const { permissions } = usePermissions();
    return (
        <Show  {...props}>
            <SimpleShowLayout>
                <TextField source="roomNumber"/>
                <TextField source="description" />
                <TextField source="type" />
                <SgxDateField source="dischargeDate" />
                <ReferenceField source="sectorId" reference="sectors" link="show">
                    <TextField source="description"/>
                </ReferenceField>
                {permissions && permissions.isOn('HABILITAR_LLAMADO') && <TextField source="topic" />}
                <SectionTitle label="resources.rooms.fields.beds"/>
                <CreateRelatedButton
                    reference="beds"
                    refFieldName="roomId"
                    label="resources.beds.createRelated"
                />
                {/*TODO: Aislar esto en un componente. También se usa en edit.js*/}
                <ReferenceManyField
                    addLabel={false}
                    reference="beds"
                    target="roomId"
                    sort={{ field: 'bedNumber', order: 'DESC' }}
                >
                    <Datagrid rowClick="show">
                        <TextField source="bedNumber" />
                        <EditButton />
                    </Datagrid>
                </ReferenceManyField>
            </SimpleShowLayout>
        </Show>
    )
};

export default RoomShow;

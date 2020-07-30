import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    TopToolbar,
    EditButton,
    ReferenceManyField,
    Datagrid,
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';

const InstitutionShowActions = ({ data }) => {
    return (!data || !data.id) ? <TopToolbar></TopToolbar> :
        (
            <TopToolbar>
                <EditButton basePath="/institutions" record={{ id: data.id }} />
            </TopToolbar>
        )
};
const InstitutionShow = props => (
    <Show actions={<InstitutionShowActions />} {...props}>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="name" />
            <TextField source="website" />
            <TextField source="phone" />
            <TextField source="email" />
            <TextField source="cuit" />
            <TextField source="sisaCode" />
            <ReferenceField source="addressId" reference="addresses" link={'show'}>
                <TextField source="cityId"/>
            </ReferenceField>
            <ReferenceField label="Latitud" source="addressId" reference="addresses" link={false}>
                <TextField  source="latitud" />
            </ReferenceField>
            <ReferenceField label="Longitud" source="addressId" reference="addresses" link={false}>
                <TextField  source="longitud" />
            </ReferenceField>
            <SectionTitle label="resources.institutions.fields.sectors"/>
            <CreateRelatedButton
                reference="sectors"
                refFieldName="institutionId"
                label="resources.sectors.createRelated"
            />
            {/*TODO: Aislar esto en un componente. También se usa en edit.js*/}
            <ReferenceManyField
                addLabel={false}
                reference="sectors"
                target="institutionId"
                sort={{ field: 'description', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
                    <TextField source="description" />
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>
        </SimpleShowLayout>
    </Show>
);

export default InstitutionShow;

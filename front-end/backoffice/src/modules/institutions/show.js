import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    TopToolbar,
    EditButton,
} from 'react-admin';


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
            <ReferenceField source="addressId" reference="addresses">
                <TextField source="cityId"/>
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default InstitutionShow;

import React from 'react';
import {
    BooleanField,
    Datagrid,
    DeleteButton,
    EditButton,
    ListButton,
    ReferenceField,
    ReferenceManyField,
    Show,
    SimpleShowLayout,
    TextField, TopToolbar
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';

const CareLineShowActions = ({data}) => {
    return (!data || !data.id) ? <TopToolbar/> :
        (
            <TopToolbar>
                <ListButton basePath="/carelines" label="Listar Líneas de cuidado"/>
                <EditButton basePath="/carelines" record={{id: data.id}}/>
            </TopToolbar>
        )
};
const CareLineShow = props => (
    <Show actions={<CareLineShowActions/>} {...props}>
        <SimpleShowLayout>
            <TextField source="description"/>
            <BooleanField source="consultation"/>
            <BooleanField source="procedure"/>
            <SectionTitle label="resources.clinicalspecialtycarelines.name"/>
            <CreateRelatedButton
                reference="clinicalspecialtycarelines"
                refFieldName="careLineId"
                label="resources.clinicalspecialtycarelines.addRelated"
            />
            <ReferenceManyField
                addLabel={false}
                reference="clinicalspecialtycarelines"
                target="careLineId"
                sort={{field: 'id', order: 'DESC'}}
            >
                <Datagrid>
                    <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                        <TextField source="name"/>
                    </ReferenceField>
                    <DeleteButton/>
                </Datagrid>
            </ReferenceManyField>

        </SimpleShowLayout>
    </Show>
);

export default CareLineShow;

import React from 'react';
import {
    BooleanInput,
    Datagrid,
    DeleteButton,
    Edit,
    ReferenceField,
    ReferenceManyField,
    required,
    SimpleForm,
    TextField,
    TextInput,
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';
import CustomToolbar from '../components/CustomToolbar';

const CareLineEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="description" validate={[required()]} />

            <BooleanInput source="consultation" disabled={false} initialValue={false}/>

            <BooleanInput source="procedure" disabled={false} initialValue={false}/>

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
            >
                <Datagrid>
                    <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                        <TextField source="name" />
                    </ReferenceField>
                    <DeleteButton redirect={false} />
                </Datagrid>
            </ReferenceManyField>
            <SectionTitle label="resources.carelineproblems.name"/>
            <CreateRelatedButton
                reference="carelineproblems"
                refFieldName="careLineId"
                label="resources.carelineproblems.addRelated"
            />
            <ReferenceManyField
                addLabel={false}
                reference="carelineproblems"
                target="careLineId"
                sort={{field: 'id', order: 'DESC'}}
            >
                <Datagrid>
                    <ReferenceField source="snomedId" reference="snomedconcepts" link="show">
                        <TextField source="pt"/>
                    </ReferenceField>
                    <DeleteButton redirect={false} />
                </Datagrid>
            </ReferenceManyField>
        </SimpleForm>
    </Edit>
);

export default CareLineEdit;

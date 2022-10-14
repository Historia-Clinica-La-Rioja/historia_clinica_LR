import React from 'react';
import {
    Create,
    SimpleForm,
    ReferenceInput,
    AutocompleteInput,
    ReferenceField,
    TextField,
    usePermissions,
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';
import SectionTitle from "../components/SectionTitle";
import {ADMINISTRADOR, ROOT} from "../roles";

const CareLineInstitutionPracticeCreate = props => {
    const institutionId = props?.location?.state?.record?.institutionId;
    const careLineInstitutionId = props?.location?.state?.record?.careLineInstitutionId;
    const redirect = `/carelineinstitution/${careLineInstitutionId}/show`;
    const { permissions } = usePermissions();
    const userIsRootOrAdmin = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR.role) || (roleAssignment.role === ROOT.role)).length > 0;
    return(
        <Create {...props}>
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar/>}>

                <ReferenceField source="institutionId" reference="institutions">
                    <TextField source="name" />
                </ReferenceField>
                <ReferenceField source="careLineId" reference="carelines">
                    <TextField source="description" />
                </ReferenceField>

                <SectionTitle label="resources.carelineinstitutionpractice.fields.newpractice"/>
                <ReferenceInput
                    source="snomedRelatedGroupId"
                    reference="practicesinstitution"
                    sort={{ field: 'description', order: 'ASC' }}
                    filterToQuery={searchText => ({id: careLineInstitutionId, institutionId: institutionId})}
                >
                    <AutocompleteInput optionText="description" optionValue="id" disabled={userIsRootOrAdmin} resettable />
                </ReferenceInput>

            </SimpleForm>
        </Create>
    );
};

export default CareLineInstitutionPracticeCreate;

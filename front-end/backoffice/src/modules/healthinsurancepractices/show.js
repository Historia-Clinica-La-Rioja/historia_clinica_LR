import {DeleteButton, ReferenceField, Show, SimpleShowLayout, TextField} from 'react-admin';

const HealthInsurancePracticeShow = props => (
    <Show {...props} >
        <SimpleShowLayout >
            <ReferenceField source="medicalCoverageId" reference="medicalcoverages">
                <TextField source="name" />
            </ReferenceField>
            <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties" link={false}>
                <TextField source="name" />
            </ReferenceField>
            <ReferenceField source="mandatoryMedicalPracticeId" reference="mandatorymedicalpractices" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <TextField source="coverageInformation"/>
            <DeleteButton />
        </SimpleShowLayout>
    </Show>
);

export default HealthInsurancePracticeShow;
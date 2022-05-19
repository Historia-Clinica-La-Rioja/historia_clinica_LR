import {Datagrid, DeleteButton, List, ReferenceField, TextField} from 'react-admin';

const HealthInsurancePracticeList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
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
        </Datagrid>
    </List>
);

export default HealthInsurancePracticeList;
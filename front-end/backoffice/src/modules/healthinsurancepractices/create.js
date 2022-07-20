import {
    Create,
    SimpleForm,
    TextInput
} from 'react-admin';
import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const HealthInsurancePracticeCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="list">
        <SgxSelectInput source="medicalCoverageId"
                    element="medicalcoverages"
                    optionText="name"
                    alwaysOn
                    allowEmpty={false} />

            <SgxSelectInput source="clinicalSpecialtyId"
                    element="clinicalspecialties"
                    optionText="name"
                    alwaysOn
                    allowEmpty={false} />

            <SgxSelectInput source="mandatoryMedicalPracticeId"
                    element="mandatorymedicalpractices"
                    optionText="description"
                    alwaysOn
                    allowEmpty={false} />

            <TextInput source="coverageInformation"/>
        </SimpleForm>
    </Create>
);

export default HealthInsurancePracticeCreate;
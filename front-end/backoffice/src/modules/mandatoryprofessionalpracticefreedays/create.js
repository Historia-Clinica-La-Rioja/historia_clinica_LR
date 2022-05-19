import {
    ArrayInput,
    AutocompleteInput,
    Create,
    ReferenceInput,
    required,
    SimpleForm,
    SimpleFormIterator
} from 'react-admin';
import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const MandatoryProfessionalPracticeFreeDaysCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="list">
            <ReferenceInput
                source="healthcareProfessionalId"
                reference="healthcareprofessionals"
                label="resources.healthcareprofessionalhealthinsurances.fields.licenseNumber"
                sort={{ field: 'licenseNumber', order: 'ASC' }}
            >
                <AutocompleteInput optionText="licenseNumber" optionValue="id" validate={[required()]}/>
            </ReferenceInput>

            <SgxSelectInput source="clinicalSpecialtyId"
                    element="clinicalspecialties"
                    optionText="name"
                    alwaysOn
                    allowEmpty={false} 
                    validate={[required()]}/>

            <SgxSelectInput source="mandatoryMedicalPracticeId"
                    element="mandatorymedicalpractices"
                    optionText="description"
                    alwaysOn
                    allowEmpty={false} 
                    validate={[required()]}/>

            <ArrayInput source="days" validate={[required()]}>
                <SimpleFormIterator>
                    <AutocompleteInput validate={[required()]} label="Día" optionValue="id" choices={daysChoices} optionText="name"/>
                </SimpleFormIterator>
            </ArrayInput>
        </SimpleForm>
    </Create>
);

const daysChoices = [
    { id: 1, name:'Lunes' },
    { id: 2, name:'Martes' },
    { id: 3, name:'Miércoles' },
    { id: 4, name:'Jueves' },
    { id: 5, name:'Viernes' },
    { id: 6, name:'Sábado' },
 ];

export default MandatoryProfessionalPracticeFreeDaysCreate;
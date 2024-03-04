import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required,
    maxLength,
    FormDataConsumer,
    usePermissions,
    ReferenceInput,
    SelectInput,
    BooleanInput

} from 'react-admin';
import SgxSelectInput from '../../../sgxSelectInput/SgxSelectInput';
import CustomToolbar from '../../components/CustomToolbar';
import { ROOT } from '../../roles';

const CENTRO_DE_DIAGNOSTICO = 2;
const InstitutionField = ({formData, ...res}) => {
    return formData.pacServerType === CENTRO_DE_DIAGNOSTICO ? (
        <ReferenceInput {...res}
            perPage={1000}
            sort={{ field: 'name', order: 'ASC' }}>
                <SelectInput optionText="name" optionValue="id" required={true}/>
        </ReferenceInput>
    ): null
}

const GlobalPacsCreate = props => {
    const { permissions } = usePermissions();
    const userIsRoot = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ROOT.role)).length > 0;
    return(
        <Create {...props} hasCreate={userIsRoot}>
            <SimpleForm redirect="show" toolbar={<CustomToolbar/>}>

                {/* Name */}
                <TextInput source="name" validate={[
                    required(),
                    maxLength(20)]}
                />

                {/* AETITLE */}
                <TextInput source="aetitle" validate={[
                    required(),
                    maxLength(15)]}
                />

                {/* Domain */}
                <TextInput source="domain" validate={[
                    required(),
                    maxLength(50)]}
                />

                {/* PAC Server Type */}
                <FormDataConsumer>
                    {formDataProps => ( <SgxSelectInput {...formDataProps} source="pacServerType" element="pacservertypes" optionText="description" allowEmpty={false} required={true} />)}
                </FormDataConsumer>

                {/* Institutions */}
                <FormDataConsumer>
                    {formDataProps => ( <InstitutionField {...formDataProps} reference="institutions" source="institutionId"/>)}
                </FormDataConsumer>

                {/* PAC Server Protocol */}
                <FormDataConsumer>
                    {formDataProps => ( <SgxSelectInput {...formDataProps} source="pacServerProtocol" element="pacserverprotocols" optionText="description" allowEmpty={false} required={true} />)}
                </FormDataConsumer>

                {/* Username */}
                <BooleanInput label="resources.pacservers.fields.active" source="active" />

            </SimpleForm>
        </Create>
    );
};

export default GlobalPacsCreate;

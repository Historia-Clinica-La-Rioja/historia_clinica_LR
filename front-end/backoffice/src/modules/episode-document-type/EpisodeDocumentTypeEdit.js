import {
    TextInput,
    Edit,
    SimpleForm,
    required,
    usePermissions,
    FormDataConsumer
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import {ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE} from "../roles";
import { ConsentTypes } from './EpisodeDocumentTypeCreate';

const EpisodeDocumentTypeEdit = props => {
    const {permissions} = usePermissions();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (<Edit {...props} hasEdit={userIsAdminInstitutional}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="description" validate={[required()]} />

            <FormDataConsumer>
                {formDataProps => (<ShowBody {...formDataProps}/>)}
            </FormDataConsumer>
        </SimpleForm>
    </Edit>)
};

const ShowBody = ({formData}) => {
    return (formData.consentId !== ConsentTypes()[0].id) ? <TextInput source="richTextBody" label="Detalles del documento" validate={[required()]} fullWidth multiline/> : null
}

export default EpisodeDocumentTypeEdit;
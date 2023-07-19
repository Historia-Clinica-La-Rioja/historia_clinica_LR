import {
    TextInput,
    Edit,
    SimpleForm,
    required,
    usePermissions,
    FormDataConsumer,
    RichTextField,
    RichTextInput
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
            {userIsAdminInstitutional && <FormDataConsumer>
                {formDataProps => ((formDataProps.formData.consentId !== ConsentTypes()[0].id) ? <RichTextInput source="richTextBody" label="Detalles del documento" validate={[required()]} fullWidth multiline/> : null)}
            </FormDataConsumer>}

            {!userIsAdminInstitutional && <FormDataConsumer>
                {formDataProps => ((formDataProps.formData.consentId !== ConsentTypes()[0].id) ? <RichTextField source="richTextBody" label="Detalles del documento" validate={[required()]} fullWidth multiline/> : null)}
            </FormDataConsumer>}
        </SimpleForm>
    </Edit>)
};

export default EpisodeDocumentTypeEdit;
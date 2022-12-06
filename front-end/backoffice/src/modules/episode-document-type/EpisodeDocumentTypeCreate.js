import {
    Create,
    required,
    SimpleForm,
    TextInput
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";

const EpisodeDocumentTypeCreate = (props) => (
    <Create {...props}>
        <SimpleForm toolbar={<CustomToolbar />} redirect="list">
            <TextInput source="description" label="Tipo de documento" validate={[required()]} />
        </SimpleForm>
    </Create>
);


export default EpisodeDocumentTypeCreate;
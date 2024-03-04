import {
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';

const SnomedConceptShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="sctid"/>
            <TextField source="pt" />
        </SimpleShowLayout>
    </Show>
);

export default SnomedConceptShow;
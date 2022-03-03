import {
    Datagrid,
    Pagination,
    ReferenceField,
    ReferenceManyField,
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";

const SnomedGroupShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="description" />
            <TextField source="ecl" />
            <TextField source="customId" />
            <SgxDateField source="lastUpdate" />
            <ReferenceManyField
                reference="snomedrelatedgroups"
                target="groupId"
                sort={{field: 'orden', order: 'ASC'}}
                perPage={10}
                pagination={<Pagination rowsPerPageOptions={[10, 25, 50]} />}
                label="resources.snomedgroups.fields.snomedConcepts"
            >
                <Datagrid>
                    <ReferenceField source="snomedId" reference="snomedconcepts" label="resources.snomedconcepts.fields.sctid" sortable={false} link="show">
                        <TextField source="sctid" />
                    </ReferenceField>
                    <ReferenceField source="snomedId" reference="snomedconcepts" label="resources.snomedconcepts.fields.pt" sortable={false} link={false}>
                        <TextField source="pt"/>
                    </ReferenceField>
                    <TextField source="orden"/>
                    <SgxDateField source="lastUpdate" />
                </Datagrid>
            </ReferenceManyField>
        </SimpleShowLayout>
    </Show>
);

export default SnomedGroupShow;

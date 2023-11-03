import React from 'react';
import {
    Show,
    TextField,
    NumberField,
    TopToolbar,
    EditButton,
    Button,
    ReferenceField,
    SimpleShowLayout,
    SelectField,
    useRedirect
} from 'react-admin';
import ArrowBackIcon from "@material-ui/icons/ArrowBack";
import {makeStyles} from "@material-ui/core/styles";

const MoveStudiesActions = ({ data }) => {
    const useStyles = makeStyles({
        button: {
            marginRight: '1%',
        },
        deleteButton: {
            marginLeft: 'auto',
        }
    });
    const classes = useStyles();
    const redirect = useRedirect();
    const goBack = () => {
        redirect('/movestudies');
    }
    return (!data || !data.id) ? <TopToolbar/> :
        (
            <TopToolbar>
                <Button
                    variant="outlined"
                    color="primary"
                    size="medium"
                    className={classes.button}
                    label="sgh.components.customtoolbar.backButton"
                    onClick={goBack}>
                    <ArrowBackIcon />
                </Button>
                <EditButton
                    basePath="/movestudies"
                    record={{id:data.id }}
                    variant="outlined"
                    color="primary"
                    size="medium"
                    className={classes.button}
                />
            </TopToolbar>
        )
};



const MoveStudiesShow = (props) => (
    <Show actions={<MoveStudiesActions />} {...props} >
        <SimpleShowLayout>
            <ReferenceField link={false} source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>
            <TextField source="imageId" />
            <TextField source="sizeImage" />
            <TextField source="result" />
            <SelectField source="status" choices={[
               { id: 'PENDING', name: 'resources.movestudies.pending' },
               { id: 'FINISHED', name: 'resources.movestudies.finished' },
               { id: 'MOVING', name: 'resources.movestudies.moving' }
            ]} />

            <ReferenceField link={false} source="pacServerId"  reference="pacservers">
                <TextField  source="name" />
            </ReferenceField>
            <ReferenceField link={false}source="orchestratorId" reference="orchestrator">
                <TextField  source="name" />
            </ReferenceField>
            <NumberField source="attempsNumber"/>
            <NumberField source="priorityMax" disabled/>

        </SimpleShowLayout>
    </Show>
);


export default MoveStudiesShow;

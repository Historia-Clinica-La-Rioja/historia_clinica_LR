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
    DateField,
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
        redirect('/allmovestudies');
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
                    basePath="/allmovestudies"
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
           <TextField source="identificationNumber" />
           <TextField source="firstName" />
           <TextField source="lastName" />
           <DateField source="appoinmentDate" showTime options={{ year: 'numeric', month: '2-digit', day: '2-digit'}}/>
           <TextField source="appoinmentTime" type="time"/>
           <TextField source="imageId" />
           <SelectField source="status" choices={[
                { id: 'PENDING', name: 'resources.allmovestudies.pending' },
                { id: 'FINISHED', name: 'resources.allmovestudies.finished' },
                { id: 'MOVING', name: 'resources.allmovestudies.moving' },
                { id: 'FAILED', name: 'resources.allmovestudies.failed' }
           ]} />
           <TextField source="result" />
           <TextField source="acronym" />

        </SimpleShowLayout>
    </Show>
);


export default MoveStudiesShow;

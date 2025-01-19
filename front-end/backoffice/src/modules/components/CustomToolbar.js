import React from 'react';
import { SaveButton, DeleteButton, Toolbar, Button } from 'react-admin';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles({
    button: {
        marginRight: '1%',
    },
    deleteButton: {
        marginLeft: 'auto',
    }
});

const CustomToolbar = ({isEdit, deleteRedirect, canSave, ...props}) => {
    const classes = useStyles();

    const goBack = () => {
        window.history.back();
    }

    const deleteButton = () => {
        return deleteRedirect ? deleteAndRedirectButton() : deleteDefaultButton();
    }

    const deleteDefaultButton = () => {
        return <DeleteButton className={classes.deleteButton}/>
    }

    const deleteAndRedirectButton = () => {
        return <DeleteButton className={classes.deleteButton} redirect={deleteRedirect}/>
    }

    return (
        <Toolbar {...props}>
            <Button
                variant="outlined"
                color="primary"
                size="medium"
                className={classes.button}
                label="sgh.components.customtoolbar.backButton"
                onClick={goBack}>
                    <ArrowBackIcon />
            </Button>
            <SaveButton disabled={canSave === false} />
            {isEdit ? deleteButton() : null}
        </Toolbar>
    )

}

export default CustomToolbar;
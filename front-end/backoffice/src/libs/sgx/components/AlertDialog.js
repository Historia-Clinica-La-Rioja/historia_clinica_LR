import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';

import { useTranslate } from 'react-admin';

function AlertDialog({
    title = 'error.generic.title',
    message = 'Revise el log', 
    action = 'ra.action.close',
    dismiss = () => true,
}) {
  const [open, setOpen] = React.useState(true);
  const translate = useTranslate();

  const handleClose = () => {
    setOpen(dismiss())
  };

  return (
    <div>
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">{translate(title)}</DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            {message}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary" autoFocus>
          {translate(action)}
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}



export default AlertDialog;
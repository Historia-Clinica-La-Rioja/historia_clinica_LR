import LockIcon from '@material-ui/icons/Lock';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';

const passwordReset = (permissions: SGXPermissions) => ({
    icon: LockIcon,
});

export default passwordReset;

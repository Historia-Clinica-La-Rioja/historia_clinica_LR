import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import LockIcon from '@material-ui/icons/Lock';

const passwordReset = (permissions: SGXPermissions) => ({
    icon: LockIcon,
});

export default passwordReset;

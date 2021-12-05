import icon from '@material-ui/icons/SingleBed';

import BedShow from './BedShow';
import BedList from './BedList';
import BedCreate from './BedCreate';
import BedEdit from './BedEdit';

const beds =  {
    icon,
    show: BedShow,
    list: BedList,
    create: BedCreate,
    edit: BedEdit,
};

export default beds;

import HierarchicalUnitShow from './HierarchicalUnitShow';
import HierarchicalUnitList from './HierarchicalUnitList';
import HierarchicalUnitCreate from './HierarchicalUnitCreate';
import HierarchicalUnitEdit from './HierarchicalUnitEdit';

const hierarchicalunits = {
    list: HierarchicalUnitList,
    show: HierarchicalUnitShow,
    create: HierarchicalUnitCreate,
    edit: HierarchicalUnitEdit,
    options: {
        submenu: 'facilities'
    }
};
export default hierarchicalunits;

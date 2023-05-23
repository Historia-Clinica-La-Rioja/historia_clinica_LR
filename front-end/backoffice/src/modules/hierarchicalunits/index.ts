import HierarchicalUnitShow from './HierarchicalUnitShow';
import HierarchicalUnitList from './HierarchicalUnitList';
import HierarchicalUnitCreate from './HierarchicalUnitCreate';
import HierarchicalUnitEdit from './HierarchicalUnitEdit';

const hierarchicalunits = {
    show: HierarchicalUnitShow,
    list: HierarchicalUnitList,
    create: HierarchicalUnitCreate,
    edit: HierarchicalUnitEdit,
    options: {
        submenu: 'facilities'
    }
};
export default hierarchicalunits;

import React from 'react';
import Flow from '../../../libs/sgx/reactFlow/Flow';
import { 
  fetchHierarchicalUnits,
  fetchHierarchicalUnitTypes,
} from '../hierarchicalunits/FetchHierarchicalUnitService';

class UnidadesJerarquicas extends React.Component {

  constructor() {
    super();
    this.state = {
      sortedNodes: null
    }
  }

  componentDidMount() {

    const units$ = fetchHierarchicalUnits(this.props.institutionId).then(
      hierarchicalUnits => { return updateHUResponsablesValues(hierarchicalUnits) }
    )

    Promise.all([units$, fetchHierarchicalUnitTypes()])
      .then(
        ([hierarchicalUnits, hierarchicalUnitTypes]) => {
          const hierarchicalUnitCustomNodes = toHierarchicalUnitCustomNodes(hierarchicalUnits, hierarchicalUnitTypes)
          this.setState({ sortedNodes: getSortedNodes(hierarchicalUnitCustomNodes) })
        })
  }

  render() {
    if (!this.state.sortedNodes)
      return null;
    if (!this.state.sortedNodes.length)
      return <span style={{ paddingLeft: 10, marginTop: 0, color: '#8c8c8c' }}>Sin unidades jerárquicas definidas</span>
    return <Flow sortedNodes={this.state.sortedNodes} />;
  }
}

export default UnidadesJerarquicas


const updateHUResponsablesValues = (originalArray) => {
  const resultMap = {};

  originalArray.forEach(element => {
    const { id, responsable, ...rest } = element;

    if (resultMap[id]) {
      resultMap[id].responsable.push(` - ${responsable}`);
    } else {
      resultMap[id] = { id, responsable: [responsable], ...rest };
    }
  });

  return Object.values(resultMap);
}

const toHierarchicalUnitCustomNodes = (nodesDto, hierarchicalUnitTypes) => {
  return nodesDto.map(n => {
    return {
      id: n.id.toString(),
      type: 'custom',
      parents: n.relations,
      data: {
        name: n.alias,
        extraInfo: {
          Clasificación: hierarchicalUnitTypes.content.find(t => t.id === n.typeId).description,
          Usuarios: n.usersAmount,
          Responsables: n.responsable,
          "Servicio inmediato superior": nodesDto.find(t => t.id === n.closestServiceId)?.alias, 
        }
      }
    }
  });
}

const getSortedNodes = (nodes) => {
  return nodes.sort((a, b) => a.id - b.id)
}
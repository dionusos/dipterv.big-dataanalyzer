import React from 'react';
import * as model from '../../model/Model.js';
import './DrilldownDimensionSelector.css'

class DrilldownDimensionSelector extends React.Component {
    constructor(props) {
        super(props);
        this.letsDrilldown = this.letsDrilldown.bind(this);
    }

    componentDidMount() {
    }

    letsDrilldown(e) {
        model.newDrilldown(this.props.measurementId);
    }

    render() {
        return (
            <div id={"myModal" + this.props.measurementId} className={"modal"}>

                <div className="modal-content">
                    <p>Select some dimensions</p>
                    <div className={"fixedDimensionAndValue" + this.props.measurementId}></div>
                    <select className={"drilldownDimensionsSelector" + this.props.measurementId} multiple>
                        {
                            model.dataStoreToDimensions[model.selectedDatasource].map((dimension) => (
                                <option value={dimension.name}>{dimension.displayName}</option>
                            ))
                        }
                    </select>
                    <select className={"charTypeSelector" + this.props.measurementId}>
                        <option value="column">Column</option>
                        <option value="pie">Pie</option>
                        <option value="timeseries">Time series</option>
                    </select>
                    <button onClick={this.letsDrilldown}>Go!</button>
                </div>

            </div>

        );
    }
}

export default DrilldownDimensionSelector;
import React from 'react';
import * as appstate from '../appstate.js';
import './DrilldownDimensionSelector.css'

class DrilldownDimensionSelector extends React.Component {
    constructor(props) {
        super(props);
        this.letsDrilldown = this.letsDrilldown.bind(this);
    }

    componentDidMount() {
        appstate.updateDimensionSelector(
            appstate.datasourceByMeasurement(this.props.measurementId),
            "drilldownDimensionsSelector" + this.props.measurementId);
    }

    letsDrilldown(e) {
        appstate.newDrilldown(this.props.measurementId);
    }

    render() {
        return (
            <div id={"myModal" + this.props.measurementId} className={"modal"}>

                <div className="modal-content">
                    <p>Select some dimensions</p>
                    <div className={"fixedDimensionAndValue" + this.props.measurementId}></div>
                    <select className={"drilldownDimensionsSelector" + this.props.measurementId} multiple>
                    </select>
                    <button onClick={this.letsDrilldown}>Let's Drilldown</button>
                </div>

            </div>

        );
    }
}

export default DrilldownDimensionSelector;
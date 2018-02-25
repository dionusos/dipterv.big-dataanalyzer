import React from 'react';
import * as appstate from '../appstate.js';
import NewMeasurement from "./NewMeasurement";
import Drilldown from "./Drilldown";
import './Measurement.css'
import DrilldownDimensionSelector from './DrilldownDimensionSelector.js'

class Measurement extends React.Component {

    constructor(props){
        super(props);
    }
    render() {
        return (
            <div id="measurement">
                <h2>Measurement id is {appstate.measurementById(this.props.id).id}</h2>
                {
                    appstate.measurementById(this.props.id).drilldowns.map((drilldown) => (
                        <Drilldown id={drilldown.id} measurement={this.props.id}/>
                    ))
                }
                <DrilldownDimensionSelector measurementId={this.props.id}/>
            </div>
        );
    }
}

export default Measurement;
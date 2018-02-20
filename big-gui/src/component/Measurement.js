import React from 'react';
import * as appstate from '../appstate.js';
import NewMeasurement from "./NewMeasurement";
import Drilldown from "./Drilldown";

class Measurement extends React.Component {

    constructor(props){
        super(props);
    }
    render() {
        return (
            <div>
                <h2>Measurement id is {appstate.measurementById(this.props.id).id}</h2>
                {
                    appstate.measurementById(this.props.id).drilldowns.map((drilldown) => (
                        <Drilldown id={drilldown.id} measurement={this.props.id}/>
                    ))
                }
            </div>

        );
    }
}

export default Measurement;
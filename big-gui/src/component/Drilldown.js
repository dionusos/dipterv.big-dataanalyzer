import React from 'react';
import * as appstate from '../appstate.js';
import NewMeasurement from "./NewMeasurement";
import './Drilldown.css'

class Drilldown extends React.Component {
    constructor(props){
        super(props);
    }
    render() {
        return (
            <div>
                <h3>This is a drilldown, id={this.props.id}</h3>
                <div id={"drilldown_" + this.props.measurement + "_" + this.props.id} className="drilldown">

                </div>
            </div>

        );
    }
}

export default Drilldown;
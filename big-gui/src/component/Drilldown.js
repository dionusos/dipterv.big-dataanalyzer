import React from 'react';
import * as appstate from '../appstate.js';
import NewMeasurement from "./NewMeasurement";
import './Drilldown.css'
import loading_cat from './loading-cat.png'

class Drilldown extends React.Component {
    constructor(props){
        super(props);
        this.deleteFrom = this.deleteFrom.bind(this);
    }

    deleteFrom() {
        appstate.deleteDrilldownsFrom(this.props.id);
    }

    render() {
        return (
            <div>
                <h3>This is a drilldown, id={this.props.id}</h3>
                <button onClick={this.deleteFrom}>Delete</button>
                <div id={"drilldown_" + this.props.measurement + "_" + this.props.id} className="drilldown">
                    <img src={loading_cat}/>

                </div>
            </div>

        );
    }
}

export default Drilldown;
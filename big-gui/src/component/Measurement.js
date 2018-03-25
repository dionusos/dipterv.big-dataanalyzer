import React from 'react';
import Drilldown from "./Drilldown";
import './Measurement.css'
import DrilldownDimensionSelector from './DrilldownDimensionSelector.js'
import * as model from '../model/Model';

class Measurement extends React.Component {

    constructor(props){
        super(props);
    }
    render() {
        return (
            <div id="measurement">
                <h2>Measurement id is {model.measurementById(this.props.id).id}</h2>
                {
                    this.props.measurement.drilldowns.map((drilldown) => (
                        <Drilldown key={drilldown.id} id={drilldown.id} measurement={this.props.id} drilldown={drilldown}/>
                    ))
                }
                <DrilldownDimensionSelector measurementId={this.props.id}/>
            </div>
        );
    }
}

export default Measurement;
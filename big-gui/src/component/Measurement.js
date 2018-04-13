import React from 'react';
import Drilldown from "./Drilldown";
import './Measurement.css'

class Measurement extends React.Component {

    constructor(props){
        super(props);
    }

    render() {
        return (
            <div id="measurement">
                {
                    this.props.measurement.drilldowns.map((drilldown) => (
                        <Drilldown key={drilldown.id.measurementId + ":" + drilldown.id.id} drilldown={drilldown}/>
                    ))
                }
            </div>
        );
    }
}

export default Measurement;
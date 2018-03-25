import React from 'react';
import * as model from '../model/Model.js';
import NewMeasurement from "./NewMeasurement";
import Measurement from "./Measurement";
import './Frame.css'

class Frame extends React.Component {
    constructor(props){
        super(props);
        model.callbacks.push(this);
        this.state        = { count: 0 } ;

    }

    update() {
        this.setState((prevState) => {
            return {count: prevState.count + 1}
        });
    }

    render() {
        return (
            <div>
                <h1>BIG data-analyzer</h1>
                <div id="frame">
                    <div id="measurements">
                        {
                            model.measurements.map((measurement) => (
                            <Measurement key={measurement.id} id={measurement.id} measurement={measurement}/>
                            ))
                        }
                        <NewMeasurement/>
                    </div>

                </div>
            </div>

        );
    }
}

export default Frame;
import React from 'react';
import * as appstate from '../appstate.js';
import NewMeasurement from "./NewMeasurement";
import Measurement from "./Measurement";
import './Frame.css'

class Frame extends React.Component {
    constructor(props){
        super(props);
        appstate.callbacks.push(this);
        this.state        = { count: 0 } ;

    }

    incrementCount() {
        this.setState((prevState) => {
            // Important: read `prevState` instead of `this.state` when updating.
            return {count: prevState.count + 1}
        });
    }

    componentWillReceiveProps(nextProps) {

    }

    update() {
        this.incrementCount();
    }

    render() {
        return (
            <div>
                <h1>BIG data-analyzer</h1>
                <div id="frame">
                    <div id="measurements">
                        {
                            appstate.measurements.map((measurement) => (
                            <Measurement id={measurement.id}/>
                            ))
                        }
                    </div>
                    <NewMeasurement/>
                </div>
            </div>

        );
    }
}

export default Frame;
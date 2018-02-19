import React from 'react';
import * as appstate from '../appstate.js';
import NewMeasurement from "./NewMeasurement";

class Frame extends React.Component {
    render() {
        return (
            <div>
                <h1>BIG data-analyzer</h1>
                <div className="frame">
                    <div className="measurements">

                    </div>
                    <NewMeasurement/>
                </div>
            </div>

        );
    }
}

export default Frame;
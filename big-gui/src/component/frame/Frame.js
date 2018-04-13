import React from 'react';
import NewMeasurement from "../new_measurement/NewMeasurement";
import Measurement from "../measurement/Measurement";
import './Frame.css'
import {connect} from "react-redux";

class Frame extends React.Component {
    constructor(props){
        super(props);

    }

    render() {
        return (
            <div>
                <h1>BIG data-analyzer</h1>
                <div id="frame">
                    <div id="measurements">
                        {
                            this.props.data.measurements.map((measurement) => (
                                <Measurement key={measurement.id} measurement={measurement}/>
                            ))
                        }
                        <NewMeasurement store={this.props.store}/>
                    </div>

                </div>
            </div>

        );
    }
}

const mapStateToProps = (state, props) => {
    return {
        data: state.data
    }
}

export default connect(mapStateToProps)(Frame);
import React from 'react';
import './Filter.css'
import { Button, ListGroup, Label, Input, ListGroupItem } from 'reactstrap';
import {removeFilter} from "../../actions/data-actions";
import {connect} from "react-redux";

class Filter extends React.Component {
    constructor(props) {
        super(props);
        this.onRemoveFilter = this.onRemoveFilter.bind(this);
    }

    onRemoveFilter(event) {
        this.props.onRemoveFilter(this.props.measurementId, this.props.filter.dimension);
    }

    render() {
        return (
            <div className="measurementFilter">
                <Label>{this.props.filter.dimension}</Label>
                =
                <Label>{this.props.filter.value}</Label>
                <Button onClick={this.onRemoveFilter}>X</Button>
            </div>

        );
    }
}

const mapStateToProps = (state, props) => {
    return {data: state.data};
}

const mapActionsToProps = {
    onRemoveFilter: removeFilter
}

export default connect(mapStateToProps, mapActionsToProps)(Filter);

import React from 'react';
import Drilldown from "../drilldown/Drilldown";
import './Measurement.css'
import Filter from "./Filter";
import { Button, ListGroup, Label, Input, ListGroupItem, Collapse } from 'reactstrap';
import {addFilter, loadData} from "../../actions/data-actions";
import {connect} from "react-redux";

class Measurement extends React.Component {

    constructor(props){
        super(props);
        this.state = { collapse: false, pendingFilters: false };
        this.onAddFilter = this.onAddFilter.bind(this);
        this.onFilterCandidateChanged = this.onFilterCandidateChanged.bind(this);
        this.onFilterCandidateValueChanged = this.onFilterCandidateValueChanged.bind(this);
        this.toggleShowFilter = this.toggleShowFilter.bind(this);
        this.onApplyFilters = this.onApplyFilters.bind(this);
    }

    onAddFilter() {
        this.props.onAddFilter(this.props.measurement.id, this.state.filterCandidate, this.state.filterCandidateValue);
    }

    onFilterCandidateChanged(event) {
        let newState = {};
        Object.assign(newState, this.state);
        newState.filterCandidate = event.target.value;
        this.setState(newState);
    }

    onFilterCandidateValueChanged(event) {
        let newState2 = {};
        Object.assign(newState2, this.state);
        newState2.filterCandidateValue = event.target.value;
        this.setState(newState2);
    }

    toggleShowFilter() {
        let newStateFilter = {};
        Object.assign(newStateFilter, this.state);
        newStateFilter.collapse = !newStateFilter.collapse;
        this.setState(newStateFilter);
    }

    onApplyFilters(){
        let m = this.props.measurement;
        m.upToDate = true;
        var filters = m.filters;
        for(var i = 0; i < this.props.measurement.drilldowns.length; ++i){
            let d = this.props.measurement.drilldowns[i];
            let dataQuery = {};
            filters = filters.concat(d.filters);
            dataQuery.id = d.id;
            dataQuery.datasource = m.datasource;
            dataQuery.kpis = m.kpis;
            dataQuery.dimensions = d.dimensions;
            dataQuery.filters = filters;
            this.props.onLoadMeasurementData(dataQuery);
        }
    }

    render() {
        return (
            <div id="measurement">
                <Button onClick={this.toggleShowFilter}>{!this.state.collapse ? "Show filters" : "Hide filters"}</Button>
                {
                    !this.props.measurement.upToDate ? <Button onClick={this.onApplyFilters}>Apply filters</Button> : ""
                }
                <Collapse isOpen={this.state.collapse}>
                    <ListGroup>
                    {
                        this.props.measurement.filters.map((filter) => (
                            <ListGroupItem><Filter measurementId={this.props.measurement.id} filter={filter}/></ListGroupItem>
                        ))
                    }
                    </ListGroup>
                    {
                        <Input type="select" className="filterDimensionsSelector" onChange={this.onFilterCandidateChanged}>
                            {
                                this.props.measurement.datasourceDimensions.map((dimension) => (
                                    <option value={dimension.name}>{dimension.displayName}</option>
                                ))
                            }
                        </Input>
                    }
                    {
                        <Input type="text" className="filterDimensionValue" onChange={this.onFilterCandidateValueChanged}>
                        </Input>
                    }
                    {
                        <Button id="addFilterButton" onClick={this.onAddFilter}>Add filter</Button>
                    }
                </Collapse>
                {
                    this.props.measurement.drilldowns.map((drilldown) => (
                        <Drilldown key={drilldown.id.measurementId + ":" + drilldown.id.id} drilldown={drilldown}/>
                    ))
                }
            </div>
        );
    }
}

const mapStateToProps = (state, props) => {
    return {data: state.data};
}

const mapActionsToProps = {
    onAddFilter: addFilter,
    onLoadMeasurementData: loadData
}

export default connect(mapStateToProps, mapActionsToProps)(Measurement);
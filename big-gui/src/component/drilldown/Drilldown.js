import React from 'react';
import {Button, Card, CardBody, Collapse, Input} from 'reactstrap';
import './Drilldown.css'
import loading_cat from '../loading-cat.png'
import {GoogleCharts} from "google-charts";
import {changeChartType, changeDrilldownLimit, deleteDrilldown, loadData} from "../../actions/data-actions";
import {connect} from "react-redux";
import {dataQueryFromDrilldown} from "../../util/util";
import {LIMIT_DEFAULT} from "../../config/config";
import {createDrilldown} from "../../actions/metadata-actions";

class Drilldown extends React.Component {
    constructor(props){
        super(props);
        this.state = { collapse: false};
        this.drawChart = this.drawChart.bind(this);
        this.toggleShowFilter = this.toggleShowFilter.bind(this);
        this.onDeleteDrilldown = this.onDeleteDrilldown.bind(this);
        this.onLoadMeasurementData = this.onLoadMeasurementData.bind(this);
        this.onDrilldownLimitChanged = this.onDrilldownLimitChanged.bind(this);
        this.onChartTypeSelected = this.onChartTypeSelected.bind(this);
        this.onDrilldownDimensionSelected = this.onDrilldownDimensionSelected.bind(this);
        this.onChartDimensionSelected = this.onChartDimensionSelected.bind(this);
        this.doDrilldown = this.doDrilldown.bind(this);
    }

    onDeleteDrilldown() {
        this.props.onDeleteDrilldown(this.props.drilldown.id);
    }

    onLoadMeasurementData(){
        this.props.onLoadMeasurementData(dataQueryFromDrilldown(this.props.drilldown));
    }

    onDrilldownLimitChanged(event){
        this.props.onDrilldownLimitChanged(this.props.drilldown.id, event.target.value);
    }

    onChartTypeSelected(event){
        this.props.onChartTypeSelected(this.props.drilldown.id, event.target.value);
    }

    toggleShowFilter() {
        let newStateFilter = {};
        Object.assign(newStateFilter, this.state);
        newStateFilter.collapse = !newStateFilter.collapse;
        this.setState(newStateFilter);
    }

    onDrilldownDimensionSelected(event) {
        let newStateFilter = {};
        Object.assign(newStateFilter, this.state);
        newStateFilter.selectedDrilldownDimensions = this.determineSelected(event);
        this.setState(newStateFilter);
    }

    onChartDimensionSelected(selection) {
        console.info(selection);
        console.info(this.props.drilldown.dataMatrix[selection[0].row][0]);
        let newStateFilter = {};
        Object.assign(newStateFilter, this.state);
        newStateFilter.selectedChartDimension = this.props.drilldown.dataMatrix[selection[0].row][0];
        this.setState(newStateFilter);
    }

    determineSelected(event) {
        var selected = [];
        for (var i = 0; i < event.target.options.length; ++i) {
            if (event.target.options[i].selected) {
                selected.push(event.target.options[i].value);
            }
        }
        return selected;
    }

    doDrilldown() {
        let parent = this.props.drilldown;
        let drilldown = {};
        drilldown.id = {measurementId: parent.measurement.id, id: (parent.id.id + 1)};
        drilldown.measurement = parent.measurement;
        drilldown.limit = parent.limit;
        drilldown.dimensions = [];
        for(var j =0; j < this.state.selectedDrilldownDimensions.length; ++j){
            drilldown.dimensions.push({name: this.state.selectedDrilldownDimensions[j]});
        }
        drilldown.chartType = parent.chartType;
        drilldown.filters = [];
        for(var i = 0; i < parent.dimensions.length; ++i) {
            let dim = parent.dimensions[i];
            let val = this.state.selectedChartDimension.split(",")[i];
            drilldown.filters.push({dimension: dim.name, values:[val], isNegative: false});
        }
        this.props.onCreateDrilldown(drilldown);

        let dataQuery = dataQueryFromDrilldown(drilldown);
        var filters = drilldown.measurement.filters;
        for(var i = 0; i < drilldown.measurement.drilldowns.length; ++i) {
            let d = drilldown.measurement.drilldowns[i];
            filters = filters.concat(d.filters);
        }
        dataQuery.filters = filters;
        this.props.onLoadMeasurementData(dataQuery);
    }

    render() {
        this.drawChart(this);
        return (
            <div>
                <Card>
                <h3>This is a drilldown, id={this.props.drilldown.id.id}</h3>
                    <CardBody>
                        <div id={"drilldown_" + this.props.drilldown.id.measurementId + "_" + this.props.drilldown.id.id} className="drilldown">
                            <img src={loading_cat}/>

                        </div>
                        <Button onClick={this.toggleShowFilter}>{!this.state.collapse ? "Show customization" : "Hide customization"}</Button>
                        <Collapse isOpen={this.state.collapse}>
                            <Input type="select" id="chartTypeSelector"  onChange={this.onChartTypeSelected}>
                                {
                                    ["column", "pie", "timeseries"].map((chartType) => (
                                        <option value={chartType}>{chartType}</option>
                                    ))
                                }
                            </Input>
                            <Input type="number" name="limit" id="limitInput" placeholder={this.props.drilldown.limit} min="1" onChange={this.onDrilldownLimitChanged}/>
                            <Button onClick={this.onLoadMeasurementData}>Update</Button>
                            <Button onClick={this.onDeleteDrilldown}>Delete</Button>
                        </Collapse>
                        <Collapse isOpen={this.state.selectedChartDimension != null}>
                            <Input type="select" className="dimensionsSelector"  onChange={this.onDrilldownDimensionSelected} multiple>
                                {
                                    this.props.drilldown.measurement.datasourceDimensions.map((dimension) => (
                                        <option value={dimension.name}>{dimension.displayName}</option>
                                    ))
                                }
                            </Input>
                            <Button onClick={this.doDrilldown}>Drilldown</Button>
                        </Collapse>
                    </CardBody>
                </Card>
            </div>
        );
    }

    drawChart(context) {
        var drilldown = context.props.drilldown;
        if(drilldown.dataMatrix == null) {
            return;
        }
        var header = drilldown.header;
        var dataMatrix = drilldown.dataMatrix;
        var mix = [];
        mix.push(header);
        for(var i in dataMatrix) {
            mix.push(dataMatrix[i]);
        }
        var data = GoogleCharts.api.visualization.arrayToDataTable(mix);

        var options = {
            title: header.slice(1).join(",") + " by " + header[0]
        };

        var chart = context.createChart(
            document.getElementById(
                "drilldown_" + context.props.drilldown.id.measurementId + "_" + context.props.drilldown.id.id),
            drilldown.chartType);
        drilldown.chart = chart;
        chart.draw(data, options);

        GoogleCharts.api.visualization.events.addListener(chart, 'select', function(){
            context.onChartDimensionSelected(chart.getSelection());
        });
    }

    createChart(div, chartType) {
        if(chartType === "column") {
            return new GoogleCharts.api.visualization.ColumnChart(div);
        }

        if(chartType === "pie") {
            return new GoogleCharts.api.visualization.PieChart(div);
        }

        if(chartType === "timeseries") {
            return new GoogleCharts.api.visualization.LineChart(div);
        }
    }
}

const mapStateToProps = (state, props) => {
    return {data: state.data};
}

const mapActionsToProps = {
    onDeleteDrilldown: deleteDrilldown,
    onLoadMeasurementData: loadData,
    onDrilldownLimitChanged: changeDrilldownLimit,
    onChartTypeSelected: changeChartType,
    onCreateDrilldown: createDrilldown
}

export default connect(mapStateToProps, mapActionsToProps)(Drilldown);
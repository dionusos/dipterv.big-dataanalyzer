import React from 'react';
import {Button, Card, CardBody, Collapse, Input} from 'reactstrap';
import './Drilldown.css'
import loading_cat from '../loading-cat.png'
import {GoogleCharts} from "google-charts";
import {changeChartType, changeDrilldownLimit, deleteDrilldown, loadData} from "../../actions/data-actions";
import {connect} from "react-redux";
import {dataQueryFromDrilldown} from "../../util/util";

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

        GoogleCharts.api.visualization.events.addListener(chart, 'select', function() {
            var selection = drilldown.chart.getSelection();
            if (selection === undefined || selection.length == 0) {

                return;
            }
            var r = drilldown.dataMatrix[selection[0].row];
            // Get the modal
            var modal = document.getElementById('myModal' + drilldown.measurementId);

            // Get the <span> element that closes the modal
            // var span = document.getElementsByClassName("close)[0];

            modal.style.display = "block";

            // When the user clicks on <span> (x), close the modal
            /* span.onclick = function() {
                 modal.style.display = "none";
             }*/

            // When the user clicks anywhere outside of the modal, close it
            window.onclick = function (event) {
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            }

            var fixedDimensionAndValue = document.getElementsByClassName("fixedDimensionAndValue" + drilldown.measurementId)[0];
            fixedDimensionAndValue.innerHTML = header[0] + "=" + r[0];

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
    onChartTypeSelected: changeChartType
}

export default connect(mapStateToProps, mapActionsToProps)(Drilldown);
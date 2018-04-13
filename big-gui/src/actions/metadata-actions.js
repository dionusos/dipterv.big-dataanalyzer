import $ from 'jquery';
import * as model from "../model/Model";

export const UPDATE_DATASOURCES = 'metadata:updateDataSources';
export const UPDATE_KPI_LIST = 'metadata:updateKpiList';
export const UPDATE_DIMENSION_LIST = 'metadata:updateDimensionList';
export const UPDATE_SELECTED_KPIS = 'metadata:updateSelectedKpis';
export const UPDATE_SELECTED_DIMENSIONS = 'metadata:updateSelectedDimensions';
export const UPDATE_SELECTED_DATASOURCE = 'metadata:updateSelectedDatasource';
export const CREATE_MEASUREMENT = 'metadata:createMeasurement';

export function updateDataSources(datasources) {
    return {
        type: UPDATE_DATASOURCES,
        payload: datasources
    }
}

export function updateKpiList(kpis) {
    return {
        type: UPDATE_KPI_LIST,
        payload: kpis
    }
}

export function updateDimensionList(dimensions) {
    return {
        type: UPDATE_DIMENSION_LIST,
        payload: dimensions
    }
}

export function reloadDataSources() {
    return dispatch => {
        $.ajax({
            url: model.backend + "/metadata/datasources",
            success(result) {
                dispatch(updateDataSources(result));
                let datasource = result[0].name;
                dispatch(loadKpisForDataSource(datasource));
                dispatch(loadDimensionsForDataSource(datasource));
                dispatch(updateSelectedDatasource(datasource));
            },
            error() {

            }
        })
    }
}

export function loadKpisForDataSource(dataSource) {
   return dispatch => {
       $.ajax({
           url: model.backend + "/metadata/datasource/" + dataSource + "/kpi/list",
           success(result) {
               dispatch(updateKpiList(result));
           },
           error() {

           }
       })
   }
}

export function loadDimensionsForDataSource(dataSource) {
    return dispatch => {
        $.ajax({
            url: model.backend + "/metadata/datasource/" + dataSource + "/dimension/list",
            success(result) {
                dispatch(updateDimensionList(result));
            },
            error() {

            }
        })
    }
}

export function updateSelectedKpis(kpis) {
    return {
        type: UPDATE_SELECTED_KPIS,
        payload: kpis
    }
}

export function updateSelectedDimensions(dimensions) {
    return {
        type: UPDATE_SELECTED_DIMENSIONS,
        payload: dimensions
    }
}

export function updateSelectedDatasource(datasource) {
    return {
        type: UPDATE_SELECTED_DATASOURCE,
        payload: datasource
    }
}

export function createMeasurement(measurement) {
    return {
        type: CREATE_MEASUREMENT,
        payload: measurement
    }
}
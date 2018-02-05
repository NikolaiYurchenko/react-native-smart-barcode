/*
 * A smart barcode scanner for react-native apps
 * https://github.com/react-native-component/react-native-smart-barcode/
 * Released under the MIT license
 * Copyright (c) 2016 react-native-component <moonsunfall@aliyun.com>
 */


import React, {
    Component
} from 'react';
import {
    View,
    requireNativeComponent,
    NativeModules,
    AppState,
    Text,
    Platform
} from 'react-native';
import PropTypes from 'prop-types';
import { getWidth, getHeight, getDeviceWidth, getDeviceHeight } from '../../src/utils/adaptive';

const BarcodeManager = Platform.OS == 'ios' ? NativeModules.Barcode : NativeModules.CaptureModule


export default class Barcode extends Component {

    static defaultProps = {
        barCodeTypes: Object.values(BarcodeManager.barCodeTypes),
        scannerRectWidth: getWidth(240),
        scannerRectHeight: getHeight(240),
        scannerRectTop: 0,
        scannerRectLeft: 0,
        scannerRectCornerWidth: 55,
        scannerRectCornerLength: 135,
        scannerRectCornerRadius: 60,
        scannerLineInterval: 5000,
        scannerRectCornerColor: `white`,
    }

    static propTypes = {
        ...View.propTypes,
        onBarCodeRead: PropTypes.func.isRequired,
        barCodeTypes: PropTypes.array,
        scannerRectWidth: PropTypes.number,
        scannerRectHeight: PropTypes.number,
        scannerRectTop: PropTypes.number,
        scannerRectLeft: PropTypes.number,
        scannerRectCornerWidth: PropTypes.number,
        scannerRectCornerLength: PropTypes.number,
        scannerRectCornerRadius: PropTypes.number,
        scannerLineInterval: PropTypes.number,
        scannerRectCornerColor: PropTypes.string
    }

    render() {
        return (
            <NativeBarCode
                {...this.props}
            />
        )
    }

    componentDidMount() {
        AppState.addEventListener('change', this._handleAppStateChange);
    }
    componentWillUnmount() {
        AppState.removeEventListener('change', this._handleAppStateChange);
    }

    startFlash() {
        console.log('barcode.staetFlash()');
        BarcodeManager.startFlash()
    }

    stopFlash() {
        console.log('barcode.stopFlash()');
        BarcodeManager.stopFlash()
    }

    startScan() {
        BarcodeManager.startSession()
    }

    stopScan() {
        BarcodeManager.stopSession()
    }

    _handleAppStateChange = (currentAppState) => {
        if(currentAppState !== 'active' ) {
            this.stopScan()
        }
        else {
            this.startScan()
        }
    }
}

const NativeBarCode = requireNativeComponent(Platform.OS == 'ios' ? 'RCTBarcode' : 'CaptureView', Barcode)

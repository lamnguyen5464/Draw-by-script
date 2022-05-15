import { useRef, useLayoutEffect, useState } from 'react';
import { NativeModules } from 'react-native';
import AppNavigator from '@core/navigation/AppNavigator';
import { APP_STACKS_ENUMS } from '@screens/MainAppStack';

const { CoreAPIModule } = NativeModules;

const useMainScreen = props => {
    const { navigation } = props;

    useLayoutEffect(() => {}, []);

    return {
        navigation,
        onPressCreate: () => {
            CoreAPIModule.openCanvas({});
        },
    };
};

export default useMainScreen;

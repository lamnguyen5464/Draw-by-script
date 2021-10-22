import React, { useLayoutEffect } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import {
    CustomizedContainer,
    CustomizedText,
    CustomizedButton,
    AnimatedFadeDown,
} from '@components';
import useLocalization from '@core/localization';
import styles from './AuthHome.styles';
import useAuthHome from './useAuthHome';

const AuthHome = ({ navigation }) => {
    const { LOCALIZATION_ENUMS, LOCALIZED_CONTENT, setLocalization } = useLocalization();

    const { onPressSignIn, onPressSignUp } = useAuthHome({ navigation });

    const _renderMainOverlay = () => {
        return (
            <AnimatedFadeDown style={styles.pos_overlay}>
                <CustomizedContainer type={'white_overlay'}>
                    <CustomizedText type={'title_dark'}>Welcome bla bla bla</CustomizedText>
                    <CustomizedText type={'subtitle_dark'}>
                        Welcome bla aerfa fra ae rfgae fga era er aergae gaer bla bla
                    </CustomizedText>

                    <CustomizedButton
                        type={'primary'}
                        onPress={onPressSignIn}
                        containerStyle={styles.bt_sign_in}
                    >
                        Sign in
                    </CustomizedButton>
                    <CustomizedButton
                        onPress={onPressSignUp}
                        type={'secondary'}
                        containerStyle={styles.bt_sign_up}
                    >
                        Sign up
                    </CustomizedButton>
                </CustomizedContainer>
            </AnimatedFadeDown>
        );
    };

    const _renderForeground = () => (
        <CustomizedContainer
            type={'main_theme'}
            containerStyle={styles.foreground}
        ></CustomizedContainer>
    );

    return (
        <View style={styles.container}>
            {_renderForeground()}
            {_renderMainOverlay()}
        </View>
    );
};

export default AuthHome;
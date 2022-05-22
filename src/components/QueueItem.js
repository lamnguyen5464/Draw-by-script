import React, { memo } from 'react';
import { View, TouchableOpacity, StyleSheet } from 'react-native';
import SharedStyles from '@utils/SharedStyles';
import { DefaultSize, TextSize } from '@utils/Constants';
import { CustomizedText } from '.';
import Colors from '@utils/Colors';
import { Icon } from 'react-native-elements';
import { formatBasicDate } from '@utils/DateUtils';

const QueueItem = ({ data = {}, goToQueueDetail = () => null }) => {
    const { name, theme, address, hostName, status, startDate, endDate } = data;

    const _renderLine = () => <View style={styles.line} />;

    const _renderHeader = () => (
        <CustomizedText type="title_dark" textStyle={styles.title}>
            {name}
        </CustomizedText>
    );
    const _renderIconBox = (icon = '') => (
        <View style={styles.icon_box}>
            <Icon name={icon || ''} type="ionicon" color={Colors.black_12} size={30} />
        </View>
    );
    const _renderContent = (icon = '', content = '') => (
        <View style={styles.container_date}>
            <Icon
                name={icon || ''}
                type="ionicon"
                color={Colors.black_12}
                style={styles.iconDate}
            />
            <CustomizedText type="light_content">{content}</CustomizedText>
        </View>
    );

    const _renderEditButton = () => (
        <TouchableOpacity
            onPress={onPressItem}
            style={[styles.btn, styles.edit_btn]}
            activeOpacity={0.7}
        >
            <CustomizedText type="solid_btn">Edit</CustomizedText>
        </TouchableOpacity>
    );

    const _renderShareButton = () => (
        <TouchableOpacity
            onPress={onPressItem}
            style={[styles.btn, styles.share_btn]}
            activeOpacity={0.7}
        >
            <CustomizedText type="btn">Share</CustomizedText>
        </TouchableOpacity>
    );

    //const _renderTag = () => <View style={styles.tag(theme)} />;

    const onPressItem = () => {
        goToQueueDetail(data);
    };

    return (
        <View style={styles.container}>
            <View style={styles.content}>
                {_renderIconBox('brush-outline')}
                {_renderContent('people-outline', hostName)}
                {/* {_renderLine()}
            {_renderContent('location-outline', address)}
            {_renderContent('calendar-outline', formatBasicDate(startDate))}
            {_renderContent('calendar-outline', formatBasicDate(endDate))} */}
                {/* {_renderTag()} */}
                <View>
                    {_renderEditButton()}
                    {_renderShareButton()}
                </View>
            </View>
            {_renderLine()}
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        ...SharedStyles.shadow,
        flex: 1,

        // borderRadius: DefaultSize.S,
        padding: DefaultSize.M,
        // marginBottom: DefaultSize.S,
        backgroundColor: Colors.white,
    },
    content: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    title: {
        marginTop: DefaultSize.S,
    },
    line: {
        height: 1,
        width: '100%',
        marginTop: DefaultSize.L,
        backgroundColor: Colors.black_07,
    },
    iconDate: {
        marginRight: DefaultSize.S,
    },
    container_date: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    icon_box: {
        height: DefaultSize.XL * 3,
        width: DefaultSize.XL * 3,
        justifyContent: 'center',
        alignItems: 'center',
        borderColor: Colors.primary_1,
        borderWidth: DefaultSize.XXS,
        borderRadius: DefaultSize.M,
    },
    btn: {
        // backgroundColor: Colors.primary_2,
        paddingHorizontal: DefaultSize.S,
        paddingVertical: DefaultSize.XS,
        borderRadius: DefaultSize.S,
        justifyContent: 'center',
        alignItems: 'center',
    },

    edit_btn: {
        color: Colors.white,
        borderColor: Colors.primary_1,
        backgroundColor: Colors.primary_1,
        borderWidth: DefaultSize.XXS,
        borderRadius: DefaultSize.S,
    },

    share_btn: {
        marginTop: DefaultSize.XS,
        color: Colors.white,
        borderColor: Colors.primary_1,
        borderWidth: DefaultSize.XXS,
        borderRadius: DefaultSize.S,
    },
    tag: (theme = Colors.primary_1) => ({
        position: 'absolute',
        backgroundColor: theme,
        top: DefaultSize.XS,
        right: '10%',
        width: '30%',
        height: DefaultSize.S,
        borderRadius: DefaultSize.S,
    }),
});

export default memo(QueueItem);

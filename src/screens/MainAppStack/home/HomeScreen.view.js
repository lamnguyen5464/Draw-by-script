import React, { useRef } from 'react';
import { SafeAreaView, View, StyleSheet, FlatList } from 'react-native';
import useHomeScreen from './useHomeScreen';
import { CustomizedText, CustomizedContainer, CustomizedInput, QueueItem } from '@components';
import { DefaultSize, TextSize } from '@utils/Constants';
import FindQueueScreen from '@screens/MainAppStack/findqueue';
import { Icon } from 'react-native-elements';
import Colors from '@utils/Colors';
import { useHeaderHeight } from '@react-navigation/stack';
import LottieView from 'lottie-react-native';
import assets from '@assets';
import { TouchableOpacity } from 'react-native-gesture-handler';

const HomeScreen = props => {
    const {
        joinedQueue,
        isSearching,
        refInputSearch,
        startSearching,
        stopSearching,
        onPressQr,
        goToQueueDetail,
        goToAuth,
    } = useHomeScreen(props);

    const posYTabSearch = useRef();
    const headerHeight = useRef(useHeaderHeight()).current;

    const _renderSearchComponent = () =>
        isSearching ? (
            <View style={styles.search_view}>
                <FindQueueScreen
                    requestClose={stopSearching}
                    posYTabSearch={posYTabSearch.current}
                    headerHeight={headerHeight}
                />
            </View>
        ) : null;

    const _renderForeground = () => (
        <CustomizedContainer type={'foreground'} containerStyle={styles.foreground} angle={180} />
    );

    const _renderLoading = () => (
        <LottieView
            source={assets.bar_seeking}
            style={styles.loading}
            autoPlay={true}
            loop={true}
        />
    );

    const _renderMain = () => (
        <SafeAreaView>
            <View style={styles.main}>
                {_renderHeader()}
                {_renderContentTitleBar('Enjoy taking notes together')}
                <View style={styles.padding_main}>
                    {_renderSearchBar()}
                    {_renderTitle('My boards', 'recent created boards')}
                    {/* {_renderSearchBar()} */}
                    {joinedQueue?.length ? _renderJoinedList() : _renderLoading()}
                </View>
            </View>
        </SafeAreaView>
    );

    const _renderHeader = () => (
        <View style={styles.header}>
            <TouchableOpacity onPress={goToAuth}>
                <Icon
                    //onPress={onPressQr}
                    size={32}
                    name={'person-outline'}
                    type="ionicon"
                    color={Colors.primary_1}
                    style={styles.profile_icon}
                />
            </TouchableOpacity>
            {/* <CustomizedText type={'header'}>Smart Note</CustomizedText> */}
            <CustomizedText type={'content_header'}>Streak: 2</CustomizedText>
        </View>
    );

    const _renderSearchBar = () => (
        <View
            style={styles.row_search}
            onLayout={event => {
                posYTabSearch.current =
                    event?.nativeEvent?.layout?.y + event?.nativeEvent?.layout?.height;
            }}
        >
            <CustomizedInput
                ref={refInputSearch}
                icon={'search'}
                containerStyle={styles.search_bar}
                onFocus={startSearching}
                placeholder={'Search your note here'}
            />
            <Icon
                onPress={onPressQr}
                name={'qr-code'}
                type="ionicon"
                color={Colors.primary_1}
                style={styles.icon_qr}
            />
        </View>
    );

    const _renderContentTitleBar = title => (
        <CustomizedText type="content_title">{title}</CustomizedText>
    );

    const _renderTitle = (title, subtitle) => (
        <>
            <CustomizedText textStyle={styles.title} type="title_dark">
                {title}
            </CustomizedText>
            <CustomizedText textStyle={styles.subtitle} type="subtitle">
                {subtitle}
            </CustomizedText>
        </>
    );

    const _renderJoinedList = () => (
        <View style={styles.container_list}>
            <FlatList
                keyExtractor={({ _, index }) => `list_joined_at_home_${index}`}
                data={joinedQueue || []}
                renderItem={({ item, index }) => (
                    <QueueItem data={item} goToQueueDetail={goToQueueDetail} />
                )}
                showsVerticalScrollIndicator={false}
            />
        </View>
    );

    return (
        <View style={styles.container}>
            {/* {_renderForeground()} */}
            {_renderMain()}
            {_renderSearchComponent()}
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        backgroundColor: Colors.background_gray,
        flex: 1,
    },
    row_search: {
        marginTop: DefaultSize.XL,
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    foreground: {},
    main: {
        width: '100%',
        height: '100%',
        paddingTop: DefaultSize.L,
    },
    header: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingHorizontal: DefaultSize.XL,
    },
    padding_main: {
        paddingHorizontal: DefaultSize.XL,
    },
    search_bar: {
        width: '85%',
        backgroundColor: Colors.white,
    },
    search_view: {
        position: 'absolute',
        width: '100%',
        height: '100%',
    },
    profile_icon: {
        backgroundColor: Colors.white,
        padding: DefaultSize.XS,
        borderColor: Colors.primary_1,
        borderWidth: DefaultSize.XXS,
        borderRadius: DefaultSize.M,
    },
    container_list: {
        marginTop: DefaultSize.M,
        borderRadius: DefaultSize.M,
        backgroundColor: Colors.white,
        paddingVertical: DefaultSize.S,
    },
    loading: {
        width: '80%',
        height: 'auto',
        alignSelf: 'center',
    },
    title: {
        marginTop: DefaultSize.XL,
        marginLeft: DefaultSize.M,
        // fontSize:
    },
    subtitle: {
        marginLeft: DefaultSize.M,
        color: Colors.black_12,
        // fontSize:
    },
});

export default HomeScreen;

import React from 'react';
import {view, test, Button, StyleSheet} from 'react-native';

const OnBoardingScreen = ({navigation}) => {
    return (
        <View style={StyleSheet.container}>
            <Text>Onboarding Screen</Text>
            <Button title="Cilck Here" onPress={() => navigation.navigate("Login")}
            />
        </View>
    );
};

export default OnBoardingScreen;

const styles = StyleSheet.create({
    constainer: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center'
    },
});
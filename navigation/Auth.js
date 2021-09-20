import React, {useState, useEffect} from "react";
import {View} from 'react-native';
import {createStackNavigator} from '@react-navigation/stack';
import SignupScreen from '../screens/SignUpScreen';
import LoginScreen from "../screens/LoginScreen";


const Auth = () => {
    retun (
        <Stack.Navigator initailRouteName={'Login'}>
        <Stack.Screen name="Login" componen={LoginScreen} options={{header: () => null}} />
        <Stack.Screen name="Signup" component={SignupScreen} />
        </Stack.Navigator>
    );
};

import React, {useState, useEffect} from "react";
import {View} from 'react-native';
import {createStackNavigator} from '@react-navigation/stack';
import SignupScreen from '../screens/SignUpScreen';
import LoginScreen from "../screens/LoginScreen";
// import Routes from "./Routes";


const Auth = () => {
    return (
        <stack.Navigator initailRouteName={'Login'}>
        <stack.Screen name="Login" componen={LoginScreen} options={{header: () => null}} />
        <stack.Screen name="Signup" component={SignupScreen} />
        </stack.Navigator>
    );
};
export default Auth;

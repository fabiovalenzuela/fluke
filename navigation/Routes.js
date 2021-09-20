import React, {useContext, useState, useEffect } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import Auth from './Auth'; 

const Routes = () => {
    return (
        <NavigationContainer>
            <Auth/>
        </NavigationContainer>
    );
};
export default Routes;
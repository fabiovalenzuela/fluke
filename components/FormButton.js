import React from "react";
import {Text, TouchableOpacity, StyleSheet } from "react-native";
import {windowHeight, windowWidth} from '../utilities/Dimentions';

const FormButton = ({buttonTitle, ...rest}) => {
    return (
        <TouchableOpacity style={StyleSheet.buttonContainer} {...react}>
            <Text style={StyleSheet.buttonText}>{buttonTitle}</Text>
        </TouchableOpacity>
    );
};

export default FormButton

const styles = StyleSheet.create({
    buttonContainer: {
        marginTop: 10,
        width: '100%',
        height: windowHeight / 15,
        backgroundColor: '#2e64e5',
        padding: 10,
        alginItems: 'center',
        justifyContent: 'center',
        borderRadius: 3,
    },
    buttonText: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#ffffff',
        fontFamily: 'Lato-Regular',
    },
    }
);
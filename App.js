//import { StatusBar } from 'expo-status-bar';
import React from 'react';
//import { StyleSheet, Text, View } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from'@react-navigation/stack';
import Providers from './navigation';
import LoginScreen from './screens/LoginScreen';

const AppStack =  createStackNavigator();

const App = () => {
  return (
    <NavigationContainer>
      <AppStack.Navigator
      headerMode="none"
      >
      <AppStack.Screen name="LoginScreen" component={LoginScreen} />
      </AppStack.Navigator>
    </NavigationContainer>
    );
  }
  export default App;
// export default function App() {
//   return (
//     <View style={styles.container}>
//       <Text>Open up App.js to start working on your app!</Text>
//       <StatusBar style="auto" />
//     </View>
//   );
// }

// const styles = StyleSheet.create({
//   container: {
//     flex: 1,
//     backgroundColor: '#fff',
//     alignItems: 'center',
//     justifyContent: 'center',
//   },
// });

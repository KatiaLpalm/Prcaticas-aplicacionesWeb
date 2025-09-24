import React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import HomeScreen from "./screens/HomeScreen";
import IMCScreen from "./screens/IMCScreen";
import DivisasScreen from "./screens/DivisasScreen";
import PropinaScreen from "./screens/PropinaScreen";

const Stack = createNativeStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Home">
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="IMC" component={IMCScreen} />
        <Stack.Screen name="Cambio de Divisas" component={DivisasScreen} />
        <Stack.Screen name="Propina" component={PropinaScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

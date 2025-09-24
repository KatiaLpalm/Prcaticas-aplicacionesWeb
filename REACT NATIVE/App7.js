
import React,{Children, useState} from 'react';
import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View } from 'react-native';

  const Texto =() => {
    const[texto,setTexto]=useState('mas vale malo por conocido')
    const actualizaTexto =()=>
      setTexto ('que bueno por conocer')
    return(
      <Text onPress={actualizaTexto}>{texto}</Text>

    )
  }
export default function App() {
  return (
    <View style={styles.container}>
       <text>Hola mundo reac native</text>
       <Texto texto={'cambiado con props'}/>
       <Texto texto={'esta es mi clase favorita'}/>

      <StatusBar style="auto" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

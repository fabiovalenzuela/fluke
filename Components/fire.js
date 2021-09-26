// Import the functions you need from the SDKs you need
import 'firebase/auth';
import 'firebase/app';
import 'firebase/database';
import firebase, { initializeApp } from "firebase/app";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyCw2yy5RLa7QH-BS1ZGxw8YbZi3ea40XC4",
  authDomain: "fluke-b0ecf.firebaseapp.com",
  databaseURL: "https://fluke-b0ecf-default-rtdb.firebaseio.com",
  projectId: "fluke-b0ecf",
  storageBucket: "fluke-b0ecf.appspot.com",
  messagingSenderId: "861347454208",
  appId: "1:861347454208:web:4b0b750c63200586e667e5"
};

// Initialize Firebase
const fire = firebase.initializeApp(firebaseConfig);

export default fire;

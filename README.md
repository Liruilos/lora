# Innovation Lab Project - Team LoRa Lösung

## People Counter Prototype with Visualization using Grafana

This Software application is part of a Proototype designed for the subject Innovation Lab at the Hochscule Heilbronn. 
The prototype counts people (either using a clicker or with sensor data) entering or leaving an event, and this data is then sent to this spring boot server app.
The app creates custom metrics based on which event the data has been sent from, which can then be pulled via Grafana from Prometheous to be displayed on a dashboard.

Teammitglieder: Lauren Walton, Philipp Senfft, Dennis Sommer, Rania Adam

## How to Use

The Spring boot application can be built using the maven 'pack' command, and run either locally or on a virtual machine that is running JVM (using Java 1.8).

For full details on how to run the technical stack, please see the Technical Documentation pdf file located in the same folder as this readme.

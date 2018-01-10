/**
 *  MQTT Ceiling Fan
 *
 *  Copyright 2017 Carlos Ravelo
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
    definition (name: "MQTT Ceiling Fan", namespace: "gandazgul", author: "Carlos Ravelo") {
        capability "Switch Level"
        capability "Switch"
        capability "Button"
        capability "Sensor"
        capability "Temperature Measurement"
        capability "Relative Humidity Measurement"

        command "setStatus"
        command "lowSpeed"
        command "medSpeed"
        command "highSpeed"
        command "fanOff"
        command "fanReset"
    }

    simulator {
        // TODO: define status and reply messages here
    }

    tiles (scale: 2) {
        multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "off", label:'LIGHT', action:"switch.on", icon:"st.Lighting.light24", backgroundColor:"#ffffff", nextState: "activating"
                attributeState "activating", label: 'ACTIVATING', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "activating"
            }

            tileAttribute ("device.level", key: "SLIDER_CONTROL") {
                attributeState "level", action:"switch level.setLevel"
            }
        }

        /*standardTile("switch", "device.switch", width: 4, height: 4, canChangeIcon: true) {
            state "off", label:'LIGHT', action:"switch.on", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "activating", defaultState: true
            state "activating", label: 'ACTIVATING', action:"switch.off", icon:"st.Lighting.light24", backgroundColor:"#2179b8", nextState: "activating"
        }

        controlTile("levelSliderControl", "device.level", "slider", height: 2, width: 2, inactiveLabel: false) {
            state "level", action:"switch level.setLevel"
        }*/

        standardTile("fanOff", "device.button", width: 2, height: 2) {
            state "off", label:'OFF', action: "fanOff", icon:"st.Home.home30", backgroundColor: "#FFFFFF", nextState:"adjusting.off", defaultState: true
            state "adjusting.off", label:'ADJUSTING', action: "fanReset", icon:"st.Home.home30", backgroundColor: "#2179b8", nextState:"adjusting.off"
        }

        standardTile("lowSpeed", "device.button", width: 2, height: 2) {
            state "low", label:'LOW', action: "lowSpeed", icon:"st.Home.home30", backgroundColor: "#FFFFFF", nextState:"adjusting.low", defaultState: true
            state "adjusting.low", label:'ADJUSTING', icon:"st.Home.home30", backgroundColor: "#2179b8", nextState:"adjusting.low"
        }

        standardTile("medSpeed", "device.button", width: 2, height: 2) {
            state "medium", label:'MED', action: "medSpeed", icon:"st.Home.home30", backgroundColor: "#FFFFFF", nextState:"adjusting.med", defaultState: true
            state "adjusting.med", label:'ADJUSTING', action: "medSpeed", icon:"st.Home.home30", backgroundColor: "#2179b8", nextState:"adjusting.med"
        }

        standardTile("highSpeed", "device.button", width: 2, height: 2) {
            state "high", label:'HIGH', action: "highSpeed", icon:"st.Home.home30", backgroundColor: "#FFFFFF", nextState:"adjusting.high", defaultState: true
            state "adjusting.high", label:'ADJUSTING', action: "highSpeed", icon:"st.Home.home30", backgroundColor: "#2179b8", nextState:"adjusting.high"
        }

        valueTile("temperature", "device.temperature", width: 2, height: 2) {
            state("temperature", label:'${currentValue}', unit:"F",
                backgroundColors:[
                    [value: 31, color: "#153591"],
                    [value: 44, color: "#1e9cbb"],
                    [value: 59, color: "#90d2a7"],
                    [value: 74, color: "#44b621"],
                    [value: 84, color: "#f1d801"],
                    [value: 95, color: "#d04e00"],
                    [value: 96, color: "#bc2323"]
                ]
            )
        }

        valueTile("humidity", "device.humidity", width: 2, height: 2) {
            state("humidity", label:'${currentValue}%', unit:"%", backgroundColor: "#1e9cbb", defaultState: true)
        }

        main("switch")

        details(["switch", "lowSpeed", "medSpeed", "highSpeed", "fanOff", "temperature", "humidity"])
    }
}

// parse events into attributes
def parse(String description) {
    log.debug "Parsing '${description}'"
}

// handle commands
def on() {
    sendEvent(name: "switch", value: "activating")
}

def off() {
    sendEvent(name: "switch", value: "off")
}

def setLevel(double val) {
    sendEvent(name: "level", value: val)
}

def setStatus(type, status) {
    log.debug("Setting status ${type}: ${status}")
    // Yes, this is calling the method dynamically
    if (type == "level") {
        setLevel(Float.parseFloat(status));
    }
    else {
        sendEvent(name: type, value: status)
    }
}

def lowSpeed() {
    sendEvent(name: "button", value: "adjusting.low")
}

def medSpeed() {
    sendEvent(name: "button", value: "adjusting.med")
}

def highSpeed() {
    sendEvent(name: "button", value: "adjusting.high")
}

def fanOff() {
    sendEvent(name: "button", value: "adjusting.off")
}

def fanReset() {
    sendEvent(name: "button", value: "off")
}

<template>
  <div id="mapContainer"></div>
</template>

<script>
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import * as mapData from "@/static/data-of-HaNam";

export default {
  data() {
    return {
      map: null,
    };
  },
  mounted() {
    this.map = L.map("mapContainer").setView([20.5362, 105.9851], 11);
    L.tileLayer("http://{s}.tile.osm.org/{z}/{x}/{y}.png", {
      attribution:
        '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
    }).addTo(this.map);
    L.marker([20.5362, 105.9851], []).addTo(this.map);
    // zoom the map to the polyline
    //use a mix of renderers
    // create a red polyline from an array of LatLng points
    var latlngs = [mapData.BoundOfHaNam140172308,
    mapData.BoundOfHaNam483722346,
    mapData.BoundOfHaNam484307782,
    mapData.BoundOfHaNam484338262,
    mapData.BoundOfHaNam483725164,
    mapData.BoundOfHaNam140172310,
    mapData.BoundOfHaNam484366982,
    mapData.BoundOfHaNam978347953,
    mapData.BoundOfHaNam118907946,
    mapData.BoundOfHaNam484399804,
    mapData.BoundOfHaNam483725941,
    mapData.BoundOfHaNam975185412,
    mapData.BoundOfHaNam975185400,
    mapData.BoundOfHaNam975146972,
    mapData.BoundOfHaNam975146971,
    mapData.BoundOfHaNam978332526,
    mapData.BoundOfHaNam484401451,
    mapData.BoundOfHaNam891153330,
    mapData.BoundOfHaNam483220090,
    mapData.BoundOfHaNam483720005,
    mapData.BoundOfHaNam483196192,
    mapData.BoundOfHaNam140210320,
    mapData.BoundOfHaNam483720007,
    mapData.BoundOfHaNam140172307,
    mapData.BoundOfHaNam161496642,
  ];

    var polyline = L.polyline(latlngs, { color: 'red' }).addTo(this.map);

    // zoom the map to the polyline
    this.map.fitBounds(polyline.getBounds());
    var customPane = this.map.createPane("customPane");
    customPane.style.zIndex = 399; // put just behind the standard overlay pane which is at 400
  },
  onBeforeUnmount() {
    if (this.map) {
      this.map.remove();
    }
  },
};
</script>
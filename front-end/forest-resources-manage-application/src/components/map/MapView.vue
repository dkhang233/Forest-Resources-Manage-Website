<template>
  <div id="mapContainer"></div>
</template>

<script>
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import * as mapData from "@/static/data-of-HaNam";
import * as mapApi from "@/api/map"

export default {
  data() {
    return {
      map: null,
    };
  },
  methods: {
    retrieveDataOfRelation(relationId) {
      mapApi.retrieveAllDataOfRelation(relationId).then((res) => {
        try {
          let members = res.data.elements[0].members
          for (let i in members) {
            if (members[i].type == 'way') {
              this.retrieveDataOfWay(members[i].ref)
              console.log(i + " : " + members[i].ref)
            }
          }
        } catch (error) {
          console.log(error)
        }
      })
        .catch((err) => {
          console.log(err)
        })
    },
    retrieveDataOfWay(wayId) {
      mapApi.retrieveAllDataOfWay(wayId)
        .then((res) => {
          try {
            let nodesData = new Map()
            let boundData = []
            let way = null
            let elements = res.data.elements
            for (let i in elements) {
              if (elements[i].type == 'node') {
                if (elements[i].lat != null) {
                  nodesData.set(elements[i].id, [elements[i].lat, elements[i].lon])
                }
              }
              if (elements[i].type == 'way') {
                way = elements[i]
              }
            }
            for (let j in way.nodes) {
              boundData.push(nodesData.get(way.nodes[j]))
            }
            var latlngs = boundData
            var polyline = L.polyline(latlngs, { color: 'red' }).addTo(this.map);
            // zoom the map to the polyline
            // this.map.fitBounds(polyline.getBounds());
          } catch (error) {
            console.log(error)
          }
        })
        .catch((err) => {
          console.log(err)
        })
    }
  },
  created() {
    this.retrieveDataOfRelation(1901010)
  },
  mounted() {
    this.map = L.map("mapContainer").setView([20.5362, 105.9851], 10);
    L.tileLayer("https://api.mapbox.com/styles/v1/dkhang233/clqdt6l9t00e301o9hcqthyz3/tiles/256/{z}/{x}/{y}@2x?access_token=pk.eyJ1IjoiZGtoYW5nMjMzIiwiYSI6ImNscWQ3bzk2djA5ejIya3BuYXM4dm11NWMifQ.EgYHhPNYsqKwlrX_GKOzAw",
      {
        maxZoom: 18,
        attribution:
          '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors ' +
          'and  ©<a href="http://mapbox.com">Mapbox</a>',
      }
    ).addTo(this.map);
    L.marker([20.5362, 105.9851], []).addTo(this.map);
    // var latlngs = [mapData.BoundOfHaNam140172308,
    // mapData.BoundOfHaNam483722346,
    // mapData.BoundOfHaNam484307782,
    // mapData.BoundOfHaNam484338262,
    // mapData.BoundOfHaNam483725164,
    // mapData.BoundOfHaNam140172310,
    // mapData.BoundOfHaNam484366982,
    // mapData.BoundOfHaNam978347953,
    // mapData.BoundOfHaNam118907946,
    // mapData.BoundOfHaNam484399804,
    // mapData.BoundOfHaNam483725941,
    // mapData.BoundOfHaNam975185412,
    // mapData.BoundOfHaNam975185400,
    // mapData.BoundOfHaNam975146972,
    // mapData.BoundOfHaNam975146971,
    // mapData.BoundOfHaNam978332526,
    // mapData.BoundOfHaNam484401451,
    // mapData.BoundOfHaNam891153330,
    // mapData.BoundOfHaNam483220090,
    // mapData.BoundOfHaNam483720005,
    // mapData.BoundOfHaNam483196192,
    // mapData.BoundOfHaNam140210320,
    // mapData.BoundOfHaNam483720007,
    // mapData.BoundOfHaNam140172307,
    // mapData.BoundOfHaNam161496642,
    // ];

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
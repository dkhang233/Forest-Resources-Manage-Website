<template>
    <div ref="mapContainer" class=""></div>
</template>
<script>
import mapboxGl from 'mapbox-gl';
import * as mapApi from "@/api/map"
mapboxGl.accessToken = "pk.eyJ1IjoiZGtoYW5nMjMzIiwiYSI6ImNscWQ3bzk2djA5ejIya3BuYXM4dm11NWMifQ.EgYHhPNYsqKwlrX_GKOzAw"
export default {
    data() {
        return {
            map: null
        };
    },
    methods: {
        retrieveDataOfRelation(relationId, map) {
            mapApi.retrieveAllDataOfRelation(relationId).then((res) => {
                try {
                    let members = res.data.elements[0].members
                    for (let i in members) {
                        if (members[i].type == 'way') {
                            this.retrieveDataOfWay(members[i].ref, map)
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
        retrieveDataOfWay(wayId, map) {
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
                                    nodesData.set(elements[i].id, [elements[i].lon, elements[i].lat])
                                }
                            }
                            if (elements[i].type == 'way') {
                                way = elements[i]
                            }
                        }
                        for (let j in way.nodes) {
                            boundData.push(nodesData.get(way.nodes[j]))
                        }
                        this.drawLine(wayId, boundData, map)
                        // var latlngs = boundData
                        // var polyline = L.polyline(latlngs, { color: 'red' }).addTo(this.map);
                        // zoom the map to the polyline
                        // this.map.fitBounds(polyline.getBounds());
                    } catch (error) {
                        // console.log(error)
                    }
                })
                .catch((err) => {
                    // console.log(err)
                })
        },
        drawLine(wayId, boundData, map) {
            map.addSource('route' + wayId, {
                'type': 'geojson',
                'data': {
                    'type': 'Feature',
                    'properties': {},
                    'geometry': {
                        'type': 'LineString',
                        'coordinates': boundData
                    }
                }
            });

            map.addLayer({
                'id': 'route' + wayId,
                'type': 'line',
                'source': 'route' + wayId,
                'layout': {
                    'line-join': 'round',
                    'line-cap': 'round'
                },
                'paint': {
                    'line-color': '#E74C3C',
                    'line-width': 1
                }
            });
        },
        addImage(map) {
            map.loadImage(
                '',
                (error, image) => {
                    if (error) throw error;

                    // Add the image to the map style.
                    map.addImage('cat', image);

                    // Add a data source containing one point feature.
                    map.addSource('point', {
                        'type': 'geojson',
                        'data': {
                            'type': 'FeatureCollection',
                            'features': [
                                {
                                    'type': 'Feature',
                                    'geometry': {
                                        'type': 'Point',
                                        'coordinates': [105.915517, 20.54472]
                                    }
                                }
                            ]
                        }
                    });

                    // Add a layer to use the image to represent the data.
                    map.addLayer({
                        'id': 'points',
                        'type': 'symbol',
                        'source': 'point', // reference the data source
                        'layout': {
                            'icon-image': 'cat', // reference the image
                            'icon-size': 0.25
                        }
                    });
                }
            );
        }
    },
    created() {

    },
    mounted() {
        try {
            const map = new mapboxGl.Map({
                container: this.$refs.mapContainer,
                style: "mapbox://styles/dkhang233/clqdiuan300cn01qr2fcn28mw", // Replace with your preferred map style
                center: [
                    105.915517,
                    20.54472
                    // -122.483696, 37.833818
                    // 20.3679253, 105.9346393
                ],
                zoom: 9,
            })
            map.addControl(new mapboxGl.NavigationControl())
            this.retrieveDataOfRelation(1901010, map)
            // this.addImage(map)
            this.map = map
            // this.retrieveDataOfRelation(1901010)
            // this.test()
        } catch (error) {
            console.log(error)
        }
    },
    unmounted() {
        if (this.map != null) {
            this.map.remove()
            this.map = null
        }
    }
}
</script>
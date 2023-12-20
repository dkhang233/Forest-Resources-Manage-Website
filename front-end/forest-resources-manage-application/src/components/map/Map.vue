<template>
    <div ref="mapContainer" class="h-[900px]"></div>
</template>
<script>
import mapboxgl from "mapbox-gl"
mapboxgl.accessToken = "pk.eyJ1IjoiZGtoYW5nMjMzIiwiYSI6ImNscWQ3bzk2djA5ejIya3BuYXM4dm11NWMifQ.EgYHhPNYsqKwlrX_GKOzAw"
export default {
    mounted() {
        const map = new mapboxgl.Map({
            container: this.$refs.mapContainer,
            style: "mapbox://styles/dkhang233/clqdiuan300cn01qr2fcn28mw", // Replace with your preferred map style
            center: [
                105.915517,
                20.54472
            ],
            zoom: 9,
        });

        this.map = map;
    }
}
</script>
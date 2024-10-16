<template>
    <div class="grid grid-cols-10" v-loading="loadingStatus">
        <div class="mt-1 col-span-8" ref="mapContainer"></div>
        <div class="ele-info">
            <div>Kinh độ:&nbsp;<span>{{ shorterCoordinates.lng }}</span></div>
            <div>Vĩ độ:&nbsp;<span id="lat">{{ shorterCoordinates.lat }}</span></div>
            <div class="m-2">
                <el-select v-model="form.type" placeholder="Loại cơ sở">
                    <el-option label="Cơ sở lưu trữ động vật" value="animal" />
                    <el-option label="Cơ sở sản xuất gỗ" value="wood" />
                    <el-option label="Cơ sở lưu trữ giống cây" value="seed" />
                </el-select>
            </div>
            <div class="m-2">
                <el-input v-model="form.code" placeholder="Mã cơ sở" />
            </div>
            <button class="m-3 p-2 font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg px-5 bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="addCoordinates">
                Thêm địa điểm
            </button>
        </div>
        <div class="col-span-2">
            <el-scrollbar class="h-[40rem] bg-[#FBFBFB]" always>
                <ul id="listings" class="mx-2 p-2 w-[90%]">
                </ul>
            </el-scrollbar>
            <div class="h-[10%] flex align-center justify-center">
                <button class="p-2 m-3  font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg 
                        px-5 bg-red-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="deleteCoordinates">
                    Xóa địa điểm
                </button>
            </div>
        </div>
    </div>
</template>
<script>
import * as mapApi from "@/api/map"
import * as animalApi from "@/api/animal"
import * as woodApi from "@/api/wood"
import * as seedApi from "@/api/seed"

//import mapbox
import "mapbox-gl/dist/mapbox-gl.css"
import mapboxgl from 'mapbox-gl'
import MapboxGeocoder from '@mapbox/mapbox-gl-geocoder';
import '@mapbox/mapbox-gl-geocoder/dist/mapbox-gl-geocoder.css';
mapboxgl.accessToken = "pk.eyJ1IjoiZGtoYW5nMjMzIiwiYSI6ImNtMWEzeGZzOTFmdnYyanNqdjcyeTZmc3oifQ.yZ46LQ3CDpQtK_iRdzqdlg"
export default {
    data() {
        return {
            map: null,
            loadingStatus: false,
            dialogFormVisible: false,
            form: {
                type: '',
                code: '',
                lat: '',
                lng: ''
            },
            formType: ''
        };
    },
    computed: {
        shorterCoordinates() {
            return {
                lat: Number(this.form.lat).toFixed(5),
                lng: Number(this.form.lng).toFixed(5)
            }
        }
    },
    methods: {
        retrieveAnimalFacilitiesCoordinates(map) {
            animalApi.retrieveAllCoordinates()
                .then((res) => {
                    for (let i in res.data) {
                        let code = res.data[i].code
                        let lng = res.data[i].lng
                        let lat = res.data[i].lat
                        if (lng != '' && lat != '') {
                            let feature = {
                                "type": "Feature",
                                "geometry": {
                                    "type": "Point",
                                    "coordinates": [
                                        lng, lat
                                    ]
                                },
                                "properties": {
                                    'type': 'animal',
                                    'code': code,
                                }
                            }
                            this.buildLocationList(feature)
                            this.addMarkers(feature)
                        }
                    }
                })
                .catch(err => console.log(err))
        },
        retrieveWoodFacilitiesCoordinates(map) {
            woodApi.retrieveAllCoordinates()
                .then((res) => {
                    for (let i in res.data) {
                        let code = res.data[i].code
                        let lng = res.data[i].lng
                        let lat = res.data[i].lat
                        if (lng != '' && lat != '') {
                            let feature = {
                                "type": "Feature",
                                "geometry": {
                                    "type": "Point",
                                    "coordinates": [
                                        lng, lat
                                    ]
                                },
                                "properties": {
                                    'type': 'wood',
                                    'code': code,
                                }
                            }
                            this.buildLocationList(feature)
                            this.addMarkers(feature)
                        }
                    }
                })
                .catch(err => console.log(err))
        },
        retrieveSeedFacilitiesCoordinates(map) {
            seedApi.retrieveAllCoordinates()
                .then((res) => {
                    for (let i in res.data) {
                        let code = res.data[i].code
                        let lng = res.data[i].lng
                        let lat = res.data[i].lat
                        if (lng != '' && lat != '') {
                            let feature = {
                                "type": "Feature",
                                "geometry": {
                                    "type": "Point",
                                    "coordinates": [
                                        lng, lat
                                    ]
                                },
                                "properties": {
                                    'type': 'seed',
                                    'code': code,
                                }
                            }
                            this.buildLocationList(feature)
                            this.addMarkers(feature)
                        }
                    }
                })
                .catch(err => console.log(err))
        },
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
                    } catch (error) {
                        console.log(error)
                    }
                })
                .catch((err) => {
                    console.log(err)
                })
        },
        handleClickOnMap(e, map) {
            this.form.lng = e.lngLat.lng
            this.form.lat = e.lngLat.lat
            if (map.getLayer('points') != null && map.getSource('points') != null) {
                map.removeLayer('points')
                map.removeSource('points')
            }
            map.addSource('points', {
                'type': 'geojson',
                'data': {
                    'type': 'FeatureCollection',
                    'features': [
                        {
                            // feature for Mapbox DC
                            'type': 'Feature',
                            'geometry': {
                                'type': 'Point',
                                'coordinates': [
                                    e.lngLat.lng, e.lngLat.lat
                                ]
                            }
                        }
                    ]
                }
            });
            map.addLayer({
                'id': 'points',
                'type': 'symbol',
                'source': 'points',
                'layout': {
                    'icon-image': 'custom-marker'
                }
            });
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
                    'line-width': 3
                }
            });
        },
        addMarkers(feature) {
            const el = document.createElement('div');
            if (feature.properties.type == 'animal') {
                el.id = `marker-${feature.properties.code}-${feature.properties.type}`;
                el.className = 'animal-marker border-10 rounded-full';
            }
            if (feature.properties.type == 'wood') {
                el.id = `marker-${feature.properties.code}-${feature.properties.type}`;
                el.className = 'wood-marker border-10 rounded-full';
            }
            if (feature.properties.type == 'seed') {
                el.id = `marker-${feature.properties.code}-${feature.properties.type}`;
                el.className = 'seed-marker border-10 rounded-full';
            }
            new mapboxgl.Marker(el, { offset: [0, -23] })
                .setLngLat(feature.geometry.coordinates)
                .addTo(this.map);
            let flyToFacility = this.flyToFacility
            let createPopUp = this.createPopUp
            el.addEventListener('click', (e) => {
                flyToFacility(feature);
                createPopUp(feature);
                const activeItem = document.getElementsByClassName('bg-primary-200');
                e.stopPropagation();
                if (activeItem[0]) {
                    activeItem[0].classList.remove('bg-primary-200');
                }
                const listing = document.getElementById(
                    `listing-${feature.properties.code}-${feature.properties.type}`
                );
                listing.classList.add('bg-primary-200');
            });
        },
        buildLocationList(feature) {
            const listings = document.getElementById('listings');
            const listing = listings.appendChild(document.createElement('li'));
            listing.id = `listing-${feature.properties.code}-${feature.properties.type}`;
            listing.className = 'w-full p-4 hover:cursor-pointer rounded-lg hover:bg-primary-200 hover:text-primary-600';

            const link = listing.appendChild(document.createElement('a'));
            link.className = 'font-bold';
            link.id = `link-${feature.properties.code}-${feature.properties.type}`;
            link.innerHTML = this.convertFacilityName(feature.properties.type);

            const details = listing.appendChild(document.createElement('div'));
            details.className = 'w-full'
            details.innerHTML = `Mã: ${feature.properties.code}`

            let flyToFacility = this.flyToFacility
            let createPopUp = this.createPopUp
            let form = this.form
            listing.addEventListener('click', function () {
                form.code = feature.properties.code
                form.type = feature.properties.type
                form.lng = feature.geometry.coordinates[0]
                form.lat = feature.geometry.coordinates[1]
                flyToFacility(feature)
                createPopUp(feature)
                const activeItem = document.getElementsByClassName('bg-primary-200');
                if (activeItem[0]) {
                    activeItem[0].classList.remove('bg-primary-200')
                }
                listing.classList.add('bg-primary-200')
            });
        },
        flyToFacility(currentFeature) {
            if (currentFeature.geometry.coordinates[0] != '' && currentFeature.geometry.coordinates[1] != '') {
                this.map.flyTo({
                    center: currentFeature.geometry.coordinates,
                    zoom: 15
                });
            } else {
                this.$message({
                    message: 'Không xác định được vị trí',
                    type: 'error'
                })
            }
        },
        createPopUp(currentFeature) {
            const popUps = document.getElementsByClassName('mapboxgl-popup');
            if (popUps[0]) popUps[0].remove();
            let facilityName = this.convertFacilityName(currentFeature.properties.type)
            const popup = new mapboxgl.Popup({ closeOnClick: false })
                .setLngLat(currentFeature.geometry.coordinates)
                .setHTML(`<h3>${facilityName}</h3>
                    <h4>Mã: ${currentFeature.properties.code}</h4>
                    <h4>Vĩ độ: ${currentFeature.geometry.coordinates[1]}</h4>
                    <h4>Kinh độ: ${currentFeature.geometry.coordinates[0]}</h4>
                `)
                .addTo(this.map);
        },
        deleteCoordinates() {
            if (this.form.code == '') {
                this.$message({
                    message: 'Chưa chọn vị trí',
                    type: 'error'
                })
            } else {
                this.$confirm(
                    'Xóa vị trí này. Tiếp tục?',
                    'Xác nhận',
                    {
                        confirmButtonText: 'OK',
                        cancelButtonText: 'Hủy',
                        type: 'warning',

                    }
                )
                    .then(() => {
                        this.loadingStatus = true
                        let type = this.form.type
                        if (type == 'animal') {
                            animalApi.deleteCoordinates(this.form.code)
                                .then((res) => {
                                    const listing = document.getElementById(`listing-${this.form.code}-${this.form.type}`)
                                    listing.remove()
                                    const marker = document.getElementById(`marker-${this.form.code}-${this.form.type}`)
                                    marker.remove()
                                    const popUps = document.getElementsByClassName('mapboxgl-popup');
                                    if (popUps[0]) popUps[0].remove();
                                    this.$notify({
                                        title: 'Thành công',
                                        message: 'Cập nhập thành công',
                                        type: 'success'
                                    })
                                    this.loadingStatus = false
                                })
                                .catch((err) => {
                                    this.loadingStatus = false
                                    let message = ''
                                    try {
                                        message = err.response.data.messages
                                        this.$notify({
                                            title: 'Đã xảy ra lỗi',
                                            message: message,
                                            type: 'error',
                                        })
                                    } catch (error) {

                                    }
                                    console.log(err)
                                })
                        }
                        if (type == 'wood') {
                            woodApi.deleteCoordinates(this.form.code)
                                .then((res) => {
                                    const listing = document.getElementById(`listing-${this.form.code}-${this.form.type}`)
                                    listing.remove()
                                    const marker = document.getElementById(`marker-${this.form.code}-${this.form.type}`)
                                    marker.remove()
                                    const popUps = document.getElementsByClassName('mapboxgl-popup');
                                    if (popUps[0]) popUps[0].remove();
                                    this.$notify({
                                        title: 'Thành công',
                                        message: 'Cập nhập thành công',
                                        type: 'success'
                                    })
                                    this.loadingStatus = false
                                })
                                .catch((err) => {
                                    this.loadingStatus = false
                                    let message = ''
                                    try {
                                        message = err.response.data.messages
                                        this.$notify({
                                            title: 'Đã xảy ra lỗi',
                                            message: message,
                                            type: 'error',
                                        })
                                    } catch (error) {

                                    }
                                    console.log(err)
                                })
                        }
                        if (type == 'seed') {
                            seedApi.deleteCoordinates(this.form.code)
                                .then((res) => {
                                    const listing = document.getElementById(`listing-${this.form.code}-${this.form.type}`)
                                    listing.remove()
                                    const marker = document.getElementById(`marker-${this.form.code}-${this.form.type}`)
                                    marker.remove()
                                    const popUps = document.getElementsByClassName('mapboxgl-popup');
                                    if (popUps[0]) popUps[0].remove();
                                    this.$notify({
                                        title: 'Thành công',
                                        message: 'Cập nhập thành công',
                                        type: 'success'
                                    })
                                    this.loadingStatus = false
                                })
                                .catch((err) => {
                                    this.loadingStatus = false
                                    let message = ''
                                    try {
                                        message = err.response.data.messages
                                        this.$notify({
                                            title: 'Đã xảy ra lỗi',
                                            message: message,
                                            type: 'error',
                                        })
                                    } catch (error) {

                                    }
                                    console.log(err)
                                })
                        }
                    })
                    .catch((err) => {

                    })
            }
        },
        addCoordinates() {
            if (this.form.code == '' || this.form.type == '') {
                this.$message({
                    message: 'Vui lòng nhập đủ thông tin',
                    type: 'error'
                })
            }
            else {
                if (this.form.lat == '' || this.form.lng == '') {
                    this.$message({
                        message: 'Vui lòng chọn tọa độ trên bản đồ',
                        type: 'error'
                    })
                }
                else {
                    this.$confirm(
                        'Thêm vị trí này. Tiếp tục?',
                        'Xác nhận',
                        {
                            confirmButtonText: 'OK',
                            cancelButtonText: 'Hủy',
                            type: 'warning',

                        }
                    )
                        .then(() => {
                            this.loadingStatus = true
                            let type = this.form.type
                            if (type == 'animal') {
                                animalApi.updateCoordinates(this.form)
                                    .then((res) => {
                                        let code = res.data.code
                                        let lng = res.data.lng
                                        let lat = res.data.lat
                                        if (lng != '' && lat != '') {
                                            let feature = {
                                                "type": "Feature",
                                                "geometry": {
                                                    "type": "Point",
                                                    "coordinates": [
                                                        lng, lat
                                                    ]
                                                },
                                                "properties": {
                                                    'type': 'animal',
                                                    'code': code,
                                                }
                                            }
                                            const listing = document.getElementById(`listing-${this.form.code}-${this.form.type}`)
                                            if (listing) listing.remove();
                                            const marker = document.getElementById(`marker-${this.form.code}-${this.form.type}`)
                                            if (marker) marker.remove()
                                            const popUps = document.getElementsByClassName('mapboxgl-popup');
                                            if (popUps[0]) popUps[0].remove();
                                            this.buildLocationList(feature)
                                            this.addMarkers(feature)
                                        }
                                        this.$notify({
                                            title: 'Thành công',
                                            message: 'Cập nhập thành công',
                                            type: 'success'
                                        })
                                        this.loadingStatus = false
                                    })
                                    .catch((err) => {
                                        this.loadingStatus = false
                                        let message = ''
                                        try {
                                            message = err.response.data.messages
                                            this.$notify({
                                                title: 'Đã xảy ra lỗi',
                                                message: message,
                                                type: 'error',
                                            })
                                        } catch (error) {

                                        }
                                        console.log(err)
                                    })
                            }
                            if (type == 'wood') {
                                woodApi.updateCoordinates(this.form)
                                    .then((res) => {
                                        let code = res.data.code
                                        let lng = res.data.lng
                                        let lat = res.data.lat
                                        if (lng != '' && lat != '') {
                                            let feature = {
                                                "type": "Feature",
                                                "geometry": {
                                                    "type": "Point",
                                                    "coordinates": [
                                                        lng, lat
                                                    ]
                                                },
                                                "properties": {
                                                    'type': 'wood',
                                                    'code': code,
                                                }
                                            }
                                            const listing = document.getElementById(`listing-${this.form.code}-${this.form.type}`)
                                            if (listing) listing.remove();
                                            const marker = document.getElementById(`marker-${this.form.code}-${this.form.type}`)
                                            if (marker) marker.remove()
                                            const popUps = document.getElementsByClassName('mapboxgl-popup');
                                            if (popUps[0]) popUps[0].remove();
                                            this.buildLocationList(feature)
                                            this.addMarkers(feature)
                                        }
                                        this.$notify({
                                            title: 'Thành công',
                                            message: 'Cập nhập thành công',
                                            type: 'success'
                                        })
                                        this.loadingStatus = false
                                    })
                                    .catch((err) => {
                                        this.loadingStatus = false
                                        let message = ''
                                        try {
                                            message = err.response.data.messages
                                            this.$notify({
                                                title: 'Đã xảy ra lỗi',
                                                message: message,
                                                type: 'error',
                                            })
                                        } catch (error) {

                                        }
                                        console.log(err)
                                    })
                            }
                            if (type == 'seed') {
                                seedApi.updateCoordinates(this.form)
                                    .then((res) => {
                                        let code = res.data.code
                                        let lng = res.data.lng
                                        let lat = res.data.lat
                                        if (lng != '' && lat != '') {
                                            let feature = {
                                                "type": "Feature",
                                                "geometry": {
                                                    "type": "Point",
                                                    "coordinates": [
                                                        lng, lat
                                                    ]
                                                },
                                                "properties": {
                                                    'type': 'seed',
                                                    'code': code,
                                                }
                                            }
                                            const listing = document.getElementById(`listing-${this.form.code}-${this.form.type}`)
                                            if (listing) listing.remove();
                                            const marker = document.getElementById(`marker-${this.form.code}-${this.form.type}`)
                                            if (marker) marker.remove()
                                            const popUps = document.getElementsByClassName('mapboxgl-popup');
                                            if (popUps[0]) popUps[0].remove();
                                            this.buildLocationList(feature)
                                            this.addMarkers(feature)
                                        }
                                        this.$notify({
                                            title: 'Thành công',
                                            message: 'Cập nhập thành công',
                                            type: 'success'
                                        })
                                        this.loadingStatus = false
                                    })
                                    .catch((err) => {
                                        this.loadingStatus = false
                                        let message = ''
                                        try {
                                            message = err.response.data.messages
                                            this.$notify({
                                                title: 'Đã xảy ra lỗi',
                                                message: message,
                                                type: 'error',
                                            })
                                        } catch (error) {

                                        }
                                        console.log(err)
                                    })
                            }
                        })
                        .catch((err) => {

                        })
                }
            }
        },
        convertFacilityName(type) {
            if (type == 'animal') {
                return 'Cơ sở lưu trữ động vật'
            }
            if (type == 'wood') {
                return 'Cơ sở sản xuất gỗ'
            }
            if (type == 'seed') {
                return 'Cơ sở lưu trữ giống cây trồng'
            }
        }
    },
    created() {

    },
    mounted() {
        try {
            const map = new mapboxgl.Map({
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
            this.map = map
            const geocoder = new MapboxGeocoder({
                accessToken: mapboxgl.accessToken,
                mapboxgl: mapboxgl,
                // marker: false,
                countries: 'VN',
                language: 'vi',
                minLength: 5,
                placeholder: 'Tìm kiếm địa điểm'
            })
            map.on('mousedown', (e) => {
                map.getCanvas().style.cursor = 'move';
            });
            map.on('mouseup', (e) => {
                map.getCanvas().style.cursor = 'pointer';
            });
            map.on('click', (e) => {
                this.handleClickOnMap(e, map)
            });
            map.addControl(geocoder)
            map.addControl(new mapboxgl.NavigationControl())
            map.loadImage('https://docs.mapbox.com/mapbox-gl-js/assets/custom_marker.png',
                (error, image) => {
                    if (error) throw error;
                    map.addImage('custom-marker', image);
                })
            this.retrieveDataOfRelation(1901010, map)
            this.retrieveAnimalFacilitiesCoordinates(map)
            this.retrieveWoodFacilitiesCoordinates(map)
            this.retrieveSeedFacilitiesCoordinates(map)
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

<style>
.animal-marker {
    cursor: pointer;
    height: 30px;
    width: 30px;
    background-image: url('@/assets/image/animal-facility-icon.png');
    background-repeat: no-repeat;
}

.wood-marker {
    cursor: pointer;
    height: 30px;
    width: 30px;
    background-image: url('@/assets/image/wood-facility-icon.png');
    background-repeat: no-repeat;
}

.seed-marker {
    cursor: pointer;
    height: 30px;
    width: 30px;
    background-image: url('@/assets/image/seed-facility-icon.png');
    background-repeat: no-repeat;
}

.mapboxgl-popup {
    padding-bottom: 50px;
}

.mapboxgl-popup-close-button {
    display: none;
}

.mapboxgl-popup-content {
    font:
        400 15px/22px 'Source Sans Pro',
        'Helvetica Neue',
        sans-serif;
    padding: 0;
    width: 15rem;
}

.mapboxgl-popup-content h3 {
    background: #E74C3C;
    color: #fff;
    margin: 0;
    padding: 10px;
    border-radius: 3px 3px 0 0;
    font-weight: 700;
    margin-top: -15px;
}

.mapboxgl-popup-content h4 {
    margin: 0;
    padding: 10px;
    font-weight: 400;
}

.mapboxgl-popup-content div {
    padding: 10px;
}

.mapboxgl-popup-anchor-top>.mapboxgl-popup-content {
    margin-top: 15px;
}

.mapboxgl-popup-anchor-top>.mapboxgl-popup-tip {
    border-bottom-color: #E74C3C;
}

.ele-info {
    position: absolute;
    font-family: sans-serif;
    margin-top: 5px;
    margin-left: 5px;
    padding: 5px;
    width: 25 rem;
    border: 2px solid black;
    font-size: 20px;
    color: #222;
    background-color: #fff;
}

#map {
    position: absolute;
    top: 0;
    bottom: 0;
    width: 100%;
}
</style>
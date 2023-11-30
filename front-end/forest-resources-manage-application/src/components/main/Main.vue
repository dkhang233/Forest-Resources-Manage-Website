<template>
    <div class="h-screen overflow-hidden" style="background: #edf2f7;">
        <custom-header class="top-0"></custom-header>
        <div class="flex">
            <div>
                <side-bar></side-bar>
            </div>
            <div class="w-full">
                <router-view></router-view>
            </div>
        </div>
    </div>
</template>

<script>
import sideBar from '../common/SideBar'
import customHeader from '../common/CustomHeader'
import navigator from '../common/Navigator'
export default {
    components: {
        sideBar,
        customHeader,
        navigator
    },
    data() {
        return {
            username: ''
        }
    },
    methods: {

    }
}
</script>


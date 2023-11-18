<template>
    <div id="index">
        <custom-header class="topbar"></custom-header>
        <section class="container">
            <div class="left_side">
                <side-bar></side-bar>
            </div>
            <div class="main_wrapper">
                <!-- <navigator class="nav"></navigator> -->
                <router-view></router-view>
            </div>
        </section>
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

<style scoped>
#index {
    box-shadow: 1px 0 5px rgba(0, 0, 0, 0.1);
    margin-bottom: 30px;
}

.container {
    display: flex;
    background-color: #fff;
}

.main_wrapper {
    overflow: hidden;
    flex: 1;
    background-color: #fff;
}
</style>


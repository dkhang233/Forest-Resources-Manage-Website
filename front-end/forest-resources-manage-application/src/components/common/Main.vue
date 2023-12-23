<template>
    <div class="h-screen overflow-hidden" style="background: #edf2f7;">
        <custom-header class="top-0"></custom-header>
        <div class="grid grid-cols-7 h-[800px]">
            <div class="col-span-1">
                <side-bar></side-bar>
            </div>
            <div class="h-screen col-span-6 col-start-2  bg-[#FBFBFB]">
                <el-scrollbar class= "h-[89%] bg-[#FBFBFB]" always>
                <router-view></router-view>
            </el-scrollbar>
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


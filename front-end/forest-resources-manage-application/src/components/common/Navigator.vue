<template>
    <div id="nav">
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item class="title">{{ this.appStore.menu[this.appStore.currentIndex].title}}</el-breadcrumb-item>
            <el-breadcrumb-item v-for="(item,index) in this.appStore.menu[this.appStore.currentIndex].content" :key="index">{{item.item }}</el-breadcrumb-item>
        </el-breadcrumb>
    </div>
</template>

<script>
import { mapStores } from 'pinia'
import { useAppStore } from "../../stores/app-store"
export default {
    name:"navigator",
    computed: {
        ...mapStores(useAppStore)
    },
    methods: {
        
    },
    created() {
    },
    beforeDestroy() {
        // this.bus.$off('sendIndex') //销毁
    },
}
</script>

<style scoped>
#nav .el-breadcrumb {
    height: 60px;
    line-height: 60px;
    padding-left: 20px;
}

#nav .el-breadcrumb .title {
    font-weight: bold;
}

#nav{
    position: fixed;
    top: 100px;
    left: 100px;
}
</style>

<template>
    <div id="nav">
        <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item class="title">{{ active.title }}</el-breadcrumb-item>
            <el-breadcrumb-item v-if="active.item != null">{{ active.item }}</el-breadcrumb-item>
        </el-breadcrumb>
    </div>
</template>

<script>
import { mapStores } from 'pinia'
import { useAppStore } from "../../store/store"
export default {
    name:"navigator",
    data() {
        return {
            active: {}
        }
    },
    watch: {
        
    },
    computed: {
        ...mapStores(useAppStore)
    },
    methods: {
        
    },
    created() {
        this.getIndex()
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
</style>

<template>
    <div id="left">
        <el-menu active-text-color="#dd5862" text-color="#fff" :default-active="this.$route.path"
            class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" :collapse="this.appStore.flag"
            background-color="#124280" menu-trigger="click" router>
            <el-menu-item index ='/'>
                <i class="icon"><font-awesome-icon class="iconfont" icon= "fa-house"/></i>
                <template #title>
                    <span class="title">Home</span>
                </template>
            </el-menu-item>
            <el-sub-menu v-for="(item, index) in this.appStore.menu" :index='item.index' :key="index">
                <template #title>
                    <i class="icon"><font-awesome-icon class="iconfont" :icon="item.icon" /></i>
                    <span class="title">{{ item.title }}</span>
                </template>
                <el-menu-item-group v-for="(list, index1) in item.content" :key="index1">
                    <el-menu-item @click="handleTitle(item.index)" :index="list.path" v-if="list.item != null">
                        {{ list.item }}
                    </el-menu-item>
                </el-menu-item-group>
            </el-sub-menu>
        </el-menu>
    </div>
</template>

<script>
import { mapStores } from 'pinia'
import { useAppStore } from "../../stores/app-store"
import { useUserStore } from '@/stores/user-store'

export default {
    name: "sideBar",
    computed: {
        ...mapStores(useAppStore, useUserStore),
    },
    created() {
        this.addData()
    },
    methods: {
        handleOpen(key, keyPath) {
            // console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            // console.log(key, keyPath);
        },
        handleTitle(index) {
            this.appStore.currentIndex = index;
        },
        addData() {
            if (this.userStore.roles == 0) {
                this.appStore.menu.push({
                    index: '5',
                    title: 'Hệ thống',
                    icon: 'fa-gauge-high',
                    content: [{ item: 'Quản lí tài khoản', path: '/account' }, { item: 'Quản lí truy cập', path: '/access' }],
                })
            }
        }
    },
}
</script>

<style>
.el-menu-vertical-demo .el-submenu__title {
    overflow: hidden;
}

.iconfont {
    font-size: 18px;
    color: #fff;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 240px;
    min-height: 100%;
}

#left {
    height: 750px;
    background-color: #124280;
    z-index: 0;
    position: relative;
}

#left .el-menu-vertical-demo .title {
    color: #fff;
    font-size: 16px;
    font-weight: bold;
    margin-left: 14px;
}

.el-submenu {
    border-bottom: 1px solid #eeeeee0f !important;
}

.el-submenu__title:hover {
    background-color: #fff;
}

.el-submenu__title i {
    color: #fbfbfc !important;
}

.el-button {
    margin: 5px 0px 10px 17px;
}

.icon {
    display: inline-block;
    margin-bottom: 41px;
}
</style>

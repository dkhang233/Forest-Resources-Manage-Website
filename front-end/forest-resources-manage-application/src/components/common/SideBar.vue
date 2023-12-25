<template>
    <div class="h-screen bg-[#FBFBFB]">
        <el-scrollbar class="h-[89%] bg-[#FBFBFB]" always>
            <el-menu active-text-color="#dd5862" text-color="#fff" class="side-bar" @open="handleOpen" @close="handleClose"
                :collapse="this.appStore.flag" background-color="#FBFBFB" menu-trigger="click" router>
                <el-menu-item :index="`/main`">
                    <font-awesome-icon icon="fa-house" />
                    <template #title>
                        <span class="text-lg font-[600] ml-[1rem]">Trang chủ</span>
                    </template>
                </el-menu-item>
                <el-menu-item :index="`/main/administration`">
                    <font-awesome-icon icon="fa-city" />
                    <template #title>
                        <span class="text-lg font-[600] ml-[1rem]">Hành chính</span>
                    </template>
                </el-menu-item>
                <el-sub-menu v-for="(item, index) in this.appStore.menu" :index='item.index' :key="index">
                    <template #title>
                        <i class="icon"><font-awesome-icon class="iconfont" :icon="item.icon" style="color: #494965;" /></i>
                        <span class="title">{{ item.title }}</span>
                    </template>
                    <el-menu-item-group v-for="(list, index1) in item.content" :key="index1">
                        <el-menu-item @click="handleTitle(item.index)" :index="list.path" v-if="list.item != null">
                            {{ list.item }}
                        </el-menu-item>
                    </el-menu-item-group>
                </el-sub-menu>
                <el-menu-item :index="`/main/map`">
                    <font-awesome-icon :icon="['fas', 'map-location-dot']" />
                    <template #title>
                        <span class="text-lg font-bold ml-[1rem]">Bản đồ</span>
                    </template>
                </el-menu-item>
                <el-sub-menu :index="''" v-if="userStore.role === 'admin'">
                    <template #title>
                        <i class="icon"><font-awesome-icon class="iconfont" icon="fa-gauge-high"
                                style="color: #494965;" /></i>
                        <span class="title">Hệ thống</span>
                    </template>
                    <el-menu-item-group>
                        <el-menu-item @click="handleTitle(5)" :index="`/main/account`">
                            Quản lí tài khoản
                        </el-menu-item>
                        <el-menu-item @click="handleTitle(5)" :index="`/main/access`">
                            Quản lí truy cập
                        </el-menu-item>
                    </el-menu-item-group>
                </el-sub-menu>
            </el-menu>
        </el-scrollbar>
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
    },
}
</script>

<style>
.iconfont {
    font-size: 18px;
    color: #fff;
}

/* 
#left {
    min-height: 697px;
    background-color:#FBFBFB;
    z-index: 5;
    position: relative;
}*/

.title {
    color: #2C3E50;
    font-size: 16px;
    font-weight: bold;
    margin-left: 14px;
}

.el-submenu {
    border-bottom: 1px solid #eeeeee0f !important;
}

.el-submenu__title:hover {
    background-color: #2C3E50;
}

.el-submenu__title {
    color: #2C3E50;
}

.el-menu-item {
    color: #2C3E50;
}

.el-sub-menu .el-icon {
    color: #2C3E50;
}

.el-button {
    margin: 5px 0px 10px 17px;
}

.icon {
    display: inline-block;
    margin-bottom: 41px;
}

.el-scrollbar__thumb {
    background-color: #2C3E50;
    width: 10px;
}

.el-scrollbar__bar.is-vertical {
    width: 10px;
}
</style>

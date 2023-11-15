<template>
    <div id="left">
        <el-menu active-text-color="#dd5862"
                 text-color="#fff" 
                 :default-active="this.$route.path"
                 class="el-menu-vertical-demo" 
                 @open="handleOpen" 
                 @close="handleClose" 
                 :collapse="this.appStore.flag" 
                 background-color="#124280"
                 menu-trigger="click"
                 router
        >
            <el-sub-menu v-for="(item, index) in this.appStore.menu" :index='item.index' :key="index">
                <template #title>
                    <i class="icon"><font-awesome-icon class="iconfont" :icon="item.icon" /></i>
                    <span class="title">{{ item.title }}</span>
                </template>
                <el-menu-item-group v-for="(list,index1) in item.content" :key="index1">
                    <el-menu-item @click="handleTitle(item.index)" 
                                  :index="list.path"
                                  v-if="list.item != null"
                    >
                        {{ list.item }}
                    </el-menu-item>
                </el-menu-item-group>
            </el-sub-menu>
        </el-menu>
    </div>
</template>

<script>
import { mapStores } from 'pinia'
import {useAppStore} from "../../store/store"

export default {
    name: "sideBar",
    data() {
       
    },
    computed: {
        ...mapStores(useAppStore)
    },
    created() {
        // this.addData()
    },
    methods: {
        handleOpen(key, keyPath) {
            // console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            // console.log(key, keyPath);
        },
        //点击标题传递参数给navigator组件
        handleTitle(index) {
            this.appStore.currentIndex = index;
            // this.bus.$emit('sendIndex', index)
        },
        addData() {
            let role = this.$cookies.get("role")
            if (role == 0) {
                this.menu.push({
                    index: '5',
                    title: '教师管理',
                    icon: 'icon-Userselect',
                    content: [{ item1: '教师管理', path: '/teacherManage' }, { item2: '添加教师', path: '/addTeacher' }],
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
    height: 100%;
    background-color: #124280;
    z-index: 0;
    position: fixed;
    top: 80px;
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

.el-button{
    margin: 5px 0px 10px 17px;
}

.icon{
    display: inline-block; /* Đảm bảo icon hiển thị trên cùng một dòng */
    margin-bottom: 41px;
}

</style>

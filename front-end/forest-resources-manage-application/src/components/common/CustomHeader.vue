<template>
    <!-- Main navigation container -->
    <nav
        class="flex-no-wrap relative flex w-full items-center justify-between bg-[#FBFBFB] py-2 shadow-md shadow-black/5 dark:bg-neutral-600 dark:shadow-black/10 lg:flex-wrap lg:justify-start lg:py-4">
        <div class="flex w-full flex-wrap items-center justify-between px-3">
            <!-- Hamburger button for mobile view -->
            <button class="block border-0 bg-transparent px-2 
                text-[#2C3E50] hover:no-underline hover:shadow-none 
                focus:no-underline focus:shadow-none focus:outline-none 
                focus:ring-0 dark:text-neutral-200 lg:hidden" @click="changeHeaderView">
                <!-- Hamburger icon -->
                <span class="[&>svg]:w-7">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="h-7 w-7">
                        <path fill-rule="evenodd"
                            d="M3 6.75A.75.75 0 013.75 6h16.5a.75.75 0 010 1.5H3.75A.75.75 0 013 6.75zM3 12a.75.75 0 01.75-.75h16.5a.75.75 0 010 1.5H3.75A.75.75 0 013 12zm0 5.25a.75.75 0 01.75-.75h16.5a.75.75 0 010 1.5H3.75a.75.75 0 01-.75-.75z"
                            clip-rule="evenodd" />
                    </svg>
                </span>
            </button>

            <!-- Collapsible navigation container -->
            <div class="!visible flex-grow basis-[100%] items-center lg:!flex lg:basis-auto" v-show="headerView">
                <!-- Logo -->
                <a class="mb-4 ml-2 mr-5 mt-3 flex items-center text-neutral-900 hover:text-neutral-900 focus:text-neutral-900 dark:text-neutral-200 dark:hover:text-neutral-400 dark:focus:text-neutral-400 lg:mb-0 lg:mt-0"
                    >
                    <img src="@/assets/logo/logo.png" class="w-[60px] h-[55px]" alt="TE Logo" loading="lazy" />
                </a>
                <!-- Left navigation links -->
                <ul class="list-style-none mr-auto flex flex-col pl-0 lg:flex-row" data-te-navbar-nav-ref>
                    <!-- Team link -->
                    <li class="mb-4 lg:mb-0 lg:pr-2" data-te-nav-item-ref>
                        <a class="text-[#2C3E50] transition duration-200 hover:text-neutral-400 hover:ease-in-out focus:text-neutral-700 disabled:text-black/30 motion-reduce:transition-none dark:text-neutral-200 dark:hover:text-neutral-300 dark:focus:text-neutral-300 lg:px-2 [&.active]:text-black/90 dark:[&.active]:text-neutral-400"
                            data-te-nav-link-ref>Team</a>
                    </li>
                    <!-- Projects link -->
                    <li class="mb-4 lg:mb-0 lg:pr-2" data-te-nav-item-ref>
                        <a class="text-[#2C3E50] transition duration-200 hover:text-neutral-400 hover:ease-in-out focus:text-neutral-700 disabled:text-black/30 motion-reduce:transition-none dark:text-neutral-200 dark:hover:text-neutral-300 dark:focus:text-neutral-300 lg:px-2 [&.active]:text-black/90 dark:[&.active]:text-neutral-400"
                            data-te-nav-link-ref>Projects</a>
                    </li>
                </ul>
            </div>

            <!-- Right elements -->
            <div class="relative flex items-center">

                <!-- Container with two dropdown menus -->
                <div class="relative" data-te-dropdown-ref data-te-dropdown-alignment="end">
                    <!-- First dropdown trigger -->
                    <a class="hidden-arrow mr-4 flex items-center text-neutral-600 transition duration-200 hover:text-neutral-700 hover:ease-in-out focus:text-neutral-700 disabled:text-black/30 motion-reduce:transition-none dark:text-neutral-200 dark:hover:text-neutral-300 dark:focus:text-neutral-300 [&.active]:text-black/90 dark:[&.active]:text-neutral-400"
                         id="dropdownMenuButton1" role="button" data-te-dropdown-toggle-ref aria-expanded="false">
                        <!-- Dropdown trigger icon -->
                        <span class="[&>svg]:w-5">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="h-5 w-5">
                                <path fill-rule="evenodd"
                                    d="M5.25 9a6.75 6.75 0 0113.5 0v.75c0 2.123.8 4.057 2.118 5.52a.75.75 0 01-.297 1.206c-1.544.57-3.16.99-4.831 1.243a3.75 3.75 0 11-7.48 0 24.585 24.585 0 01-4.831-1.244.75.75 0 01-.298-1.205A8.217 8.217 0 005.25 9.75V9zm4.502 8.9a2.25 2.25 0 104.496 0 25.057 25.057 0 01-4.496 0z"
                                    clip-rule="evenodd" />
                            </svg>
                        </span>
                        <!-- Notification counter -->
                        <span
                            class="absolute -mt-4 ml-2.5 rounded-full bg-danger px-[0.35em] py-[0.15em] text-[0.6rem] font-bold leading-none text-white">1</span>
                    </a>
                </div>

                <!-- Second dropdown container -->
                <div class="relative" data-te-dropdown-ref data-te-dropdown-alignment="end">
                    <!-- Second dropdown trigger -->
                    <span
                        class="hidden-arrow flex items-center whitespace-nowrap transition duration-150 ease-in-out motion-reduce:transition-none"
                        @click="showSetting" id="dropdownMenuButton2" role="button" data-te-dropdown-toggle-ref
                        aria-expanded="false">
                        <!-- User avatar -->
                        <img :src="avatar" class="rounded-full" style="height: 50px; width: 50px" alt="" loading="lazy"
                            v-if="avatar != ''" />
                        <img src="@/assets/image/default-avatar.png" class="rounded-full" style="height: 50px; width: 50px"
                            alt="" loading="lazy" v-if="avatar == ''" />
                    </span>
                    <!-- Second dropdown menu -->
                    <ul class="absolute z-[1000] right-1  min-w-max list-none overflow-hidden rounded-lg border-none bg-white bg-clip-padding text-left text-base shadow-lg dark:bg-neutral-700 [&[data-te-dropdown-show]]:block"
                        v-show="loginFlag">
                        <!-- Second dropdown menu items -->
                        <li>
                            <a class="block w-full whitespace-nowrap bg-transparent px-4 py-2 text-sm font-normal text-neutral-700 hover:bg-neutral-100 active:text-neutral-800 active:no-underline disabled:pointer-events-none disabled:bg-transparent disabled:text-neutral-400 dark:text-neutral-200 dark:hover:bg-white/30"
                                href="#/main/profile">Thông tin tài khoản</a>
                        </li>
                        <li>
                            <a class="block w-full whitespace-nowrap bg-transparent px-4 py-2 text-sm font-normal text-neutral-700 hover:bg-neutral-100 active:text-neutral-800 active:no-underline disabled:pointer-events-none disabled:bg-transparent disabled:text-neutral-400 dark:text-neutral-200 dark:hover:bg-white/30"
                                href="#/main/change-pass">Đổi mật khẩu</a>
                        </li>
                        <li>
                            <a class="block w-full whitespace-nowrap bg-transparent px-4 py-2 text-sm font-normal text-neutral-700 hover:bg-neutral-100 hover:cursor-pointer active:text-neutral-800 active:no-underline disabled:pointer-events-none disabled:bg-transparent disabled:text-neutral-400 dark:text-neutral-200 dark:hover:bg-white/30"
                                @click="logout">Đăng xuất</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
</template>

<script>
import { mapStores } from 'pinia'
import { useAppStore } from "../../stores/app-store"
import { useUserStore } from '@/stores/user-store'
import * as userApi from '@/api/user'

export default {
    name: "customHeader",
    data() {
        return {
            loginFlag: false,
            headerView: false,
        }
    },
    created() {
        this.getUserInfo()
    },
    computed: {
        ...mapStores(useAppStore, useUserStore),
        avatar() {
            let avatar = this.userStore.avatar
            if (avatar == '') {
                return ''
            } else {
                return userApi.IMAGE_URL + avatar
            }
        }
    },
    methods: {
        //Hiển thị cài đặt tài khoản khi người dùng ấn vào avatar
        showSetting() {
            this.loginFlag = !this.loginFlag
        },
        changeHeaderView() {
            this.headerView = !this.headerView
        },
        getUserInfo() {
            this.userStore.getInfor()
        },
        logout() {
            $cookies.remove("username")
            $cookies.remove("role")
            this.$router.push({ path: "/" })
        }
    },
    created() {
        this.getUserInfo()
    }
}
</script>


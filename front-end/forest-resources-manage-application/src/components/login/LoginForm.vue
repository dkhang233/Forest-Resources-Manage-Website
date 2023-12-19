<template>
    <h2 class=" mb-5 text-4xl font-bold">Xin chào</h2>
    <p class="max-w-sm mb-12 font-sans font-light text-black">Vui lòng đăng nhập để sử dụng hệ thống</p>
    <el-form :model="form">
        <el-form-item>
            <input type="text"
                class="w-full p-6 mb-3 border border-gray-300 rounded-md placeholder:font-sans placeholder:font-light"
                placeholder="Tên người dùng hoặc email" v-model="form.username" />
        </el-form-item>
        <el-form-item>
            <input type="password"
                class="w-full p-6 border border-gray-300 rounded-md placeholder:font-sans placeholder:font-light"
                placeholder="Mật khẩu" v-model="form.password" />
        </el-form-item>
    </el-form>
    <!-- Middle Content -->
    <div class="flex flex-col items-center justify-between mt-6 space-y-6 md:flex-row md:space-y-0">
        <a class="mr-3 font-light text-black hover:underline hover:text-blue-500" href="#/forget-pass">Quên mật khẩu</a>
        <button class="w-full md:w-auto flex justify-center 
                        items-center p-6 space-x-4 font-sans font-bold
                        text-white rounded-md shadow-lg 
                        px-9 bg-blue-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="login">
            <span>Tiếp tục</span>
            <svg xmlns="http://www.w3.org/2000/svg" class="w-7" viewBox="0 0 24 24" stroke-width="1.5" stroke="#ffffff"
                fill="none" stroke-linecap="round" stroke-linejoin="round">
                <path stroke="none" d="M0 0h24v24H0z" fill="none" />
                <line x1="5" y1="12" x2="19" y2="12" />
                <line x1="13" y1="18" x2="19" y2="12" />
                <line x1="13" y1="6" x2="19" y2="12" />
            </svg>
        </button>
    </div>

    <!-- <div class="ml-[80px] mt-[50px] ">
        <a class="font-light text-black hover:underline hover:text-blue-500" href="#/register">Đăng ký tài khoản</a>
    </div> -->
</template>

<script>
import { mapStores } from 'pinia';
import { useUserStore } from '@/stores/user-store'
import * as userApi from '@/api/user'

export default {
    emits: ['onLoadingStatus', 'offLoadingStatus'],
    data() {
        return {
            form: {
                username: "",
                password: ""
            }
        }
    },
    computed: {
        ...mapStores(useUserStore)
    },
    methods: {
        login() {
            this.$emit('onLoadingStatus')
            userApi.login(this.form)
                .then((res) => {
                    this.$emit('offLoadingStatus')
                    $cookies.set('username', res.data)
                    this.$router.push({ path: '/main' })
                })
                .catch((err) => {
                    this.$emit('offLoadingStatus')
                    this.$message({
                        message: 'Username hoặc mật khẩu không chính xác',
                        type: 'error',
                    })
                    console.log(err)
                })
        },
    },
}
</script>
<template>
    <h2 class="font-mono mb-5 text-4xl font-bold">Quên mật khẩu</h2>
    <p class="max-w-sm mb-12 font-sans font-light text-gray-600">Vui lòng nhập email để nhận mã xác nhận</p>
    <el-form :model="form">
        <el-form-item>
            <input type="text"
                class="w-full p-6 mb-3 border border-gray-300 rounded-md placeholder:font-sans placeholder:font-light"
                placeholder="Email của bạn" v-model="form.email" />
        </el-form-item>
    </el-form>
    <!-- Middle Content -->
    <div class="flex flex-col justify-end mt-6 space-y-6 md:flex-row md:space-y-0">
        <button class="md:w-auto flex 
                        items-center p-6 space-x-4 font-sans font-bold
                        text-white rounded-md shadow-lg 
                        px-9 bg-blue-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="send">
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
</template>

<script>
import * as userApi from "@/api/user"
export default {
    data() {
        return {
            form: {
                email: ""
            }
        }
    },
    methods: {
        send() {
            if (this.checkEmail(this.form.email)) {
                this.$emit('onLoadingStatus')
                userApi.resetPassword(this.form)
                    .then((res) => {
                        this.$emit('offLoadingStatus')
                        $cookies.set('email',this.form.email)
                        this.$router.push({ path: "/authenticate-code" })
                    })
                    .catch((err) => {
                        this.$emit('offLoadingStatus')
                        let message = ""
                        try {
                            message = err.response.data.messages
                        } catch (error) {
                            console.log(error)
                        }
                        this.$message({
                            message: message,
                            type: 'error'
                        })
                    })
            } else {
                this.$message({
                    message: "Địa chỉ email không chính xác",
                    type: 'error'
                })
            }

        },
        checkEmail(email) {
            return /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email)
        }
    }
}
</script>

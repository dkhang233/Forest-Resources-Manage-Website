<template>
    <h2 class="font-mono mb-5 text-4xl font-bold">Mật khẩu mới</h2>
    <p class="max-w-sm mb-12 font-sans font-light text-gray-600">Vui lòng nhập mật khẩu mới cho tài khoản của bạn</p>
    <el-form :model="form">
        <el-form-item>
            <input type="password"
                class="w-full p-6 mb-3 border border-gray-300 rounded-md placeholder:font-sans placeholder:font-light"
                placeholder="Mật khẩu mới" v-model="form.password" />
        </el-form-item>
    </el-form>
    <el-form :model="form">
        <el-form-item>
            <input type="password"
                class="w-full p-6 mb-3 border border-gray-300 rounded-md placeholder:font-sans placeholder:font-light"
                placeholder="Nhập lại mật khẩu" v-model="form.retypePassword" />
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
            checkFormStatus: false,
            form: {
                password: "",
                retypePassword: ""
            }
        }
    },
    methods: {
        checkForm() {
            if (this.form.password != "") {
                if (this.form.password == this.form.retypePassword) {
                    this.checkFormStatus = true
                }
            }
            else {
                this.$message({
                    message: "Mật khẩu nhập lại không trùng khớp",
                    type: "error"
                })
            }
        },
        send() {
            this.checkForm()
            if (this.checkFormStatus) {
                let newPassword = {
                    newPassword: this.form.password,
                    otp: $cookies.get('otp')
                }
                userApi.changePassword(newPassword)
                    .then((res) => {
                        this.$router.push({ path: '/' })
                        $cookies.remove('otp')
                    })
                    .catch((err) => {
                        let message = ""
                        try {
                            message = err.response.data.messages
                        } catch (error) {
                            console.log(error)
                        }
                        this.$message({
                            message: message,
                            type: 'error',
                            title: "Đã xảy ra lỗi"
                        })
                    })
            }
        }
    }
}
</script>

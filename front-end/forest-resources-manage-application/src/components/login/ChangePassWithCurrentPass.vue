<template>
    <div class="p-10 grid grid-cols-12" v-loading="loadingStatus">
        <h2 class="col-start-5 col-span-5 font-mono ml-[50px] mb-5  text-4xl font-bold">Đổi mật khẩu</h2>
        <el-form class="col-start-4 col-span-5" :model="form">
            <el-form-item>
                <input type="password"
                    class="w-full p-6 mb-3 border border-gray-300 rounded-md placeholder:font-sans placeholder:font-light"
                    placeholder="Mật khẩu hiện tại" v-model="form.password" />
            </el-form-item>
            <el-form-item>
                <input type="password"
                    class="w-full p-6 mb-3 border border-gray-300 rounded-md placeholder:font-sans placeholder:font-light"
                    placeholder="Mật khẩu mới" v-model="form.newPassword" />
            </el-form-item>
            <el-form-item>
                <input type="password"
                    class="w-full p-6 mb-3 border border-gray-300 rounded-md placeholder:font-sans placeholder:font-light"
                    placeholder="Nhập lại mật khẩu" v-model="form.retypePassword" />
            </el-form-item>
        </el-form>

        <!-- Middle Content -->
        <div class="col-start-7 col-span-2 mt-6 space-y-6">
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
    </div>
</template>

<script>
import * as userApi from "@/api/user"
export default {
    data() {
        return {
            loadingStatus: false,
            checkFormStatus: false,
            form: {
                password: '',
                newPassword: '',
                retypePassword: ''
            }
        }
    },
    methods: {
        checkForm() {
            if (/^\s*$/.test(this.form.newPassword) || /^\s*$/.test(this.form.password) || /^\s*$/.test(this.form.retypePassword)) {
                this.$message({
                    message: "Vui lòng nhập đủ thông tin",
                    type: "error"
                })
            }
            else {
                if (this.form.newPassword == this.form.retypePassword) {
                    this.checkFormStatus = true
                } else {
                    this.$message({
                        message: "Mật khẩu nhập lại không trừng khớp",
                        type: "error"
                    })
                }
            }
        },
        send() {
            this.checkForm()
            if (this.checkFormStatus) {
                this.loadingStatus = true
                let newPassword = {
                    otp: '',
                    password: this.form.password,
                    newPassword: this.form.newPassword,
                }
                userApi.changePasswordWithCurrentPass($cookies.get('username'), newPassword)
                    .then((res) => {
                        this.loadingStatus = false
                        this.$notify({
                            message: "Thay đổi mật khẩu thành công",
                            type: 'success',
                            title: "Thành công"
                        })
                    })
                    .catch((err) => {
                        this.loadingStatus = false
                        let message = ""
                        try {
                            message = err.response.data.messages
                        } catch (error) {
                            console.log(error)
                        }
                        this.$notify({
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

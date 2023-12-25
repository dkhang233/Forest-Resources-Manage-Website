<template>
    <el-row>
        <el-col :offset="2">
            <h1 class=" text-[#2C3E50] text-[25px] font-bold m-3">
                <font-awesome-icon class="margin-right: 10px" :icon="['fas', 'users-gear']" size="lg" />
                Quản lí tài khoản người dùng
            </h1>
        </el-col>
    </el-row>
    <el-row v-loading="loadingStatus">
        <el-col :span="20" :offset="2">
            <el-card class="h-[530px] rounded-[50px] mb-2" shadow="always">
                <el-table :data="filterTableData" class="h-[530px] w-[93rem]" fit>
                    <el-table-column label="Họ" prop="firstName" align="center" ></el-table-column>
                    <el-table-column label="Tên" prop="lastName" align="center" ></el-table-column>
                    <el-table-column label="Username" prop="username" align="center" ></el-table-column>
                    <el-table-column label="Vai trò" prop="username" align="center" ></el-table-column>
                    <el-table-column label="Trực thuộc (mã)" prop="administrationCode" align="center" ></el-table-column>
                    <el-table-column :min-width="120" align="center">
                        <template #header>
                            <el-input v-model="search" size="large" placeholder="Tìm kiếm theo username" />
                        </template>
                        <template #default="scope">
                            <el-switch v-model="scope.row.isActive" @change="changeUserStatus(index, scope.row)"
                                :loading="scope.row.loading"
                                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" />
                            <el-button @click="handleClickEdit(scope.$index, scope.row)">Chi tiết</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-card>
            <el-dialog class=" block rounded-lg
                    bg-white p-6 
                    shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)]
                    dark:bg-neutral-700" top="2vh" v-model="dialogFormVisible" :title="formTitle"
                :before-close="handleCancel">
                <el-form class="grid grid-cols-10" ref="ruleFormRef" :model="form" status-icon :rules="rules" size="default"
                    label-position="top">
                    <div class="col-span-3">
                        <el-form-item class="" prop="avatar">
                            <input ref="uploadInput" @change="handleFileChange" type="file" v-show="false" />
                            <img @click="openFileInput" class="rounded-full shadow-lg hover:cursor-pointer hover:opacity-60"
                                src="@/assets/image/default-avatar.png" v-if="form.avatar == ''" />
                            <img @click="openFileInput" class="rounded-full shadow-lg " :src="userAvatar"
                                v-if="form.avatar != ''" />
                            <font-awesome-icon
                                class="shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)] p-2 hover:cursor-pointer hover:opacity-60 hover:text-red-600"
                                v-if="form.avatar != ''" @click="resetAvatar" :icon="['fas', 'trash-can']" size="lg" />
                        </el-form-item>
                    </div>
                    <div class="col-start-5 col-span-10">
                        <el-form-item label="Username" prop="username">
                            <el-input v-model="form.username" :disabled="formType == 'update'" />
                        </el-form-item>
                        <el-form-item label="Mật khẩu" prop="password" v-if="formType == 'create'">
                            <el-input type="password" show-password v-model="form.password" />
                        </el-form-item>
                        <div class="grid grid-cols-2 gap-5">
                            <el-form-item label="Họ" prop="firstName">
                                <el-input v-model="form.firstName" />
                            </el-form-item>
                            <el-form-item label="Tên" prop="lastName">
                                <el-input v-model="form.lastName" />
                            </el-form-item>
                        </div>
                        <el-form-item label="Email" prop="email">
                            <el-input v-model="form.email" />
                        </el-form-item>
                        <div class="grid grid-cols-2 gap-5">
                            <el-form-item label="Ngày sinh" prop="birthDate">
                                <el-date-picker v-model="form.birthDate" type="date" placeholder="Chọn ngày sinh"
                                    size="default" />
                            </el-form-item>
                            <el-form-item label="Địa chỉ" prop="address">
                                <el-input v-model="form.address" />
                            </el-form-item>
                        </div>
                        <div class="grid grid-cols-2 gap-5">
                            <el-form-item label="Vai trò" prop="role">
                                <el-select v-model="form.role" placeholder="Chọn vai trò">
                                    <el-option label="User" value="user" />
                                    <el-option label="Admin" value="admin" />
                                </el-select>
                            </el-form-item>
                            <el-form-item label="Trực thuộc (mã)" prop="administrationCode">
                                <el-input v-model="form.administrationCode" />
                            </el-form-item>
                        </div>
                    </div>
                </el-form>
                <template #footer>
                    <span class="grid grid-cols-16 gap-4">
                        <!-- <button class="p-2 mr-3  font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg 
                        px-5 bg-red-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="dialogFormVisible = false"
                            v-if="formType == 'update'">
                            Xóa
                        </button> -->
                        <button class=" p-2 col-start-12  font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg px-5 bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150"
                            @click="handleUpdate(this.$refs.ruleFormRef)" v-if="formType == 'update'">
                            Cập nhập
                        </button>
                        <button class=" p-2 col-start-12 font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg px-5 bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150"
                            @click="handleCreate(this.$refs.ruleFormRef)" v-if="formType == 'create'">
                            Tạo mới
                        </button>
                    </span>
                </template>
            </el-dialog>
        </el-col>
    </el-row>
    <el-row>
        <el-col :offset="2">
            <button class="w-full md:w-auto flex justify-center 
                        items-center p-3 mt-3 ml-4 space-x-4 font-sans font-bold
                        text-white rounded-[10px] shadow-lg 
                        px-9 bg-blue-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="createNewUser">
                <font-awesome-icon :icon="['fas', 'plus']" size="lg" />
                <span>Tạo mới</span>
            </button>
        </el-col>
    </el-row>
</template>

<script>
import * as userApi from '@/api/user'
import { useUserStore } from "@/stores/user-store"
import { mapStores } from 'pinia'
export default {
    data() {
        return {
            loadingStatus: false,
            search: '',
            tableData: [],
            filterTableData: [],
            dialogFormVisible: false,
            form: {
                username: '',
                password: '',
                firstName: '',
                lastName: '',
                email: '',
                avatar: '',
                address: '',
                birthDate: '',
                role: '',
                isActive: true,
                administrationCode: ''
            },
            formBackUp: null,
            avatarFile: null,
            formType: 'update',
            rules: {
                username: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                password: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                email: [{ validator: this.checkEmail, trigger: 'blur' }],
                role: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                administrationCode: [{ validator: this.checkEmptyField, trigger: 'blur' }]
            },
        }
    },
    computed: {
        ...mapStores(useUserStore),
        userAvatar() {
            if (this.form.avatar.includes("http://")) {
                return this.form.avatar
            }
            return userApi.IMAGE_URL + this.form.avatar
        },
        formTitle() {
            return this.formType == 'update' ? 'Thông tin chi tiết' : 'Tạo người dùng mới'
        }
    },
    watch: {
        search(search) {
            this.filterTableData = this.tableData.filter(
                (data) =>
                    !search ||
                    data.username.toLowerCase().includes(search.toLowerCase())
            )
        }
    },
    methods: {
        // Lấy dữ liệu từ serve 
        retrieveData() {
            this.loadingStatus = true
            userApi.retrieveAllUsers().then((res) => {
                this.tableData = res.data
                this.filterTableData = this.tableData
                this.loadingStatus = false
            }).catch(err => console.log(err))
        },

        // Tạo tài khoản mới
        createNewUser() {
            this.formType = 'create'
            this.resetFormData()
            if (this.$refs.ruleFormRef != null) {
                this.$refs.ruleFormRef.clearValidate()
            }
            this.formBackUp = {
                username: this.form.username,
                firstName: this.form.firstName,
                lastName: this.form.lastName,
                email: this.form.email,
                avatar: this.form.avatar,
                address: this.form.address,
                birthDate: this.form.birthDate,
                role: this.form.role,
                isActive: this.form.isActive,
                administrationCode: this.form.administrationCode
            }
            this.dialogFormVisible = true
        },

        handleCreate(form) {
            if (!form) return
            form.validate((valid) => {
                if (valid) {
                    this.$confirm(
                        'Tạo mới tài khoản. Tiếp tục?',
                        'Xác nhận',
                        {
                            confirmButtonText: 'OK',
                            cancelButtonText: 'Hủy',
                            type: 'warning',

                        }
                    )
                        .then(() => {
                            this.loadingStatus = true
                            let user = new FormData()
                            user.append('avatar-file', this.avatarFile)
                            let formJson = JSON.stringify(this.form)
                            const formData = new Blob([formJson], {
                                type: 'application/json'
                            });
                            user.append('body', formData)
                            userApi.createUser(user)
                                .then((res) => {
                                    this.loadingStatus = false
                                    this.$notify({
                                        title: 'Thành công',
                                        message: 'Tạo tài khoản thành công',
                                        type: 'success'
                                    })
                                    this.retrieveData()
                                }).catch((err) => {
                                    this.loadingStatus = false
                                    this.$notify({
                                        title: 'Đã xảy ra lỗi',
                                        message: err.response.data.messages,  //response.data.messages
                                        type: 'error',
                                    })
                                    console.log(err.message)
                                })
                            this.dialogFormVisible = false
                        })
                        .catch((err) => {
                            console.log(err)
                        })
                } else {
                    return false
                }
            })
        },

        // Cập nhập thông tin tài khoản người dùng

        //Hàm xử lí khi ấn vào nút "Chi tiết"
        handleClickEdit(index, row) {
            if (this.$refs.ruleFormRef != null) {
                this.$refs.ruleFormRef.clearValidate()
            }
            this.formType = 'update'
            this.form = row
            this.formBackUp = {
                username: this.form.username,
                firstName: this.form.firstName,
                lastName: this.form.lastName,
                email: this.form.email,
                avatar: this.form.avatar,
                address: this.form.address,
                birthDate: this.form.birthDate,
                role: this.form.role,
                isActive: this.form.isActive,
                administrationCode: this.form.administrationCode
            }
            this.dialogFormVisible = true
        },


        // --------------Xử lí trong dialog "Thông tin chi tiết" -------------------------


        //Xử lí upload avatar

        // Xứ lí khi ấn vào avatar
        openFileInput() {
            this.$refs.uploadInput.value = null
            this.$refs.uploadInput.click()
        },
        // XỬ lí khi người dùng chọn file
        handleFileChange(event) {
            let file = event.target.files[0]
            if (!file.type.startsWith('image')) {
                this.$message.error('Vui lòng chọn file ảnh')
            } else if (file.size / 1024 / 1024 > 1) {
                this.$message.error('Vui lòng chọn file ảnh có kích thước nhỏ hơn 1MB')
            } else {
                this.avatarFile = file
                if (file != null) {
                    let avatar = URL.createObjectURL(file);
                    this.form.avatar = avatar
                }
            }
        },

        // Xóa avatar
        resetAvatar() {
            this.form.avatar = ''
            this.avatarFile = null
        },

        // Hàm xử lí khi ấn vào nút "Xóa"
        handleDelete(index, row) {
            console.log(index, row)
            this.$confirm(
                'Xóa thông tin này. Tiếp tục?',
                'Xác nhận',
                {
                    confirmButtonText: 'OK',
                    cancelButtonText: 'Hủy',
                    type: 'warning',
                }
            )
                .then(() => {
                    // this.updateAdministration()
                    console.log('submit!')
                    this.dialogFormVisible = false
                })
                .catch(() => {
                })
        },

        // Hàm xử lí khi ấn vào nút "Quay lại"
        handleCancel() {
            if (this.form.username == this.formBackUp.username
                && this.form.firstName == this.formBackUp.firstName
                && this.form.lastName == this.formBackUp.lastName
                && this.form.email == this.formBackUp.email
                && this.form.avatar == this.formBackUp.avatar
                && this.form.address == this.formBackUp.address
                && this.form.birthDate == this.formBackUp.birthDate
                && this.form.role == this.formBackUp.role
                && this.form.isActive == this.formBackUp.isActive
                && this.form.administrationCode == this.formBackUp.administrationCode) {
                this.dialogFormVisible = false
            }
            else {
                this.$confirm(
                    'Hủy bỏ thay đổi. Tiếp tục?',
                    'Xác nhận',
                    {
                        confirmButtonText: 'OK',
                        cancelButtonText: 'Hủy',
                        type: 'warning',
                    }
                )
                    .then(() => {
                        if (this.formBackUp != null) {
                            this.form.username = this.formBackUp.username
                            this.form.firstName = this.formBackUp.firstName
                            this.form.lastName = this.formBackUp.lastName
                            this.form.email = this.formBackUp.email
                            this.form.avatar = this.formBackUp.avatar
                            this.form.address = this.formBackUp.address
                            this.form.birthDate = this.formBackUp.birthDate
                            this.form.role = this.formBackUp.role
                            this.form.isActive = this.formBackUp.isActive
                            this.form.administrationCode = this.formBackUp.administrationCode
                        }
                        this.dialogFormVisible = false
                    })
                    .catch(() => {
                    })
            }

        },

        // Hàm xử lí khi ấn vào nút "Cập nhập"
        handleUpdate(form) {
            if (!form) return
            form.validate((valid) => {
                if (valid) {
                    this.$confirm(
                        'Cập nhập thông tin này. Tiếp tục?',
                        'Xác nhận',
                        {
                            confirmButtonText: 'OK',
                            cancelButtonText: 'Hủy',
                            type: 'warning',

                        }
                    )
                        .then(() => {
                            this.loadingStatus = true
                            let user = new FormData()
                            user.append('avatar-file', this.avatarFile)
                            let formJson = JSON.stringify(this.form)
                            const formData = new Blob([formJson], {
                                type: 'application/json'
                            });
                            user.append('body', formData)
                            userApi.updateUserByAdmin(this.form.username, user)
                                .then((res) => {
                                    this.loadingStatus = false
                                    this.$notify({
                                        title: 'Thành công',
                                        message: 'Cập nhập thành công',
                                        type: 'success'
                                    })
                                }).catch((err) => {
                                    let message = ''
                                    try {
                                        message = err.response.data.messages
                                    } catch (error) {
                                        console.log(err)
                                    }
                                    this.loadingStatus = false
                                    this.$notify({
                                        title: 'Đã xảy ra lỗi',
                                        message: message,
                                        type: 'error',
                                    })
                                })
                            this.dialogFormVisible = false
                        })
                        .catch((err) => {
                            console.log(err)
                        })
                } else {
                    return false
                }
            })
        },

        // Thay đổi trạng thái tài khoản của người dùng
        changeUserStatus(index, row) {
            row.loading = true
            let user = new FormData()
            user.append('avatar-file', this.avatarFile)
            let formJson = JSON.stringify(row)
            const formData = new Blob([formJson], {
                type: 'application/json'
            });
            user.append('body', formData)
            userApi.updateUserByAdmin(row.username, user)
                .then((res) => {
                    row.loading = false
                    this.$notify({
                        title: 'Thành công',
                        message: 'Cập nhập thành công',
                        type: 'success'
                    })
                    return true
                }).catch((err) => {
                    row.loading = false
                    row.isActive = !row.isActive
                    let message = ''
                    try {
                        message = err.response.data.messages
                    } catch (error) {
                        console.log(err)
                    }
                    this.$notify({
                        title: 'Đã xảy ra lỗi',
                        message: message,
                        type: 'error',
                    })
                    return false
                })
        },

        // Reset dữ liệu của form 
        resetFormData() {
            this.form = {
                username: '',
                password: '',
                firstName: '',
                lastName: '',
                email: '',
                avatar: '',
                avatarFile: null,
                address: '',
                birthDate: '',
                role: '',
                isActive: true,
                administrationCode: ''
            }
        },

        checkEmptyField(rule, value, callback) {
            if (value == null || /^\s*$/.test(value)) {
                return callback(new Error('Vui lòng nhập thông tin này'))
            }
            return callback()
        },
        //Kiểm tra dữ liệu người dùng nhập vào
        checkEmail(rule, value, callback) {
            if (value === '') {
                callback(new Error('Vui lòng nhập email'))
            } else {
                if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(value)) {
                    callback()
                }
                callback(new Error('Vui lòng nhập đúng địa chỉ email'))
            }
        },
    },
    created() {
        this.retrieveData()
    }
}
</script>

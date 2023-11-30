<template>
    <el-row>
        <el-col :offset="2">
            <h1 class=" text-[#21618C] text-[25px] font-bold m-3">
                <font-awesome-icon class="margin-right: 10px" :icon="['fas', 'users-gear']" size="lg" />
                Quản lí tài khoản người dùng
            </h1>
        </el-col>
    </el-row>
    <el-row v-loading="loadingStatus">
        <el-col :span="20" :offset="2">
            <el-card class="h-[550px]" shadow="always">
                <el-table :data="filterTableData" class="h-[530px] w-[93rem]" fit>
                    <el-table-column v-for="(item, index) in tableColumns" :key="index" :label="item.title"
                        :prop="item.value">
                    </el-table-column>
                    <el-table-column :min-width="120">
                        <template #header>
                            <el-input v-model="search" size="large" placeholder="Tìm kiếm theo username" />
                        </template>
                        <template #default="scope">
                            <el-switch class="ml-20" v-model="scope.row.is_active"
                                @change="changeUserStatus(index, scope.row)" :loading="scope.row.loading"
                                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" />
                            <el-button @click="handleEdit(scope.$index, scope.row)">Chi tiết</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-card>
            <el-dialog v-model="dialogFormVisible" :top="`10vh`" :title="`Thông tin chi tiết`">
                <el-form ref="ruleFormRef" :model="form" status-icon :rules="rules">
                    <div>
                        <el-form-item label="Username" prop="username">
                            <el-input v-model="form.username" :disabled="formType == 'update'" />
                        </el-form-item>
                        <el-form-item label="Họ" prop="first_name">
                            <el-input v-model="form.first_name" />
                        </el-form-item>
                        <el-form-item label="Tên" prop="last_name">
                            <el-input v-model="form.last_name" />
                        </el-form-item>
                        <el-form-item label="Email" prop="email">
                            <el-input v-model="form.email" />
                        </el-form-item>
                        <el-form-item label="Ảnh đại diện" prop="avatar">
                            <el-input v-model="form.avatar" />
                        </el-form-item>
                        <el-form-item label="Địa chỉ" prop="address">
                            <el-input v-model="form.address" />
                        </el-form-item>
                        <el-form-item label="Ngày sinh" prop="birth_date">
                            <el-date-picker v-model="form.birth_date" type="date" placeholder="Chọn ngày sinh"
                                size="small" />
                        </el-form-item>
                        <el-form-item label="Vai trò" prop="role">
                            <el-select v-model="form.role" placeholder="Chọn vai trò">
                                <el-option label="User" value="user" />
                                <el-option label="Admin" value="admin" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="Trực thuộc" prop="administration_name">
                            <el-input v-model="form.administration_name" />
                        </el-form-item>
                    </div>
                </el-form>
                <template #footer>
                    <span class="grid grid-cols-6 gap-4">
                        <button class="p-2 mr-5 space-x-[100px] font-sans font-bold 
                        text-white rounded-lg shadow-lg 
                        px-5 bg-red-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="dialogFormVisible = false"
                            v-if="formType == 'update'">
                            Xóa
                        </button>
                        <button class="p-2 mr-3 col-start-5 font-sans font-bold
                        text-white rounded-lg shadow-lg 
                        px-5 bg-[#839192] shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="dialogFormVisible = false">
                            Quay lại
                        </button>
                        <button class=" p-2 col-start-6  font-sans font-bold
                        text-white rounded-lg shadow-lg px-5 bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150"
                            @click="handleUpdate(this.$refs.ruleFormRef)" v-if="formType == 'update'">
                            Cập nhập
                        </button>
                        <button class=" p-2 col-start-6  font-sans font-bold
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
                        items-center p-3 mt-3 space-x-4 font-sans font-bold
                        text-white rounded-lg shadow-lg 
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
import { retrieveAllUsers, updateUser } from '@/api/user'
import { retrieveAdministrationByName } from '@/api/administration'
export default {
    data() {
        return {
            loadingStatus: false,
            search: '',
            tableColumns: [
                {
                    title: 'Họ',
                    value: 'first_name'
                },
                {
                    title: 'Tên',
                    value: 'last_name'
                },
                {
                    title: 'Username',
                    value: 'username'
                },
                {
                    title: 'Vai trò',
                    value: 'role'
                },
                {
                    title: 'Trực thuộc',
                    value: 'administration_name'
                },
            ],
            tableData: [],
            filterTableData: [],
            dialogFormVisible: false,
            form: {
                username: '',
                first_name: '',
                last_name: '',
                email: '',
                avatar: '',
                address: '',
                birth_date: '',
                role: '',
                is_active: true,
                administration_name: ''
            },
            formType: 'update',
            rules: {
                email: [{ validator: this.checkEmail, trigger: 'blur' }],
                administration_name: [{ validator: this.checkAdministrationName, trigger: 'blur' }]
            },
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
        retrieveData() {
            this.loadingStatus = true
            retrieveAllUsers().then((res) => {
                this.tableData = res.data
                this.filterTableData = this.tableData
                this.loadingStatus = false
            }).catch(err => console.log(err))
        },

        // Tạo tài khoản mới
        createNewUser() {
            this.formType = 'create'
            this.resetFormData()
            this.dialogFormVisible = true

        },
        handleEdit(index, row) {
            this.formType = 'update'
            this.form = row
            this.dialogFormVisible = true
        },
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

        // Cập nhập thông tin tài khoản người dùng
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
                            console.log(this.form)
                            updateUser(this.form)
                                .then((res) => {
                                    this.loadingStatus = false
                                    this.$notify({
                                        title: 'Thành công',
                                        message: 'Cập nhập thành công',
                                        type: 'success'
                                    })
                                    this.retrieveData()
                                }).catch((err) => {
                                    this.loadingStatus = false
                                    this.$notify({
                                        title: 'Đã xảy ra lỗi',
                                        message: err.response.data.messages,
                                        type: 'error',
                                    })
                                    this.retrieveData()
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

        // Thay đổi trạng thái tài khoản của người dùng
        changeUserStatus(index, row) {
            row.loading = true
            let user = {
                username: row.username,
                first_name: row.first_name,
                last_name: row.last_name,
                email: row.email,
                avatar: row.avatar,
                address: row.address,
                birth_date: row.birth_date,
                is_active: row.is_active,
                role: row.role,
                administration_name: row.administration_name
            }
            console.log(user)
            updateUser(user)
                .then((res) => {
                    row.loading = false
                    this.$message.success('Cập nhập thành công')
                    return true
                })
                .catch((err) => {
                    row.loading = false
                    row.is_active = !row.is_active
                    this.$message.error('Cập nhập thất bại')
                    return false
                })
        },

        // Reset dữ liệu của form 
        resetFormData() {
            this.form = {
                username: '',
                first_name: '',
                last_name: '',
                email: '',
                avatar: '',
                address: '',
                birth_date: '',
                role: '',
                is_active: true,
                administration_name: ''
            }
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
        checkAdministrationName(rule, value, callback) {
            if (value === '') {
                callback(new Error('Vui lòng nhập tên đơn vị hành chính trực thuộc'))
            } else {
                retrieveAdministrationByName(value)
                    .then(res => callback())
                    .catch(err => callback(new Error('Đơn vị hành chính không tồn tại')))
            }
        },



        createChart() {
            // let seedChart = echarts.init(this.$refs.box);
        }
    },
    created() {
        this.retrieveData()
        // createChart()
    }
}
</script>

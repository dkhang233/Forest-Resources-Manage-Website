<template>
    <p class="container bg-[url('@/assets/image/seed-facilities-bg.jpg')] bg-cover ">
    <div class="grid grid-cols-20 px-[9rem] pt-3">
        <div class="col-start-3">
            <el-card class="h-[33rem] w-[64rem] rounded-3xl" shadow="always" v-loading="loadingStatus">
                <el-table :data="filterTableData" class="h-[32rem] break-normal"
                    style="--el-table-row-hover-bg-color: #D0D3D4;" fit>
                    <el-table-column label="Mã" prop="code" align="center">
                    </el-table-column>
                    <el-table-column label="Tên" prop="name" align="center" min-width="150">
                    </el-table-column>
                    <el-table-column label="Ngày thành lập" prop="date" align="center" min-width="100">
                    </el-table-column>
                    <el-table-column label="Sức chứa" prop="capacity" align="center">
                    </el-table-column>
                    <el-table-column label="Trực thuộc (mã)" prop="administration[code]" align="center" min-width="100">
                    </el-table-column>
                    <el-table-column min-width="150" align="center">
                        <template #header>
                            <el-input v-model="search" size="large" placeholder="Tìm kiếm theo tên" />
                        </template>
                        <template #default="scope">
                            <el-button @click="handleClickUpdate(scope.$index, scope.row)">Chi tiết</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-card>
            <el-dialog id="dialog" class=" block rounded-lg
                    bg-white p-6 
                    shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)]
                    dark:bg-neutral-700" top="4vh" v-model="dialogFormVisible" :title="formTitle"
                :before-close="handleCancel">
                <el-form class="grid grid-cols-10" ref="ruleFormRef" :model="form" status-icon :rules="rules"
                    size="default" label-position="top">
                    <div class="col-start-1 col-span-10">
                        <el-form-item label="Mã" prop="code">
                            <el-input v-model="form.code" :disabled="formType == 'update'" />
                        </el-form-item>
                        <el-form-item label="Tên" prop="name">
                            <el-input v-model="form.name" />
                        </el-form-item>
                        <el-form-item label="Ngày thành lập" prop="date">
                            <el-date-picker v-model="form.date" type="date" locale="vi"
                                placeholder="Chọn ngày thành lập" size="default" :disabled-date="disabledDate" />
                        </el-form-item>
                        <el-form-item label="Sức chứa" prop="capacity">
                            <el-input v-model="form.capacity" />
                        </el-form-item>
                        <el-form-item label="Trực thuộc (mã)" prop="administrationCode">
                            <el-input v-model="form.administrationCode" />
                        </el-form-item>
                    </div>
                </el-form>
                <template #footer>
                    <span class="grid grid-cols-16 gap-4">
                        <button class="p-2 mr-3  font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg 
                        px-5 bg-red-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="handleDelete"
                            v-if="formType == 'update'">
                            Xóa
                        </button>
                        <button class=" p-2 col-start-12  font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg px-5 bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150"
                            @click="handleUpdateFacility(this.$refs.ruleFormRef)" v-if="formType == 'update'">
                            Cập nhập
                        </button>
                        <button class=" p-2 col-start-12 font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg px-5 bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150"
                            @click="handleCreateFacility(this.$refs.ruleFormRef)" v-if="formType == 'create'">
                            Tạo mới
                        </button>
                    </span>
                </template>
            </el-dialog>
        </div>
    </div>
    <el-row>
        <el-col :offset="5">
            <button class="w-full md:w-auto flex justify-center 
                        items-center p-3 my-5 space-x-4 font-sans font-bold
                        text-white rounded-lg shadow-lg 
                        px-9 bg-blue-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="handleClickCreateFacility">
                <font-awesome-icon :icon="['fas', 'plus']" size="lg" />
                <span>Tạo mới</span>
            </button>
        </el-col>
    </el-row>
    </p>
</template>

<script>
import * as woodApi from '@/api/wood'
import * as seedApi from '@/api/seed'
export default {
    data() {
        return {
            loadingStatus: false,
            search: '',
            tableData: [],
            filterTableData: [],
            dialogFormVisible: false,
            form: {
                code: '',
                name: '',
                date: '',
                capacity: 1,
                administrationCode: ''
            },
            formBackUp: null,
            formType: 'update',
            rules: {
                name: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                code: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                date: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                capacity: [{ validator: this.checkCapacity, trigger: 'blur' }],
                administrationCode: [{ validator: this.checkEmptyField, trigger: 'blur' }]
            },
        }
    },
    computed: {
        formTitle() {
            return this.formType == 'update' ? 'Thông tin chi tiết' : 'Tạo mới'
        }
    },
    watch: {
        search(search) {
            this.filterTableData = this.tableData.filter(
                (data) =>
                    !search ||
                    data.name.toLowerCase().includes(search.toLowerCase())
            )
        }
    },
    methods: {
        // Lấy dữ liệu ban đầu từ server
        retrieveData() {
            this.loadingStatus = true
            seedApi.retrieveAllFacilities().then((res) => {
                this.tableData = res.data
                this.filterTableData = this.tableData
                this.loadingStatus = false
            }).catch(err => console.log(err))
        },
        // Xử lý khi ấn vào nút "Tạo mới"
        handleClickCreateFacility() {
            this.formType = 'create'
            this.resetFormData()
            if (this.$refs.ruleFormRef != null) {
                this.$refs.ruleFormRef.clearValidate()
            }
            this.formBackUp = {
                code: this.form.code,
                name: this.form.name,
                date: this.form.date,
                capacity: this.form.capacity,
                administrationCode: this.form.administrationCode
            }
            this.dialogFormVisible = true
        },
        // Xử lý yêu cầu "Tạo mới"
        handleCreateFacility(form) {
            if (!form) return
            form.validate((valid) => {
                if (valid) {
                    this.$confirm(
                        'Tạo mới thông tin này. Tiếp tục?',
                        'Xác nhận',
                        {
                            confirmButtonText: 'OK',
                            cancelButtonText: 'Hủy',
                            type: 'warning',

                        }
                    )
                        .then(() => {
                            const loading = this.$loading({
                                target: this.$el.querySelector('#dialog')
                            })
                            seedApi.createFacility(this.form)
                                .then((res) => {
                                    loading.close()
                                    this.dialogFormVisible = false
                                    this.$notify({
                                        title: 'Thành công',
                                        message: 'Thêm mới thành công',
                                        type: 'success'
                                    })
                                    this.retrieveData()
                                }).catch((err) => {
                                    loading.close()
                                    let message = ''
                                    try {
                                        message = err.response.data.messages
                                    } catch (error) {
                                        console.log(err)
                                    }
                                    this.$notify({
                                        title: 'Đã xảy ra lỗi',
                                        message: message,  //response.data.messages
                                        type: 'error',
                                    })
                                })
                        })
                        .catch((err) => {
                            // console.log(err)
                        })
                } else {
                    return false
                }
            })
        },
        //Hàm xử lí khi ấn vào nút "Chi tiết"
        handleClickUpdate(index, row) {
            if (this.$refs.ruleFormRef != null) {
                this.$refs.ruleFormRef.clearValidate()
            }
            this.formType = 'update'
            this.form = row
            this.form.administrationCode = row.administration.code
            this.formBackUp = {
                code: this.form.code,
                name: this.form.name,
                date: this.form.date,
                capacity: this.form.capacity,
                administrationCode: this.form.administrationCode
            }
            this.dialogFormVisible = true
        },
        // Hàm xử lí khi ấn vào nút "Cập nhập"
        handleUpdateFacility(form) {
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
                            const loading = this.$loading({
                                target: this.$el.querySelector('#dialog')
                            })
                            seedApi.updateFacility(this.form)
                                .then((res) => {
                                    loading.close()
                                    this.dialogFormVisible = false
                                    this.$notify({
                                        title: 'Thành công',
                                        message: 'Cập nhập thành công',
                                        type: 'success'
                                    })
                                    this.retrieveData()
                                }).catch((err) => {
                                    loading.close()
                                    try {
                                        this.$notify({
                                            title: 'Đã xảy ra lỗi',
                                            message: err.response.data.messages,
                                            type: 'error',
                                        })
                                        console.log(err.message)
                                    } catch (error) {
                                        console.log(error)
                                    }
                                })
                        })
                        .catch((err) => {
                            // console.log(err)
                        })
                } else {
                    return false
                }
            })
        },

        // --------------Xử lí trong dialog "Thông tin chi tiết" -------------------------
        // Hàm xử lí khi ấn vào nút "Xóa"
        handleDelete() {
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
                    const loading = this.$loading({
                        target: this.$el.querySelector('#dialog')
                    })
                    seedApi.deleteFacility(this.form.code)
                        .then((res) => {
                            loading.close()
                            this.dialogFormVisible = false
                            this.$notify({
                                title: 'Thành công',
                                message: 'Xóa thành công',
                                type: 'success'
                            })
                            this.retrieveData()
                        })
                        .catch((err) => {
                            loading.close()
                            try {
                                this.$notify({
                                    title: 'Đã xảy ra lỗi',
                                    message: err.response.data.messages,
                                    type: 'error',
                                })
                                console.log(err.message)
                            } catch (error) {
                                console.log(error)
                            }
                        })
                })
                .catch(() => {
                })
        },

        // Hàm xử lí khi ấn vào nút "Quay lại"
        handleCancel() {
            if (this.form.code == this.formBackUp.code
                && this.form.name == this.formBackUp.name
                && this.form.capacity == this.formBackUp.capacity
                && this.form.administrationCode == this.formBackUp.administrationCode
            ) {
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
                            this.form.code = this.formBackUp.code
                            this.form.name = this.formBackUp.name
                            this.form.capacity = this.formBackUp.capacity
                            this.form.administrationCode = this.formBackUp.administrationCode
                        }
                        this.dialogFormVisible = false
                    })
                    .catch(() => {
                    })
            }

        },

        // Reset dữ liệu của form 
        resetFormData() {
            this.form = {
                code: '',
                name: '',
                date: '',
                capacity: '',
                administrationCode: ''
            }
        },
        checkEmptyField(rule, value, callback) {
            let pattern = /^\s*$/
            if (value == null || pattern.test(value)) {
                callback(new Error('Vui lòng nhập thông tin này'))
            }
            return callback()
        },
        checkCapacity(rule, value, callback) {
            let pattern = /^\s*$/
            if (pattern.test(value) || isNaN(value) || value < 0) {
                return callback(new Error('Giá trị của thông tin này phải là số nguyên lớn hơn hoặc bằng 0'))
            }
            return callback()
        },
        disabledDate(date) {
            return date > Date.now() ? true : false
        }
    },
    created() {
        this.retrieveData()
    }
}
</script>
<style>
.el-table .cell {
    word-break: normal;
}
</style>

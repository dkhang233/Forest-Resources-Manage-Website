<template>
    <p class="container bg-[url('@/assets/image/wood-type-bg.jpg')] ">
    <div class="grid grid-cols-20 px-[9rem] pt-4">
        <div class="col-start-3">
            <el-card class="h-[33rem]  w-[60rem] rounded-3xl" shadow="always" v-loading="loadingStatus">
                <el-table :data="filterTableData" class="h-[32rem] " style="--el-table-row-hover-bg-color: #D0D3D4;"
                    fit>
                    <el-table-column v-for="(item, index) in tableColumns" :key="index" :label="item.title"
                        :prop="item.value" align="center">
                    </el-table-column>
                    <el-table-column :min-width="120" align="center">
                        <template #header>
                            <el-input v-model="search" size="large" placeholder="Tìm kiếm theo loại gỗ" />
                        </template>
                        <template #default="scope">
                            <el-button @click="handleEdit(scope.$index, scope.row)">Chi tiết</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-card>
            <el-dialog id="dialog" class=" block rounded-lg
                    bg-white p-6 
                    shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)]
                    dark:bg-neutral-700" top="15vh" v-model="dialogFormVisible" :title="formTitle"
                :before-close="handleCancel">
                <el-form class="grid grid-cols-10" ref="ruleFormRef" :model="form" status-icon :rules="rules"
                    size="default" label-position="top">
                    <div class="col-span-3">
                        <el-form-item class="" prop="avatar">
                            <img @click="openFileInput"
                                class="rounded-full shadow-lg hover:cursor-pointer hover:opacity-60"
                                src="@/assets/image/no-image.png" v-if="form.image == ''" />
                            <img @click="openFileInput" class="rounded-full shadow-lg " :src="productionTypeImage"
                                v-if="form.image != ''" />
                            <font-awesome-icon
                                class="shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)] p-2 hover:cursor-pointer hover:opacity-60 hover:text-red-600"
                                v-if="form.image != ''" @click="resertImage" :icon="['fas', 'trash-can']" size="lg" />
                            <input class="mt-[50px]" ref="uploadInput" @change="handleFileChange" type="file"
                                v-show="false" />
                        </el-form-item>
                    </div>
                    <div class="col-start-5 col-span-10">
                        <el-form-item label="Loại gỗ" prop="woodType">
                            <el-input v-model="form.woodType" :disabled="formType == 'update'" />
                        </el-form-item>
                        <el-form-item label="Khả năng sản xuất" prop="capacity">
                            <el-input v-model="form.capacity" />
                        </el-form-item>
                    </div>
                </el-form>
                <template #footer>
                    <span class="grid grid-cols-16 gap-4">
                        <button class="p-2 mr-3  font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg 
                        px-5 bg-red-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="handleDeleteProductionType"
                            v-if="formType == 'update'">
                            Xóa
                        </button>
                        <button class=" p-2 col-start-12  font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg px-5 bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150"
                            @click="handleUpdateProductionType(this.$refs.ruleFormRef)" v-if="formType == 'update'">
                            Cập nhập
                        </button>
                        <button class=" p-2 col-start-12 font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg px-5 bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150"
                            @click="handleCreateProductionType(this.$refs.ruleFormRef)" v-if="formType == 'create'">
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
                        border transition hover:-translate-y-0.5 duration-150"
                @click="handleClickCreateProductionType">
                <font-awesome-icon :icon="['fas', 'plus']" size="lg" />
                <span>Tạo mới</span>
            </button>
        </el-col>
    </el-row>
    </p>
</template>

<script>
import * as woodApi from '@/api/wood'
export default {
    data() {
        return {
            loadingStatus: false,
            search: '',
            image: '',
            tableColumns: [
                {
                    title: 'Loại gỗ',
                    value: 'woodType'
                },
                {
                    title: 'Khả năng sản xuất',
                    value: 'capacity'
                },
            ],
            tableData: [],
            filterTableData: [],
            dialogFormVisible: false,
            form: {
                woodType: '',
                capacity: '',
                image: '',
            },
            formBackUp: null,
            imageFile: null,
            formType: 'update',
            rules: {
                woodType: [{ validator: this.checkWoodType, trigger: 'blur' }],
                capacity: [{ validator: this.checkCapacity, trigger: 'blur' }]
            },
        }
    },
    computed: {
        productionTypeImage() {
            if (this.form.image == null || this.form.image == '') {
                return ''
            }
            else if (this.form.image.includes("http://")) {
                console.log(this.form.image)
                return this.form.image
            }
            else {
                console.log(this.form.image)
                return "http://localhost:8088/api/v1/wood-facilities/production-type/images/" + this.form.image
            }
        },
        formTitle() {
            return this.formType == 'update' ? 'Thông tin chi tiết' : 'Tạo loại hình sản xuất mới'
        }
    },
    watch: {
        search(search) {
            this.filterTableData = this.tableData.filter(
                (data) =>
                    !search ||
                    data.woodType.toLowerCase().includes(search.toLowerCase())
            )
        }
    },
    methods: {
        // Lấy dữ liệu ban đầu từ server
        retrieveData() {
            this.loadingStatus = true
            woodApi.retrieveAllWoodType().then((res) => {
                this.tableData = res.data
                this.filterTableData = this.tableData
                this.loadingStatus = false
            }).catch(err => console.log(err))
        },
        // Xử lý khi ấn vào nút "Tạo mới"
        handleClickCreateProductionType() {
            this.formType = 'create'
            this.resetFormData()
            if (this.$refs.ruleFormRef != null) {
                this.$refs.ruleFormRef.clearValidate()
            }
            this.formBackUp = {
                woodType: this.form.woodType,
                capacity: this.form.capacity,
                image: this.form.image
            }
            this.dialogFormVisible = true
        },
        // Xử lý yêu cầu "Tạo mới"
        handleCreateProductionType(form) {
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
                            let productionType = new FormData()
                            productionType.append('file-image', this.imageFile)
                            let formJson = JSON.stringify(this.form)
                            const formData = new Blob([formJson], {
                                type: 'application/json'
                            });
                            productionType.append('body', formData)
                            woodApi.createProductionType(productionType)
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
                            console.log(err)
                        })
                } else {
                    return false
                }
            })
        },
        //Hàm xử lí khi ấn vào nút "Chi tiết"
        handleEdit(index, row) {
            if (this.$refs.ruleFormRef != null) {
                this.$refs.ruleFormRef.clearValidate()
            }
            this.formType = 'update'
            this.form = row
            this.formBackUp = {
                woodType: this.form.woodType,
                capacity: this.form.capacity,
                image: this.form.image
            }
            this.dialogFormVisible = true
        },


        // --------------Xử lí trong dialog "Thông tin chi tiết" -------------------------


        //Xử lí upload avatar

        // Xứ lí khi ấn vào avatar
        openFileInput() {
            this.$refs.uploadInput.click()
            this.$refs.uploadInput.value = null
        },
        // XỬ lí khi người dùng chọn file
        handleFileChange(event) {
            let file = event.target.files[0]
            if (file != null) {
                if (!file.type.startsWith('image')) {
                    this.$message.error('Vui lòng chọn file ảnh!')
                } else if (file.size / 1024 / 1024 > 1) {
                    this.$message.error('Vui lòng chọn file ảnh có kích thước nhỏ hơn 1MB!')
                } else {
                    this.imageFile = file
                    let image = URL.createObjectURL(file);
                    this.form.image = image
                }
            }
        },

        // Xóa avatar
        resertImage() {
            this.form.image = ''
            this.imageFile = null
        },

        // Hàm xử lí khi ấn vào nút "Xóa"
        handleDeleteProductionType(index, row) {
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
                    woodApi.deleteProductionType(this.form.woodType)
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
                            let message = ''
                            try {
                                message = err.response.data.messages
                                this.$notify({
                                    title: 'Đã xảy ra lỗi',
                                    message: message,
                                    type: 'error',
                                })
                            } catch (error) {

                            }
                            console.log(err)
                        })
                })
                .catch(() => {
                })
        },

        // Hàm xử lí khi ấn vào nút "Quay lại"
        handleCancel() {
            if (this.form.woodType == this.formBackUp.woodType
                && this.form.capacity == this.formBackUp.capacity
                && this.form.image == this.formBackUp.image
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
                            this.form.woodType = this.formBackUp.woodType
                            this.form.capacity = this.formBackUp.capacity
                            this.form.image = this.formBackUp.image
                        }
                        this.imageFile = null
                        this.dialogFormVisible = false
                    })
                    .catch(() => {
                    })
            }

        },

        // Hàm xử lí khi ấn vào nút "Cập nhập"
        handleUpdateProductionType(form) {
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
                            let productionType = new FormData()
                            productionType.append('file-image', this.imageFile)
                            let formJson = JSON.stringify(this.form)
                            const formData = new Blob([formJson], {
                                type: 'application/json'
                            });
                            productionType.append('body', formData)
                            woodApi.updateProductionType(productionType)
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
                        })
                } else {
                    return false
                }
            })
        },
        // Reset dữ liệu của form 
        resetFormData() {
            this.form = {
                woodType: '',
                capacity: 1,
                image: ''
            }
        },
        checkWoodType(rule, value, callback) {
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
    },
    created() {
        this.retrieveData()
    }
}
</script>

<template>
    <div>
        <div class="absolute top-0 w-[45%] h-full bg-center bg-cover bg-[url('@/assets/image/default-animal.jpg')]"
            v-if="animalImage == ''">
        </div>
        <div class="absolute top-0 w-1/2 h-full bg-center bg-cover" :style="`background-image: url('${animalImage}');`"
            v-if="animalImage != ''">
        </div>
        <div class="relative grid grid-cols-20 pl-[100px] pr-[90px] pt-[20px]">
            <!-- <img class="h-[550px] rounded-s-3xl" src="@/assets/image/default-animal.jpg" alt="" v-if="animalImage == ''" /> -->
            <!-- <img class="h-[550px] rounded-s-3xl" :src="animalImage" alt="" v-if="animalImage != ''" /> -->
            <div class="col-start-11">
                <el-card class=" h-[500px] w-[50rem] rounded-[20px]" shadow="always" v-loading="loadingStatus">
                    <el-table :data="filterTableData" class="h-[520px] hover:cursor-pointer"
                        style="--el-table-row-hover-bg-color: #D0D3D4;" fit @row-click="changeAnimalImage">
                        <el-table-column v-for="( item, index ) in tableColumns " :key="index" :label="item.title"
                            :prop="item.value" align="center">
                        </el-table-column>
                        <el-table-column :min-width="120" align="center">
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
                        <div class="col-span-3">
                            <el-form-item class="" prop="avatar">
                                <img @click="openFileInput"
                                    class="rounded-full shadow-lg hover:cursor-pointer hover:opacity-60"
                                    src="@/assets/image/no-image.png" v-if="form.image == ''" />
                                <img @click="openFileInput" class="rounded-full shadow-lg " :src="animalImage"
                                    v-if="form.image != ''" />
                                <font-awesome-icon
                                    class="shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)] p-2 hover:cursor-pointer hover:opacity-60 hover:text-red-600"
                                    v-if="form.image != ''" @click="resertImage" :icon="['fas', 'trash-can']"
                                    size="lg" />
                                <input class="mt-[50px]" ref="uploadInput" @change="handleFileChange" type="file"
                                    v-show="false" />
                            </el-form-item>
                        </div>
                        <div class="col-start-5 col-span-10">
                            <el-form-item label="Tên" prop="name">
                                <el-input v-model="form.name" :disabled="formType == 'update'" />
                            </el-form-item>
                            <div class="grid grid-cols-2 gap-5">
                                <el-form-item label="Loại" prop="animalType">
                                    <el-input v-model="form.animalType" />
                                </el-form-item>
                                <el-form-item label="Thức ăn chính" prop="mainFood">
                                    <el-input v-model="form.mainFood" />
                                </el-form-item>
                            </div>
                            <el-form-item label="Bệnh chính" prop="mainDisease">
                                <el-input v-model="form.mainDisease" />
                            </el-form-item>
                            <div class="grid grid-cols-2 gap-5">
                                <el-form-item label="Tuổi thọ" prop="longevity">
                                    <el-input v-model="form.longevity" size="default" />
                                </el-form-item>
                                <el-form-item label="Loại biến động" prop="fluctuationName">
                                    <el-select v-model="form.fluctuationName" class="m-2" placeholder="Select">
                                        <el-option key="1" label="Theo chu kỳ" value="Theo chu kỳ" />
                                        <el-option key="2" label="Không theo chu kỳ" value="Không theo chu kỳ" />
                                    </el-select>
                                </el-form-item>
                            </div>
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
            </div>
        </div>
        <el-row>
            <el-col :offset="10">
                <button class="w-full md:w-auto flex justify-center 
                        items-center p-3 mt-3 space-x-4 font-sans font-bold
                        text-white rounded-lg shadow-lg 
                        px-9 bg-blue-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="handleClickCreate">
                    <font-awesome-icon :icon="['fas', 'plus']" size="lg" />
                    <span>Tạo mới</span>
                </button>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import * as animalApi from '@/api/animal'
import * as administrationApi from '@/api/administration'
export default {
    data() {
        return {
            loadingStatus: false,
            search: '',
            image: '',
            tableColumns: [
                {
                    title: 'Tên',
                    value: 'name'
                },
                {
                    title: 'Loại',
                    value: 'animalType'
                },
                // {
                //     title: 'Thức ăn chính',
                //     value: 'mainFood'
                // },
                // {
                //     title: 'Bệnh chính',
                //     value: 'mainDisease'
                // },
                // {
                //     title: 'Tuổi thọ',
                //     value: 'longevity'
                // },
                {
                    title: 'Loại biến động',
                    value: 'fluctuation[name]'
                }
            ],
            tableData: [],
            filterTableData: [],
            dialogFormVisible: false,
            form: {
                name: '',
                animalType: '',
                image: '',
                mainFood: '',
                mainDisease: '',
                longevity: '',
                fluctuationName: '',
            },
            formBackUp: null,
            imageFile: null,
            formType: 'update',
            rules: {
                name: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                animalType: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                mainFood: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                mainDisease: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                longevity: [{ validator: this.checkLongevity, trigger: 'blur' }],
                fluctuationName: [{ validator: this.checkEmptyField, trigger: 'blur' }],
            },
        }
    },
    computed: {
        animalImage() {
            if (this.form.image == null || this.form.image == '') {
                return ''
            }
            else if (this.form.image.includes("http://")) {
                console.log(this.form.image)
                return this.form.image
            }
            else {
                console.log(this.form.image)
                return animalApi.IMAGE_URL + this.form.image
            }
        },
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
        // Lấy dữ liệu từ serve 
        retrieveData() {
            this.loadingStatus = true
            animalApi.retrieveAllAnimalSpecies().then((res) => {
                this.tableData = res.data
                this.filterTableData = this.tableData
                this.loadingStatus = false
            }).catch(err => console.log(err))
        },

        changeAnimalImage(row) {
            this.form.image = row.image
        },
        // Tạo tài khoản mới
        handleClickCreate() {
            this.formType = 'create'
            this.resetFormData()
            if (this.$refs.ruleFormRef != null) {
                this.$refs.ruleFormRef.clearValidate()
            }
            this.formBackUp = {
                name: '',
                animalType: '',
                image: '',
                mainFood: '',
                mainDisease: '',
                longevity: '',
                fluctuationName: '',
            }
            this.dialogFormVisible = true
        },

        handleCreate(form) {
            if (!form) return
            form.validate((valid) => {
                if (valid) {
                    this.$confirm(
                        'Tạo mới loài động vật. Tiếp tục?',
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
                            let animal = new FormData()
                            animal.append('file-image', this.imageFile)
                            this.form.fluctuationId = this.form.fluctuationName === 'Không theo chu kỳ' ? 2 : 1
                            let formJson = JSON.stringify(this.form)
                            const formData = new Blob([formJson], {
                                type: 'application/json'
                            });
                            animal.append('body', formData)
                            console.log(`animal: ${formData}`)
                            animalApi.createAmimal(this.form.name, animal)
                                .then((res) => {
                                    loading.close()
                                    this.dialogFormVisible = false
                                    this.$notify({
                                        title: 'Thành công',
                                        message: 'Thêm loại động vật thành công',
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

        // Cập nhập thông tin tài khoản người dùng

        //Hàm xử lí khi ấn vào nút "Chi tiết"
        handleClickUpdate(index, row) {
            if (this.$refs.ruleFormRef != null) {
                this.$refs.ruleFormRef.clearValidate()
            }
            this.formType = 'update'
            this.form = row
            this.form.fluctuationName = row.fluctuation.name
            this.formBackUp = {
                name: this.form.name,
                animalType: this.form.animalType,
                image: this.form.image,
                mainFood: this.form.mainFood,
                mainDisease: this.form.mainDisease,
                longevity: this.form.longevity,
                fluctuationName: this.form.fluctuationName,
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
                    this.$message.error('Vui lòng chọn file ảnh')
                } else if (file.size / 1024 / 1024 > 1) {
                    this.$message.error('Vui lòng chọn file ảnh có kích thước nhỏ hơn 1MB')
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
                    animalApi.deleteAnimalSpecie(this.form.name)
                        .then(() => {
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
            if (this.form.name == this.formBackUp.name
                && this.form.image == this.formBackUp.image
                && this.form.animalType == this.formBackUp.animalType
                && this.form.mainFood == this.formBackUp.mainFood
                && this.form.mainDisease == this.formBackUp.mainDisease
                && this.form.longevity == this.formBackUp.longevity
                && this.form.fluctuationName == this.formBackUp.fluctuationName) {
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
                            this.form.name = this.formBackUp.name
                            this.form.image = this.formBackUp.image
                            this.form.animalType = this.formBackUp.animalType
                            this.form.mainFood = this.formBackUp.mainFood
                            this.form.mainDisease = this.formBackUp.mainDisease
                            this.form.longevity = this.formBackUp.longevity
                            this.form.fluctuationName = this.formBackUp.fluctuationName
                        }
                        this.imageFile = null
                        console.log(this.imageFile)
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
                            const loading = this.$loading({
                                target: this.$el.querySelector('#dialog')
                            })
                            let animal = new FormData()
                            animal.append('file-image', this.imageFile)
                            this.form.fluctuationId = this.form.fluctuationName === 'Không theo chu kỳ' ? 2 : 1
                            let formJson = JSON.stringify(this.form)
                            const formData = new Blob([formJson], {
                                type: 'application/json'
                            });
                            animal.append('body', formData)
                            animalApi.updateAnimalSpecie(this.form.name, animal)
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
                            console.log(err)
                        })
                } else {
                    return false
                }
            })
        },


        // Reset dữ liệu của form 
        resetFormData() {
            this.form = {
                name: '',
                image: '',
                animalType: '',
                mainFood: '',
                mainDisease: '',
                longevity: '',
                fluctuationName: '',
            }
        },

        checkEmptyField(rule, value, callback) {
            if (value == null || /^\s*$/.test(value)) {
                return callback(new Error('Vui lòng nhập thông tin này'))
            }
            return callback()
        },
        checkLongevity(rule, value, callback) {
            if (value == null || /^\s*$/.test(value)) {
                return callback(new Error('Vui lòng nhập thông tin này'))
            }
            if (isNaN(value) || value <= 0) {
                return callback(new Error('Thông tin không hợp lệ'))
            }
            return callback()
        }
    },
    created() {
        this.retrieveData()
    }
}
</script>

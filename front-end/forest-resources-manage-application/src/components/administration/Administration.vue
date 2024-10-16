<template>
    <div class="m-5">
        <div>
            <el-card class="administrations w-[50%] h-[35rem]" v-loading="loadingStatus">
                <template #header>
                    <font-awesome-icon :icon="['fas', 'magnifying-glass']" flip="horizontal" size="lg" />
                    <el-input :offset="2" v-model="filterText" placeholder="Tìm kiếm đơn vị hành chính theo tên hoặc mã"
                        class="form" />
                </template>
                <el-tree-v2 default-expand-all ref="treeRef" class="el-tree" :data="treeData" :props="defaultProps"
                    :item-size="50" :expand-on-click-node="false" :filter-method="filterNode" :height="500">
                    <template #default="{ node, data }">
                        <span class="custom-tree-node">
                            <span>{{ `${node.label} (${data.code})` }}</span>
                            <a class="text-blue-500 ml-[80%] hover:text-blue-300" @click="showNode(data)">Chi
                                tiết</a>
                        </span>
                    </template>
                </el-tree-v2>
            </el-card>
        </div>
        <img class="absolute top-0 bottom-0 right-0 left-[50rem] h-[38rem] w-[38%]"
            src="@/assets/image/vietnam_map.png" />
        <el-dialog v-model="dialogFormVisible" title="Thông tin đơn vị hành chính">
            <el-form ref="ruleFormRef" :model="form" status-icon :rules="rules">
                <el-form-item label="Mã" prop="code">
                    <el-input v-model="form.code" autocomplete="off" disabled />
                </el-form-item>
                <el-form-item label="Cấp Hành Chính" prop="level">
                    <el-select v-model="form.level" placeholder="Chọn cấp hành chính">
                        <el-option label="xã" value="xã" />
                        <el-option label="thị trấn" value="thị trấn" />
                        <el-option label="huyện" value="huyện" />
                        <el-option label="thành phố" value="thành phố" />
                        <el-option label="tỉnh" value="tỉnh" />
                    </el-select>
                </el-form-item>
                <el-form-item label="Tên" prop="name">
                    <el-input v-model="form.name" placeholder="" />
                </el-form-item>
                <el-form-item label="Trực thuộc" prop="sub">
                    <el-input v-model="form.subCode" autocomplete="off" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="grid grid-cols-6 gap-4">
                    <!-- <button class="p-2 mr-5 space-x-[100px] font-sans font-bold 
                        text-white rounded-lg shadow-lg 
                        px-5 bg-red-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="deleteBtn">
                        Xóa
                    </button> -->
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
                        @click="updateBtn(this.$refs.ruleFormRef)">
                        Cập nhập
                    </button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import { retrieveSubAdministrationsWithHierarchy, updateAdministration } from "../../api/administration"
import { mapStores } from 'pinia'
import { useUserStore } from "../../stores/user-store"
import Map from "@/components/map/Map";
export default {
    components: {
        Map
    },
    data() {
        return {
            defaultProps: {
                value: `code`,
                children: `children`,
                label: `fullName`,
            },
            filterText: "",
            treeData: [],
            dialogFormVisible: false,
            form: {
                code: "",
                level: "",
                name: "",
                subCode: "",
            },
            rules: {
                name: [{ validator: this.checkAdministrationName, trigger: 'change' }, { validator: this.checkAdministrationSub, trigger: 'change' }]
            },
            loadingStatus: false
        }
    },
    watch: {
        filterText(val) {
            this.$refs.treeRef.filter(val)
        }
    },
    computed: {
        ...mapStores(useUserStore)
    },
    methods: {
        filterNode(value, dataa) {
            console.log(dataa)
            if (!value) return true
            return dataa.fullName.includes(value) || dataa.code.includes(value)
        },
        showNode(node) {
            this.form.code = node.code
            this.form.name = node.name
            this.form.level = node.levelName
            this.form.subCode = node.subordinateCode
            this.dialogFormVisible = true
        },
        checkAdministrationName(rule, value, callback) {
            if (!value) {
                return callback(new Error('Vui lòng nhập mã hành chính'))
            }
            else {
                callback()
            }
        },
        checkAdministrationSub(rule, value, callback) {
            if (!value) {
                return callback(new Error('Vui lòng trực thuộc'))
            }
            else {
                callback()
            }
        },
        updateBtn(formEl) {
            if (!formEl) return
            formEl.validate((valid) => {
                if (valid) {
                    this.$confirm(
                        'Cập nhập đơn vị hành chính này. Tiếp tục?',
                        'Xác nhận',
                        {
                            confirmButtonText: 'OK',
                            cancelButtonText: 'Hủy',
                            type: 'warning',
                        }
                    )
                        .then(() => {
                            this.updateAdministration()
                            this.dialogFormVisible = false
                        })
                        .catch(() => {
                        })
                } else {
                    console.log('error submit!')
                    return false
                }
            })
        },
        deleteBtn() {
            this.$confirm(
                'Xóa đơn vị hành chính này. Tiếp tục?',
                'Cảnh báo',
                {
                    confirmButtonText: 'OK',
                    cancelButtonText: 'Hủy',
                    type: 'warning',
                }
            )
                .then(() => {
                    this.dialogFormVisible = false
                    this.$notify({
                        title: 'Thành công',
                        message: 'Xóa thành công',
                        type: 'success'
                    })
                })
                .catch(() => {
                })
        },
        retrieveAdministrations() {
            this.loadingStatus = true
            retrieveSubAdministrationsWithHierarchy(35)
                .then((res) => {
                    this.treeData = res.data
                    this.loadingStatus = false
                })
                .catch(err => console.log(err));
        },
        updateAdministration() {
            this.loadingStatus = true
            updateAdministration(this.form.code, {
                name: this.form.name,
                subordinateCode: this.form.subCode,
                administrativeLevelName: this.form.level
            })
                .then(
                    (res) => {
                        this.loadingStatus = false
                        this.$notify({
                            title: 'Thành công',
                            message: 'Cập nhập thành công',
                            type: 'success'
                        })
                        this.retrieveAdministrations()
                    }
                )
                .catch(
                    (err) => {
                        this.loadingStatus = false
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
                    }
                )

        }
    },
    created() {
        this.retrieveAdministrations()
    }
}
</script>

<style scoped>
.form {
    margin: 0px 0px 0px 20px;
    width: 400px;
}

.el-tree {
    --el-tree-node-hover-bg-color: #D0D3D4;
    font-size: 20px !important;
}
</style>

<template>
    <div class="tree" v-loading="loadingStatus">
        <font-awesome-icon :icon="['fas', 'magnifying-glass']" flip="horizontal" size="lg" />
        <el-input :offset="2" v-model="filterText" placeholder="Filter keyword" class="form" />
        <el-tree ref="treeRef" class="el-tree" :data="treeData" :props="defaultProps" :expand-on-click-node="false"
            :item-size="50" :height="1000" :filter-node-method="filterNode" @node-click="showNode" />
        <el-dialog v-model="dialogFormVisible" title="Administration Details">
            <el-form ref="ruleFormRef" :model="form" status-icon :rules="rules">
                <el-form-item label="Mã" prop="code">
                    <el-input v-model="form.code" autocomplete="off" />
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
                    <el-input v-model="form.subName" autocomplete="off" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button type="danger" @click="deleteBtn" class="del-btn">Xóa</el-button>
                    <el-button @click="dialogFormVisible = false">Quay lại</el-button>
                    <el-button type="primary" @click="updateBtn(this.$refs.ruleFormRef)">
                        Cập nhập
                    </el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import { retrieveSubAdministrationsWithHierarchy, updateAdministration } from "../../api/administration"
import { mapStores } from 'pinia'
import { useUserStore } from "../../stores/user-store"
export default {
    data() {
        return {
            defaultProps: {
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
                subName: "",
            },
            rules: {
                code: [{ validator: this.checkAdministrationCode, trigger: 'change' }]
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
            if (!value) return true
            return dataa.fullName.includes(value)
        },
        showNode(node, nodeProperty, treeNode) {
            this.form.code = node.code
            this.form.name = node.name
            this.form.level = node.levelName
            this.form.subName = node.subordinateName
            this.dialogFormVisible = true
        },
        checkAdministrationCode(rule, value, callback) {
            if (!value) {
                return callback(new Error('Vui lòng nhập mã hành chính'))
            }
            if (!Number.isInteger(Number(value))) {
                callback(new Error('Mã hành chính phải là một số '))
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
                        title : 'Thành công',
                        message: 'Xóa thành công',
                        type: 'success'
                    })
                })
                .catch(() => {
                })
        },
        retrieveAdministrations() {
            this.loadingStatus = true
            retrieveSubAdministrationsWithHierarchy(this.userStore.administrativeName)
                .then((res) => {
                    this.treeData = res.data
                    this.loadingStatus = false
                })
                .catch(err => console.log(err));
        },
        updateAdministration() {
            this.loadingStatus = true
            updateAdministration({
                code: this.form.code,
                name: this.form.name,
                subordinateName: this.form.subName,
                administrativeLevelName: this.form.level
            })
                .then(
                    (res) => {
                        this.loadingStatus = false
                        this.$notify({
                            title : 'Thành công',
                            message: 'Cập nhập thành công',
                            type: 'success'
                        })
                        this.retrieveAdministrations()
                    }
                )
                .catch(
                    (err) => {
                        this.loadingStatus = false
                        this.$notify({
                            title : 'Thất bại',
                            message: 'Cập nhập thất bại',
                            type: 'error',
                        })
                        this.$notify({
                            title: 'Đã xảy ra lỗi',
                            message: err.response.data.messages,
                            type: 'error',
                        })
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
.tree {
    margin: 15px 30px 10px 30px;
}

.form {
    margin: 0px 0px 0px 10px;
    width: 400px;
}

.el-tree {
    --el-tree-node-hover-bg-color: #D0D3D4;
    font-size: 20px !important;
    margin: 20px;
}

.del-btn {
    position: absolute;
    left: 0px;
}
</style>

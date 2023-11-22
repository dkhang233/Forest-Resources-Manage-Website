<template>
    <el-table :data="filterTableData">
        <el-table-column v-for="(item, index) in tableColumns" :key="index" :label="item.title" :prop="item.prop" />
        <el-table-column align="right">
            <template #header>
                <el-input v-model="search" size="large" placeholder="Tìm kiếm theo tên" />
            </template>
            <template #default="scope">
                <el-button @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
                <el-button type="danger" @click="handleDelete(scope.$index, scope.row)">Delete</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-dialog v-model="dialogFormVisible" :title="`Chỉnh sửa ${tableTitle}`">
        <el-form ref="ruleFormRef" :model="form" status-icon :rules="rules">
            <div>
                <el-form-item v-for="(item, index) in tableColumns" :key="index" :label="item.title" :prop="item.prop">
                    <el-input v-model="form[item.prop]" autocomplete="off" disabled />
                </el-form-item>
            </div>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogFormVisible = false">Quay lại</el-button>
                <el-button type="primary" @click="updateBtn(this.$refs.ruleFormRef)">
                    Cập nhập
                </el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
export default {
    props: ['tableColumn','tableDate'],
    data() {
        return {
            search: '',
            tableData: [],
            filterTableData: [],
            dialogFormVisible: false,
            rules: {
                // name: [{ validator: this.checkAdministrationName, trigger: 'change' }, { validator: this.checkAdministrationSub, trigger: 'change' }]
            },
            form: null
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
        helper(prop) {
            return `form.${prop}`
        },
        handleEdit(index, row) {
            this.form = row
            console.log(this.form)
            this.dialogFormVisible = true
            console.log(index, row)
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
        updateBtn(formEl) {
            if (!formEl) return
            formEl.validate((valid) => {
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
                            // this.updateAdministration()
                            console.log('submit!')
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
    },
    created() {
        this.filterTableData = this.tableData;
    }
}


</script>

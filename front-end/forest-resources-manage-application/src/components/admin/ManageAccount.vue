<template>
    <el-row style="margin:30px 0 10px 0;">
        <el-col :offset="2">
            <h1 style="color: #21618C;">
                <font-awesome-icon style="margin-right: 10px" :icon="['fas', 'users-gear']" size="lg" />
                Quản lí tài khoản người dùng
            </h1>
        </el-col>
    </el-row>
    <el-row>
        <el-col>
            <div class="box" ref="box"></div>
        </el-col>
        <el-col :span="20" :offset="2">
            <el-table :data="filterTableData" class="seed-table">
                <el-table-column v-for="(item, index) in tableColumns" :key="index" :label="item.title"
                    :prop="item.value" />
                <el-table-column>
                    <template #header>
                        <el-input v-model="search" size="large" placeholder="Tìm kiếm theo tên" />
                    </template>
                    <template #default="scope">
                        <el-switch v-model="scope.row.status" :before-change="beforeChange"
                            @change="changeActive(scope.$index, scope.row)" :loading="loadingActive"
                            style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949" />
                        <el-button @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-dialog v-model="dialogFormVisible" :title="`Chỉnh sửa thông tin`">
                <el-form ref="ruleFormRef" :model="form" status-icon :rules="rules">
                    <div>
                        <el-form-item v-for="(item, index) in tableColumns" :key="index" :label="item.title"
                            :prop="item.value">
                            <el-input v-model="form[item.value]" autocomplete="off" />
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
        </el-col>
    </el-row>
    <el-row style="margin:30px 0 0 0;">
        <el-col :offset="2">
            <el-button class="create-btn" type="primary" round><el-icon :size="30" style="margin-right: 10px">
                    <CirclePlus />
                </el-icon>New account</el-button>
        </el-col>
    </el-row>
</template>

<script>
export default {
    data() {
        return {
            search: '',
            status: true,
            loadingActive: false,
            tableColumns: [
                {
                    title: 'Name',
                    value: 'name'
                },
                {
                    title: 'Date',
                    value: 'date'
                },
                {
                    title: 'Address',
                    value: 'address'
                },
            ],
            tableData: [
                {
                    date: '2016-05-03',
                    name: 'Tom',
                    address: 'No. 189, Grove St, Los Angeles',
                    status: false
                },
                {
                    date: '2016-05-02',
                    name: 'John',
                    address: 'No. 189, Grove St, Los Angeles',
                },
                {
                    date: '2016-05-04',
                    name: 'Morgan',
                    address: 'No. 189, Grove St, Los Angeles',
                },
                {
                    date: '2016-05-01',
                    name: 'Jessy',
                    address: 'No. 189, Grove St, Los Angeles',
                },
                {
                    date: '2016-05-04',
                    name: 'Morgan',
                    address: 'No. 189, Grove St, Los Angeles',
                },
                {
                    date: '2016-05-01',
                    name: 'Jessy',
                    address: 'No. 189, Grove St, Los Angeles',
                },
                {
                    date: '2016-05-04',
                    name: 'Morgan',
                    address: 'No. 189, Grove St, Los Angeles',
                },
                {
                    date: '2016-05-01',
                    name: 'Jessy',
                    address: 'No. 189, Grove St, Los Angeles',
                },
            ],
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
        beforeChange() {
            this.loadingActive= true
            return new Promise((resolve) => {
                setTimeout(() => {
                    this.loadingActive = false
                    this.$message.success('Switch success')
                    return resolve(true)
                }, 1000)
            })
        },
        changeActive(index, row) {
            console.log(row)
        },
        createChart() {
            // let seedChart = echarts.init(this.$refs.box);
        }
    },
    created() {
        this.filterTableData = this.tableData
        // createChart()
    }
}


</script>


<style scoped>
.seed-table {
    margin: 30px 20px 0 20px;
    height: 480px;
}

.create-btn {
    width: 140px;
    height: 50px;
    font-size: 15px;
}
</style>
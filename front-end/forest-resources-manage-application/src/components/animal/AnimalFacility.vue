<template>
    <div v-loading="loadingStatus">
        <div class="grid grid-cols-10 ">
            <span class="col-start-3 col-span-7 font-bold text-lg">Biểu đồ thống kê tổng số lượng động vật lưu trữ tại các
                cơ
                sở</span>
            <stacked-area-chart class="col-start-3 col-span-7  h-[500px] mt-5" :chart-label="chartLabel"
                :chart-data="chartData" v-if="chartData.size != 0"></stacked-area-chart>
            <el-select v-model="dataType" class="mt-[3rem] mr-3 col-start-3  col-span-1" placeholder="Select" size="large">
                <el-option v-for="item in dataTypes" :key="item.label" :label="item.label" :value="item.value" />
            </el-select>
            <div class="mt-6 col-start-4  col-span-4 grid grid-cols-2 gap-3" v-if="dataType == 'month'">
                <div>
                    <span>Bắt đầu</span>
                    <VueDatePicker v-model="beginMonth" month-picker locale="vi" cancelText="Hủy" selectText="Chọn" />
                </div>
                <div class="">
                    <span>Kết thúc</span>
                    <VueDatePicker v-model="endMonth" month-picker locale="vi" cancelText="Hủy" selectText="Chọn" />
                </div>
            </div>
            <div class="mt-6 col-start-4  col-span-4 grid grid-cols-2 gap-3" v-if="dataType == 'quarter'">
                <div>
                    <span>Bắt đầu</span>
                    <VueDatePicker v-model="beginQuarter" quarter-picker />
                </div>
                <div class="">
                    <span>Kết thúc</span>
                    <VueDatePicker v-model="endQuarter" quarter-picker locale="vi" cancelText="Hủy" selectText="Chọn" />
                </div>
            </div>
            <div class="mt-6 col-start-4  col-span-4 grid grid-cols-2 gap-3" v-if="dataType == 'year'">
                <div>
                    <span>Bắt đầu</span>
                    <VueDatePicker v-model="beginYear" year-picker locale="vi" cancelText="Hủy" selectText="Chọn" />
                </div>
                <div class="">
                    <span>Kết thúc</span>
                    <VueDatePicker v-model="endYear" year-picker locale="vi" cancelText="Hủy" selectText="Chọn" />
                </div>
            </div>
        </div>

        <div class="pt-[100px] pb-[50px]">
            <el-row v-loading="loadingStatus">
                <el-col :span="17" :offset="5">
                    <el-card class="h-[530px] rounded-[50px] mb-2" shadow="always">
                        <el-table :data="filterTableData" class="h-[530px] w-[93rem]" fit>
                            <el-table-column v-for="(item, index) in tableColumns" :key="index" :label="item.title"
                                :prop="item.value">
                            </el-table-column>
                            <el-table-column :min-width="120">
                                <template #header>
                                    <el-input v-model="search" size="large" placeholder="Tìm kiếm theo tên" />
                                </template>
                                <template #default="scope">
                                    <el-button @click="handleEdit(scope.$index, scope.row)">Chi tiết</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-card>
                    <el-dialog class=" block rounded-lg
                    bg-white p-6 
                    shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)]
                    dark:bg-neutral-700" top="8vh" v-model="dialogFormVisible" :title="formTitle">
                        <el-form class="grid grid-cols-10 gap-10" ref="ruleFormRef" :model="form" status-icon :rules="rules"
                            size="default" label-position="top">
                            <div class="col-start-1 col-span-4">
                                <el-form-item label="Tên cơ sở" prop="name">
                                    <el-input v-model="form.name" />
                                </el-form-item>
                                <el-form-item label="Sức chứa" prop="capacity">
                                    <el-input v-model="form.capacity" />
                                </el-form-item>
                                <el-form-item label="Ngày thành lập" prop="date">
                                    <el-date-picker v-model="form.date" type="date" placeholder="Chọn ngày thành lập"
                                        size="default" />
                                </el-form-item>
                                <el-form-item label="Trực thuộc" prop="administrationName">
                                    <el-input v-model="form.administrationName" />
                                </el-form-item>
                            </div>
                            <div class="col-start-5 col-span-6">
                                <span class="text-[16px]">Số lượng động vật hiện tại của cơ sở</span>
                                <el-table class="mt-2" :data="tableData" height="300" border>
                                    <el-table-column prop="name" label="Tên động vật" />
                                    <el-table-column prop="capacity" label="Số lượng" />
                                    <el-table-column :min-width="80">
                                        <template #header>
                                        </template>
                                        <template #default="scope">
                                            <el-button @click="handleEdit(scope.$index, scope.row)">Chi tiết</el-button>
                                        </template>
                                        </el-table-column>
                                </el-table>
                            </div>
                        </el-form>
                        <template #footer>
                            <span class="grid grid-cols-16 gap-4">
                                <button class="p-2 mr-3  font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg 
                        px-5 bg-red-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="dialogFormVisible = false"
                                    v-if="formType == 'update'">
                                    Xóa
                                </button>
                                <button class="p-2 mr-3 col-start-11  font-sans font-bold text-sm 
                        text-white rounded-lg shadow-lg 
                        px-5 bg-[#839192] shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="handleCancel">
                                    Quay lại
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
                </el-col>
            </el-row>
            <el-row>
                <el-col :offset="6">
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
        </div>
    </div>
</template>

<script>
import StackedAreaChart from '../chart/StackedAreaChart.vue';
import *  as animalApi from '@/api/animal';
import { format, startOfQuarter } from "date-fns"
export default {
    name: "animalFacility",
    components: {
        StackedAreaChart
    },
    data() {
        return {
            loadingStatus: false,

            // -------------------Phần biểu đồ--------------
            quarter: startOfQuarter(new Date()),
            beginMonth: {
                month: new Date().getMonth() - 6,
                year: new Date().getFullYear()
            },
            endMonth: {
                month: new Date().getMonth() - 1,
                year: new Date().getFullYear()
            },
            beginQuarter: new Date('2023-01-01'),
            endQuarter: new Date(),
            beginYear: new Date().getFullYear() - 1,
            endYear: new Date().getFullYear(),
            chartLabel: [],
            chartData: new Map(),
            chartLabelCopy: [],
            chartDataCopy: new Map(),
            dataTypes: [
                {
                    value: 'month',
                    label: 'Tháng',
                },
                {
                    value: 'quarter',
                    label: 'Quý',
                },
                {
                    value: 'year',
                    label: 'Năm',
                }
            ],
            dataType: 'month',

            // -------------Phần bảng------------------
            search: '',
            tableColumns: [
                {
                    title: 'Tên cơ sở',
                    value: 'name'
                },
                {
                    title: 'Ngày thành lập',
                    value: 'date'
                },
                {
                    title: 'Sức chứa',
                    value: 'capacity'
                },
                {
                    title: 'Trực thuộc',
                    value: 'administration[name]'
                },
            ],
            tableData: [],
            filterTableData: [],
            dialogFormVisible: false,
            form: {
                name: '',
                date: '',
                capacity: '',
                administrationName: ''
            },
            formBackUp: null,
            formType: 'update',
            rules: {
                // name: [{ validator: this.checkUsername, trigger: 'blur' }],
                // date: [{ validator: this.checkPassword, trigger: 'blur' }],
                // capacity: [{ validator: this.checkEmail, trigger: 'blur' }],
                // administrationName: [{ validator: this.checkAdministrationName, trigger: 'blur' }]
            },
        }
        //--------------------------------------------
    },
    computed: {
        formatBeginMonth() {
            if (this.beginMonth != null) {
                let month = this.beginMonth.month < 9 ? "0" + (this.beginMonth.month + 1) : (this.beginMonth.month + 1)
                let beginMonth = this.beginMonth.year + '-' + month + '-01'
                return beginMonth
            }
        },
        formatEndMonth() {
            if (this.endMonth != null) {
                let month = this.endMonth.month < 9 ? "0" + (this.endMonth.month + 1) : (this.endMonth.month + 1)
                let endMonth = this.endMonth.year + '-' + month + '-01'
                return endMonth
            }
        },
        formatBeginQuarter() {
            if (this.beginQuarter != null) {
                let month = this.beginQuarter.getMonth() < 9 ? "0" + (this.beginQuarter.getMonth() + 1) : (this.beginQuarter.getMonth() + 1)
                let beginQuarter = this.beginQuarter.getFullYear() + '-' + month + '-01'
                return beginQuarter
            }
        },
        formatEndQuarter() {
            if (this.endQuarter != null) {
                let month = this.endQuarter.getMonth() < 9 ? "0" + (this.endQuarter.getMonth() + 1) : (this.endQuarter.getMonth() + 1)
                let endQuarter = this.endQuarter.getFullYear() + '-' + month + '-01'
                return endQuarter
            }
        },
        formTitle() {
            return "Cập nhập"
        }

    },
    watch: {
        dataType(newValue) {
            if (newValue == 'month') {
                if (this.formatBeginMonth && this.formatEndMonth != null) {
                    this.chartLabelCopy = this.chartLabel
                    this.chartLabel = []
                    this.chartDataCopy = this.chartData
                    this.chartData.clear()
                    this.setupAnimalQuantityDataByMonth(this.formatBeginMonth, this.formatEndMonth)
                }
            }
            else if (newValue == 'quarter') {
                if (this.formatBeginQuarter && this.formatEndQuarter != null) {
                    this.chartLabelCopy = this.chartLabel
                    this.chartLabel = []
                    this.chartDataCopy = this.chartData
                    this.chartData.clear()
                    this.setupAnimalQuantityDataByQuarter(this.formatBeginQuarter, this.formatEndQuarter)
                }
            }
            else {
                if (this.beginYear && this.endYear != null) {
                    this.chartLabelCopy = this.chartLabel
                    this.chartLabel = []
                    this.chartDataCopy = this.chartData
                    this.chartData.clear()
                    this.setupAnimalQuantityDataByYear(this.beginYear, this.endYear)
                }
            }
        },
        formatBeginMonth(newBeginMonth) {
            if (newBeginMonth != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupAnimalQuantityDataByMonth(newBeginMonth, this.formatEndMonth)
            }
        },
        formatEndMonth(newEndMoth) {
            if (newEndMoth != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupAnimalQuantityDataByMonth(this.formatBeginMonth, newEndMoth)
            }
        },
        formatBeginQuarter(newBeginQuarter) {
            if (newBeginQuarter != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupAnimalQuantityDataByQuarter(newBeginQuarter, this.formatEndQuarter)
            }
        },
        formatEndQuarter(newEndQuarter) {
            if (newEndQuarter != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupAnimalQuantityDataByQuarter(this.formatBeginQuarter, newEndQuarter)
            }
        },
        beginYear(newBeginYear) {
            if (newBeginYear != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupAnimalQuantityDataByYear(newBeginYear, this.endYear)
            }
            console.log(newBeginYear)
        },
        endYear(newEndYear) {
            if (newEndYear != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupAnimalQuantityDataByYear(this.beginYear, newEndYear)
            }
            console.log(newEndYear)
        }
    },
    methods: {
        setupAnimalQuantityDataByMonth(beginQuarter, endQuarter) {
            this.loadingStatus = true
            animalApi.retrieveAnimalQuantityInMoth(beginQuarter, endQuarter)
                .then((res) => {
                    for (let i = 0; i <= res.data.length - 1; i++) {
                        let label = res.data[i].date.slice(0, 7)
                        this.chartLabel.push(label)
                        for (let j = 0; j < res.data[i].data.length; j++) {
                            if (this.chartData.has(res.data[i].data[j].facilitiesName)) {
                                let tmp = this.chartData.get(res.data[i].data[j].facilitiesName)
                                tmp.push(res.data[i].data[j].quantity)
                                this.chartData.set(res.data[i].data[j].facilitiesName, tmp)
                            } else {
                                this.chartData.set(res.data[i].data[j].facilitiesName, [res.data[i].data[j].quantity])
                            }
                        }
                    }
                    this.loadingStatus = false
                })
                .catch((err) => {
                    this.loadingStatus = false
                    this.chartData = this.chartDataCopy
                    this.chartLabel = this.chartLabelCopy
                    let errorMessage = ''
                    try {
                        errorMessage = err.response.data.messages
                    } catch (err) {
                        console.log(err)
                    }
                    this.$notify({
                        title: 'Đã xảy ra lỗi',
                        message: errorMessage,
                        type: 'error',
                    })
                })
        },
        setupAnimalQuantityDataByQuarter(beginQuarter, endQuarter) {
            this.loadingStatus = true
            animalApi.retrieveAnimalQuantityInQuarter(beginQuarter, endQuarter)
                .then((res) => {
                    for (let i = 0; i <= res.data.length - 1; i++) {
                        let label = res.data[i].quarter
                        this.chartLabel.push(label)
                        for (let j = 0; j < res.data[i].data.length; j++) {
                            if (this.chartData.has(res.data[i].data[j].facilitiesName)) {
                                let tmp = this.chartData.get(res.data[i].data[j].facilitiesName)
                                tmp.push(res.data[i].data[j].quantity)
                                this.chartData.set(res.data[i].data[j].facilitiesName, tmp)
                            } else {
                                this.chartData.set(res.data[i].data[j].facilitiesName, [res.data[i].data[j].quantity])
                            }
                        }
                    }
                    this.loadingStatus = false
                })
                .catch((err) => {
                    this.loadingStatus = false
                    this.chartData = this.chartDataCopy
                    this.chartLabel = this.chartLabelCopy
                    let errorMessage = ''
                    try {
                        errorMessage = err.response.data.messages
                    } catch (err) {
                        console.log(err)
                    }
                    this.$notify({
                        title: 'Đã xảy ra lỗi',
                        message: errorMessage,
                        type: 'error',
                    })
                })
        },
        setupAnimalQuantityDataByYear(beginYear, endYear) {
            this.loadingStatus = true
            animalApi.retrieveAnimalQuantityInYear(beginYear, endYear)
                .then((res) => {
                    for (let i = 0; i <= res.data.length - 1; i++) {
                        let label = res.data[i].year
                        this.chartLabel.push(label)
                        for (let j = 0; j < res.data[i].data.length; j++) {
                            if (this.chartData.has(res.data[i].data[j].facilitiesName)) {
                                let tmp = this.chartData.get(res.data[i].data[j].facilitiesName)
                                tmp.push(res.data[i].data[j].quantity)
                                this.chartData.set(res.data[i].data[j].facilitiesName, tmp)
                            } else {
                                this.chartData.set(res.data[i].data[j].facilitiesName, [res.data[i].data[j].quantity])
                            }
                        }
                    }
                    this.loadingStatus = false
                })
                .catch((err) => {
                    this.loadingStatus = false
                    this.chartData = this.chartDataCopy
                    this.chartLabel = this.chartLabelCopy
                    let errorMessage = ''
                    try {
                        errorMessage = err.response.data.messages
                    } catch (err) {
                        console.log(err)
                    }
                    this.$notify({
                        title: 'Đã xảy ra lỗi',
                        message: errorMessage,
                        type: 'error',
                    })
                })
        },
        formatQuarter(quarter) {
            console.log(quarter)
            return format(quarter, 'QQQ')
        },
        setupAnimalFacilities() {
            this.loadingStatus = true
            animalApi.retrieveAllAnimalFacilities()
                .then((res) => {
                    this.tableData = res.data
                    this.filterTableData = this.tableData
                    this.loadingStatus = false
                })
                .catch((err) => {
                    console.log(err)
                })
        },
        //Hàm xử lí khi ấn vào nút "Chi tiết"
        handleEdit(index, row) {
            if (this.$refs.ruleFormRef != null) {
                this.$refs.ruleFormRef.clearValidate()
            }
            this.formType = 'update'
            this.form = row
            this.form.administrationName = row.administration.name
            this.formBackUp = {
                name: this.form.name,
                date: this.form.date,
                capacity: this.form.capacity,
                administrationName: this.form.administrationName
            }
            this.dialogFormVisible = true
        },
        handleCancel() {
            if (this.form == this.formBackUp) {
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
                            this.form.date = this.formBackUp.date
                            this.form.capacity = this.formBackUp.capacity
                            this.form.administrationName = this.formBackUp.administrationName
                        }
                        this.dialogFormVisible = false
                    })
                    .catch(() => {
                    })
            }

        },
    },
    created() {
        this.setupAnimalQuantityDataByMonth(this.formatBeginMonth, this.formatEndMonth)
        this.setupAnimalFacilities()
    }
}
</script>

<style>
.el-dialog__title {
    color: #2C3E50;
    font-size: 25px;
    font-weight: 800;
    margin: 3
}

.el-form-item__label {
    font-size: 16px;
}
</style>

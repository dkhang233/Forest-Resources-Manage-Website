<template>
    <div class="grid grid-cols-10 container bg-[url('@/assets/image/wood-facilities-bg.jpg')] bg-cover bg-center"
        v-loading="loadingStatus">
        <div
            class="col-start-2 col-span-8 grid grid-cols-10 rounded-[25px] bg-white mt-4 p-5 shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)]">
            <span class="col-start-3 col-span-7 font-bold text-lg">Biểu đồ thống kê tổng số lượng gỗ lưu trữ tại các
                cơ
                sở</span>
            <stacked-area-chart class="col-start-3 col-span-7  h-[400px] mt-5" :chart-label="chartLabel"
                :chart-data="chartData" v-if="chartData.size != 0"></stacked-area-chart>
            <el-select v-model="dataType" class="mt-[3rem] mr-3 col-start-3  col-span-1" placeholder="Select"
                size="large">
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

        <div class="w-[100%] col-start-2 col-span-8 pt-[4rem] pb-[2rem] ">
            <el-row v-loading="loadingStatus">
                <el-col :span="24" :offset="0">
                    <el-card class="h-[500px] rounded-[50px]" shadow="always">
                        <el-table :data="filterFacilitiesTable" class="h-[500px] w-[93rem]" fit>
                            <el-table-column label="Mã" prop="code" align="center"></el-table-column>
                            <el-table-column label="Tên cơ sở" prop="name" align="center"></el-table-column>
                            <el-table-column label="Ngày thành lập" prop="date" align="center"></el-table-column>
                            <el-table-column label="Sức chứa" prop="capacity" align="center"></el-table-column>
                            <el-table-column label="Trực thuộc (mã)" prop="administration[code]"
                                align="center"></el-table-column>
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
                    <el-dialog id="facilitiesDialog" class=" block rounded-lg
                    bg-white p-6 
                    shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)]
                    dark:bg-neutral-700" top="8vh" v-model="dialogFormVisible" :title="formTitle"
                        :before-close="handleCancel">
                        <el-form class="grid grid-cols-10 gap-10" ref="facilitiesForm" :model="form" status-icon
                            :rules="rules" size="default" label-position="top">
                            <div class="col-start-1 col-span-4">
                                <el-form-item label="Mã" prop="code">
                                    <el-input v-model="form.code" :disabled="formType == 'update'" />
                                </el-form-item>
                                <el-form-item label="Tên cơ sở" prop="name">
                                    <el-input v-model="form.name" />
                                </el-form-item>
                                <el-form-item label="Sức chứa" prop="capacity">
                                    <el-input v-model="form.capacity" />
                                </el-form-item>
                                <el-form-item label="Ngày thành lập" prop="date">
                                    <el-date-picker v-model="form.date" type="date" placeholder="Chọn ngày thành lập"
                                        size="default" :disabled-date="disabledDate" />
                                </el-form-item>
                                <el-form-item label="Hình thức hoạt động" prop="operationFormName">
                                    <el-input v-model="form.operationFormName" />
                                </el-form-item>
                                <el-form-item label="Trực thuộc (mã)" prop="administrationCode">
                                    <el-input v-model="form.administrationCode" />
                                </el-form-item>
                            </div>
                            <div class="col-start-5 col-span-6">
                                <div class="">
                                    <span class="text-[16px]">Số lượng gỗ hiện tại</span>
                                    <button class="ml-[4rem] px-3 py-2 col-span-6 col-start-1 font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="handleClickCreateQuantity">
                                        <font-awesome-icon class="pr-1" :icon="['fas', 'plus']" />
                                        Thêm bản ghi
                                    </button>
                                </div>
                                <el-table class="mt-2" :data="animalQuantityTable" height="420" border>
                                    <el-table-column prop="objName" label="Loại gỗ" align="center" />
                                    <el-table-column prop="quantity" label="Số lượng" align="center" />
                                </el-table>
                            </div>
                        </el-form>
                        <el-dialog class="w-[25rem] rounded-lg
                    bg-white p-6 
                    shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)]
                    dark:bg-neutral-700" v-model="dialogQuantityFormVisible" title="Số lượng động vật"
                            :before-close="handleCancelInAnimalTable" id="animalQuantityDialog">
                            <el-form class="grid grid-cols-10 gap-10" ref="quantityForm" :model="quantityForm"
                                status-icon :rules="rules" size="default" label-position="top">
                                <div class="col-start-1 col-span-9">
                                    <el-form-item label="Loại gỗ" prop="name">
                                        <el-input v-model="quantityForm.name" />
                                    </el-form-item>
                                    <el-form-item label="Số lượng" prop="quantity">
                                        <el-input v-model="quantityForm.quantity" />
                                    </el-form-item>
                                    <el-form-item label="Ngày thống kê" prop="date">
                                        <el-date-picker v-model="quantityForm.date" type="date" locale="vi"
                                            placeholder="Chọn ngày thống kê" size="default"
                                            :disabled-date="disabledDate" />
                                    </el-form-item>
                                </div>
                            </el-form>
                            <template #footer>
                                <span class="grid grid-cols-16 gap-4">
                                    <button class=" p-2 col-start-12  font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg px-5 bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150"
                                        @click="handleCreateQuantity(this.$refs.quantityForm)">
                                        Tạo mới
                                    </button>
                                </span>
                            </template>
                        </el-dialog>
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
                                    @click="handleUpdate(this.$refs.facilitiesForm)" v-if="formType == 'update'">
                                    Cập nhập
                                </button>
                                <button class=" p-2 col-start-12 font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg px-5 bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150"
                                    @click="handleCreateFacility(this.$refs.facilitiesForm)"
                                    v-if="formType == 'create'">
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
                        border transition hover:-translate-y-0.5 duration-150" @click="handleClickCreateFacility">
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
import *  as woodApi from '@/api/wood';
import *  as animalApi from '@/api/animal';
import { format, startOfQuarter } from "date-fns"
import { ElLoading } from 'element-plus'
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
                month: new Date().getMonth() >= 6 ? new Date().getMonth() - 6 : new Date().getMonth() + 6,
                year: new Date().getMonth() > 6 ? new Date().getFullYear() : new Date().getFullYear() - 1
            },
            endMonth: {
                month: new Date().getMonth(),
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
            facilitiesTable: [],
            filterFacilitiesTable: [],
            animalQuantityData: null,
            dialogFormVisible: false,
            dialogQuantityFormVisible: false,
            form: {
                code: '',
                name: '',
                date: '',
                capacity: '',
                operationFormName: '',
                administrationCode: ''
            },
            formBackUp: null,
            formType: 'update',
            quantityForm: {
                name: '',
                quantity: '',
                date: ''
            },
            quantityFormBackUp: null,
            rules: {
                code: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                name: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                date: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                capacity: [{ validator: this.checkCapacity, trigger: 'blur' }],
                operationFormName: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                administrationCode: [{ validator: this.checkEmptyField, trigger: 'blur' }],
                quantity: [{ validator: this.checkQuantity, trigger: 'blur' }],
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
            return this.formType == 'update' ? 'Cập nhập' : 'Tạo mới'
        },
        animalQuantityTable() {
            if (this.animalQuantityData != null && this.form.code != '') {
                return this.animalQuantityData.get(this.form.code)
            }
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
                    this.setupQuantityDataByMonth(this.formatBeginMonth, this.formatEndMonth)
                }
            }
            else if (newValue == 'quarter') {
                if (this.formatBeginQuarter && this.formatEndQuarter != null) {
                    this.chartLabelCopy = this.chartLabel
                    this.chartLabel = []
                    this.chartDataCopy = this.chartData
                    this.chartData.clear()
                    this.setupQuantityDataByQuarter(this.formatBeginQuarter, this.formatEndQuarter)
                }
            }
            else {
                if (this.beginYear && this.endYear != null) {
                    this.chartLabelCopy = this.chartLabel
                    this.chartLabel = []
                    this.chartDataCopy = this.chartData
                    this.chartData.clear()
                    this.setupQuantityDataByYear(this.beginYear, this.endYear)
                }
            }
        },
        formatBeginMonth(newBeginMonth) {
            if (newBeginMonth != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupQuantityDataByMonth(newBeginMonth, this.formatEndMonth)
            }
        },
        formatEndMonth(newEndMoth) {
            if (newEndMoth != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupQuantityDataByMonth(this.formatBeginMonth, newEndMoth)
            }
        },
        formatBeginQuarter(newBeginQuarter) {
            if (newBeginQuarter != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupQuantityDataByQuarter(newBeginQuarter, this.formatEndQuarter)
            }
        },
        formatEndQuarter(newEndQuarter) {
            if (newEndQuarter != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupQuantityDataByQuarter(this.formatBeginQuarter, newEndQuarter)
            }
        },
        beginYear(newBeginYear) {
            if (newBeginYear != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupQuantityDataByYear(newBeginYear, this.endYear)
            }
            console.log(newBeginYear)
        },
        endYear(newEndYear) {
            if (newEndYear != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupQuantityDataByYear(this.beginYear, newEndYear)
            }
            console.log(newEndYear)
        },
        search(search) {
            this.filterFacilitiesTable = this.facilitiesTable.filter(
                (data) =>
                    !search ||
                    data.name.toLowerCase().includes(search.toLowerCase())
            )
        }

    },
    methods: {
        setupQuantityDataByMonth(beginQuarter, endQuarter) {
            this.loadingStatus = true
            woodApi.retrieveWoodQuantityByMonth(beginQuarter, endQuarter)
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
                                let tmp = []
                                for (let k = 0; k < i; k++) {
                                    tmp.push(0)
                                }
                                tmp.push(res.data[i].data[j].quantity)
                                this.chartData.set(res.data[i].data[j].facilitiesName, tmp)
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
        setupQuantityDataByQuarter(beginQuarter, endQuarter) {
            this.loadingStatus = true
            woodApi.retrieveWoodQuantityByQuarter(beginQuarter, endQuarter)
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
                                let tmp = []
                                for (let k = 0; k < i; k++) {
                                    tmp.push(0)
                                }
                                tmp.push(res.data[i].data[j].quantity)
                                this.chartData.set(res.data[i].data[j].facilitiesName, tmp)
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
        setupQuantityDataByYear(beginYear, endYear) {
            this.loadingStatus = true
            woodApi.retrieveWoodQuantityByYear(beginYear, endYear)
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
                                let tmp = []
                                for (let k = 0; k < i; k++) {
                                    tmp.push(0)
                                }
                                tmp.push(res.data[i].data[j].quantity)
                                this.chartData.set(res.data[i].data[j].facilitiesName, tmp)
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
        setupFacilities() {
            this.loadingStatus = true
            woodApi.retrieveAllWoodFacilities()
                .then((res) => {
                    this.facilitiesTable = res.data
                    this.filterFacilitiesTable = this.facilitiesTable
                    this.loadingStatus = false
                })
                .catch((err) => {
                    console.log(err)
                })
        },
        setupQuantity() {
            this.loadingStatus = true
            woodApi.retrieveAllWoodQuantityNow()
                .then((res) => {
                    this.animalQuantityData = new Map(Object.entries(res.data))
                    this.loadingStatus = false
                })
                .catch((err) => {

                })
        },
        //Hàm xử lí khi ấn vào nút "Chi tiết"
        handleClickUpdate(index, row) {
            if (this.$refs.facilitiesForm != null) {
                this.$refs.facilitiesForm.clearValidate()
            }
            this.formType = 'update'
            this.form = row
            this.form.administrationCode = row.administration.code
            this.form.operationFormName = row.operationForm.name
            this.formBackUp = {
                code: this.form.code,
                name: this.form.name,
                date: this.form.date,
                capacity: this.form.capacity,
                operationFormName: this.form.operationFormName,
                administrationCode: this.form.administrationCode
            }
            this.dialogFormVisible = true
        },
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
                                target: this.$el.querySelector('#facilitiesDialog')
                            })
                            let facility = {
                                code: this.form.code,
                                name: this.form.name,
                                date: this.form.date,
                                capacity: this.form.capacity,
                                operationFormName: this.form.operationFormName,
                                adminstrationCode: this.form.administrationCode
                            }
                            woodApi.updateWoodFacility(facility)
                                .then((res) => {
                                    loading.close()
                                    this.$notify({
                                        title: 'Thành công',
                                        message: 'Cập nhập thành công',
                                        type: 'success'
                                    })
                                    this.dialogFormVisible = false
                                    this.setupFacilities()
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
        handleCancel() {
            if (this.form != null && this.formBackUp != null) {
                if (this.form.code == this.formBackUp.code
                    && this.form.name == this.formBackUp.name
                    && this.form.date == this.formBackUp.date
                    && this.form.capacity == this.formBackUp.capacity
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
                                this.form.code = this.formBackUp.code
                                this.form.name = this.formBackUp.name
                                this.form.date = this.formBackUp.date
                                this.form.capacity = this.formBackUp.capacity
                                this.form.administrationCode = this.formBackUp.administrationCode
                            }
                            this.dialogFormVisible = false
                        })
                        .catch(() => {
                        })
                }
            }
        },
        handleCancelInAnimalTable() {
            if (this.quantityFormBackUp != null && this.quantityForm != null) {
                if (this.quantityForm.name == this.quantityFormBackUp.name
                    && this.quantityForm.quantity == this.quantityFormBackUp.quantity) {
                    this.dialogquantityFormVisible = false
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
                            if (this.quantityFormBackUp != null) {
                                this.quantityForm.name = this.quantityFormBackUp.name
                                this.quantityForm.quantity = this.quantityFormBackUp.quantity
                            }
                            this.dialogQuantityFormVisible = false
                        })
                        .catch(() => {
                        })
                }
            }
            this.dialogQuantityFormVisible = false
        },
        handleClickCreateFacility() {
            if (this.$refs.facilitiesForm != null) {
                this.$refs.facilitiesForm.clearValidate()
            }
            this.formType = 'create'
            this.resetFormData()
            this.formBackUp = {
                code: this.form.code,
                name: this.form.name,
                date: this.form.date,
                capacity: this.form.capacity,
                operationFormName: this.form.operationFormName,
                administrationCode: this.form.administrationCode
            }
            this.dialogFormVisible = true
        },
        handleCreateFacility(form) {
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
                                target: this.$el.querySelector('#facilitiesDialog')
                            })
                            let facility = {
                                code: this.form.code,
                                name: this.form.name,
                                date: this.form.date,
                                capacity: this.form.capacity,
                                operationFormName: this.form.operationFormName,
                                adminstrationCode: this.form.administrationCode
                            }
                            woodApi.createWoodFacility(facility)
                                .then((res) => {
                                    loading.close()
                                    this.$notify({
                                        title: 'Thành công',
                                        message: 'Cập nhập thành công',
                                        type: 'success'
                                    })
                                    this.dialogFormVisible = false
                                    this.setupFacilities()
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
                }
                else {

                }
            })
        }
        ,
        handleClickCreateQuantity() {
            if (this.$refs.quantityForm != null) {
                this.$refs.quantityForm.clearValidate()
            }
            this.resetQuantityFormData()
            this.quantityFormBackUp = {
                name: this.quantityForm.name,
                quantity: this.quantityForm.quantity
            }
            this.dialogQuantityFormVisible = true;
        },
        handleCreateQuantity(form) {
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
                                target: this.$el.querySelector('#animalQuantityDialog')
                            })
                            let quantity = {
                                codeWF: this.form.code,
                                namePT: this.quantityForm.name,
                                quantity: this.quantityForm.quantity,
                                date: this.quantityForm.date
                            }
                            loading.close()
                            woodApi.createWoodQuantity(quantity)
                                .then((res) => {
                                    this.dialogQuantityFormVisible = false
                                    this.$notify({
                                        title: 'Thành công',
                                        message: 'Cập nhập thành công',
                                        type: 'success'
                                    })
                                    this.setupQuantity()
                                })
                                .catch((err) => {
                                    loading.close()
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
                        })
                        .catch((err) => {
                            console.log(err)
                        })
                } else {
                    return false
                }
            })
        },
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
                        target: this.$el.querySelector('#facilitiesDialog')
                    })
                    woodApi.deleteWoodFacility(this.form.code)
                        .then(() => {
                            loading.close()
                            this.dialogFormVisible = false
                            this.$notify({
                                title: 'Thành công',
                                message: 'Xóa thành công',
                                type: 'success'
                            })
                            this.setupFacilities()
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
        resetFormData() {
            this.form = {
                code: "",
                name: "",
                date: "",
                capacity: 1,
                operationFormName: "",
                administrationCode: "",

            }
        },
        resetQuantityFormData() {
            this.quantityForm = {
                name: "",
                quantity: 1
            }
        },
        checkEmptyField(rule, value, callback) {
            if (value == null || /^\s*$/.test(value)) {
                return callback(new Error('Vui lòng nhập thông tin này'))
            }
            return callback()
        },
        checkQuantity(rule, value, callback) {
            let pattern = /^\s*$/
            if (pattern.test(value) || isNaN(value)) {
                return callback(new Error('Giá trị của thông tin này phải là số nguyên'))
            }
            return callback()
        },
        checkCapacity(rule, value, callback) {
            let pattern = /^\s*$/
            if (pattern.test(value) || isNaN(value) || value < 1) {
                return callback(new Error('Giá trị của thông tin này phải là số nguyên dương'))
            }
            return callback()
        },
        disabledDate(date) {
            return date > Date.now() ? true : false
        }
    },
    created() {
        this.setupQuantityDataByMonth(this.formatBeginMonth, this.formatEndMonth)
        this.setupFacilities()
        this.setupQuantity()
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

.el-table .cell {
    word-break: normal;
}
</style>

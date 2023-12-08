<template>
    <div class="grid grid-cols-10 ">
        <span class="col-start-3 col-span-7 font-bold text-lg">Biểu đồ thống kê tổng số lượng động vật lưu trữ tại các cơ
            sở</span>
        <stacked-area-chart class="col-start-3 col-span-7  h-[500px] mt-5" :chart-label="chartLabel" :chart-data="chartData"
            v-if="chartData.size != 0"></stacked-area-chart>
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
</template>

<script>
import StackedAreaChart from '../chart/StackedAreaChart.vue';
import *  as animalApi from '@/api/animal';
import { format ,startOfQuarter } from "date-fns"
export default {
    name: "animalFacility",
    components: {
        StackedAreaChart
    },
    data() {
        return {
            quarter: startOfQuarter(new Date()),
            beginMonth: {
                month: new Date().getMonth() - 6,
                year: new Date().getFullYear()
            },
            endMonth: {
                month: new Date().getMonth() - 1,
                year: new Date().getFullYear()
            },
            beginQuarter: new Date(),
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
            dataType: 'month'
        }
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

    },
    watch: {
        formatBeginMonth(newBeginMonth) {
            if (newBeginMonth != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupAnimalQuantityData(newBeginMonth, this.formatEndMonth)
            }
        },
        formatEndMonth(newEndMoth) {
            if (newEndMoth != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupAnimalQuantityData(this.formatBeginMonth, newEndMoth)
            }
        },
        formatBeginQuarter(newBeginQuarter) {
            console.log(newBeginQuarter)
        },
        formatEndQuarter(newEndQuarter) {
            console.log(newEndQuarter)
        },
        beginYear(newBeginYear) {
            console.log(newBeginYear)
        },
        endYear(newEndYear) {
            console.log(newEndYear)
        }
    },
    methods: {
        setupAnimalQuantityData(beginMonth, endMonth) {
            animalApi.retrieveAnimalQuantityInMoth(beginMonth, endMonth)
                .then((res) => {
                    for (let i = res.data.length - 1; i >= 0; i--) {
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
                })
                .catch((err) => {
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
        }
    },
    created() {
        this.setupAnimalQuantityData(this.formatBeginMonth, this.formatEndMonth)
    }
}
</script>

<template>
    <div class="grid grid-cols-10 ">
        <span class="col-start-3 col-span-7 font-bold text-lg">Biểu đồ thống kê tổng số lượng động vật lưu trữ tại các cơ
            sở</span>
        <stacked-area-chart class="col-start-3 col-span-7  h-[500px] mt-5" :chart-label="chartLabel" :chart-data="chartData"
            v-if="chartData.size != 0"></stacked-area-chart>
        <div class="mt-5 col-start-3  col-span-2">
            <span>Ngày bắt đầu</span>
            <VueDatePicker v-model="beginMonth" month-picker  />
        </div>
        <div class="mt-5 col-start-6  col-span-2">
            <span>Ngày kết thúc</span>
            <VueDatePicker v-model="endMonth" month-picker/>
        </div>
        <VueDatePicker class="mt-5 col-start-3  col-span-2" v-model="endQuarter" quarter-picker />
    </div>
</template>

<script>
import StackedAreaChart from '../chart/StackedAreaChart.vue';
import *  as animalApi from '@/api/animal';
export default {
    name: "animalFacility",
    components: {
        StackedAreaChart
    },
    data() {
        return {
            beginMonth: {
                month: new Date().getMonth(),
                year: new Date().getFullYear()
            },
            endMonth: {
                month: new Date().getMonth(),
                year: new Date().getFullYear()
            },
            endQuarter: '',
            chartLabel: [],
            chartData: new Map(),
            chartLabelCopy: [],
            chartDataCopy: new Map()
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
        }
    },
    watch: {
        endQuarter(newEndQuarter) {
            console.log(newEndQuarter)
        },
        formatBeginMonth(newBeginMonth) {
            if (newBeginMonth != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupAnimalQuantityData(newBeginMonth,this.formatEndMonth)
            }
        },
        formatEndMonth(newEndMoth) {
            if (newEndMoth != null) {
                this.chartLabelCopy = this.chartLabel
                this.chartLabel = []
                this.chartDataCopy = this.chartData
                this.chartData.clear()
                this.setupAnimalQuantityData(this.formatBeginMonth,newEndMoth)
            }
        }
    },
    methods: {
        setupAnimalQuantityData(beginMonth,endMonth) {
            animalApi.retrieveAnimalQuantityInMoth(beginMonth,endMonth)
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
        }
    },
    created() {
        this.setupAnimalQuantityData(this.formatBeginMonth,this.formatEndMonth)
    }
}
</script>

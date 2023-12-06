<template>
    <div class="grid grid-cols-10 ">
        <span class="col-start-3 col-span-7 font-bold text-lg">Biểu đồ thống kê tổng số lượng động vật lưu trữ tại các cơ
            sở</span>
        <stacked-area-chart class="col-start-3 col-span-7  h-[500px] mt-5" :chart-label="chartLabel" :chart-data="chartData"
            v-if="chartData.size != 0"></stacked-area-chart>
        <div class="mt-5 col-start-3  ">
            <el-date-picker v-model="endDate" type="month" placeholder="Pick a month" :clearable="false" />
        </div>
        <div class="mt-5 col-start-3  ">
            <button @click="console.log(chartData)">CLciac</button>
        </div>
    </div>
</template>

<script>
import { watch } from 'vue';
import StackedAreaChart from '../chart/StackedAreaChart.vue';
import *  as animalApi from '@/api/animal';
export default {
    name: "animalFacility",
    components: {
        StackedAreaChart
    },
    data() {
        return {
            endDate: '2021-11-30',
            chartLabel: [],
            chartData: new Map()
        }
    },
    computed: {

    },
    watch: {
        endDate(newEndDate) {
            console.log(newEndDate)
            let day = (newEndDate.getDate() < 10 ? "0" + newEndDate.getDate() : newEndDate.getDate())
            let month = (newEndDate.getMonth() < 10 ? "0".concat(newEndDate.getMonth() + 1) : newEndDate.getMonth() + 1)
            let endDate = newEndDate.getFullYear() + '-' + month + '-' + day
            this.setupAnimalQuantityData(endDate)
        }
    },
    methods: {
        setupAnimalQuantityData(endDate) {
            animalApi.retrieveAnimalQuantityInMoth(endDate)
                .then((res) => {
                    for (let i = res.data.length - 1; i >= 0; i--) {
                        let label = res.data[i].date.slice(0, 7)
                        this.chartLabel.push(label)
                        for (let j = 0; j < res.data[i].data.length; j++) {
                            if (this.chartData.has(res.data[i].data[j].facilitiesCode)) {
                                let tmp = this.chartData.get(res.data[i].data[j].facilitiesCode)
                                tmp.push(res.data[i].data[j].quantity)
                                this.chartData.set(res.data[i].data[j].facilitiesCode, tmp)
                            } else {
                                this.chartData.set(res.data[i].data[j].facilitiesCode, [res.data[i].data[j].quantity])
                            }
                        }
                    }
                })
                .catch((err) => {
                    this.$notify({
                        title: 'Đã xảy ra lỗi',
                        message: err.response.data.messages,
                        type: 'error',
                    })
                })
        }
    },
    created() {
        this.setupAnimalQuantityData(this.endDate)
    }
}
</script>

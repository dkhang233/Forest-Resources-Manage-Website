<template>
    <div class="grid grid-cols-10 ">
        <span class="col-start-3 col-span-7 font-bold text-lg" >Biểu đồ thống kê tổng số lượng động vật lưu trữ tại các cơ sở</span>
        <stacked-area-chart class="col-start-3 col-span-7  h-[500px] mt-5"></stacked-area-chart>
        <span class="col-start-3 col-span-7 font-bold text-lg" >Biểu đồ thống kê tổng số lượng động vật lưu trữ tại các cơ sở</span>
        <stacked-area-chart class="col-start-3 col-span-7 h-[500px] mt-5"></stacked-area-chart>
    </div>
</template>

<script>
import StackedAreaChart from '../chart/StackedAreaChart.vue';
export default {
    name: "animalFacility",
    components:{
        StackedAreaChart
    }
}
</script>

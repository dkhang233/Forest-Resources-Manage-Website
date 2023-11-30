<template>
    <p>Đây là trang quản lí truy cập</p>
    <div ref="chart"></div>
</template>

<script>
    export default{
        name : "manageAccess",
        data(){
            return {

            }
        },
        methods : {
            createChart(){
                let myChart = echarts.init(this.$refs.chart);
            }
        },
        created(){

        }

    }
</script>
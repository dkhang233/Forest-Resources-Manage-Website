<template>
  <v-chart :option="option" autoresize />
</template>
  
<script>
import { use } from 'echarts/core';
import {
  TitleComponent,
  ToolboxComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent
} from 'echarts/components';
import { LineChart } from 'echarts/charts';
import { UniversalTransition } from 'echarts/features';
import { CanvasRenderer } from 'echarts/renderers';
import VChart, { THEME_KEY } from 'vue-echarts';

use([
  TitleComponent,
  ToolboxComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent,
  LineChart,
  CanvasRenderer,
  UniversalTransition
]);

export default {
  components: {
    VChart
  },
  props: ['chartLabel', 'chartData'],
  data() {
    return {
      legendData: []
    }
  },
  computed: {
    generateSeries() {
      let series = []
      this.chartData.forEach((quantity, label) => {
        let serie = {
          name: '',
          type: 'line',
          stack: 'Total',
          areaStyle: {},
          emphasis: {
            focus: 'series'
          },
          data: []
        }
        serie.name = label
        serie.data = quantity
        this.legendData.push(label)
        series.push(serie)
      })
      return series
    }
  },
  methods: {
    setUpData() {
      this.option = {
        title: {
          text: ''
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {
          data: this.legendData
        },
        toolbox: {
          feature: {
            // saveAsImage: {}
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: this.chartLabel
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: this.generateSeries
      }
    }
  },
  created() {
    this.setUpData()
  }
}

</script>

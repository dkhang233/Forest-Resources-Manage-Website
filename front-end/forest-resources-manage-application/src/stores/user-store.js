import { defineStore } from "pinia";

export const useUserStore = defineStore('user', {
    state: () => ({
        token: 'a',
        name: '',
        welcome: '',
        avatar: '',
        roles: 0,
        administration: { code: "35", name: "", administrativeLevel: { id : 1 , name:"tỉnh"} },
        info: {}
    }),
    getters: {
        administrativeName: (state) => {
            return state.administration.code
        },
        administrativeLevelName: (state) => {
            return state.administration.administrativeLevel.name
        }
    },
    actions: {
        // login(userInfor){
        //     return new Promise((resolve, reject) => {
        //         login(userInfo).then(response => {
        //             if (response.code === 0) {
        //                 const token = response.data
        //                 // 把接口返回的token字段的值设置到localStorage的token键值对中，token的有效期是1天,Vue.ls中的ls是localStorage的意思
        //                 Vue.ls.set(ACCESS_TOKEN, token, 24 * 60 * 60 * 1000)
        //                 // 设置token事件,修改全局变量state中的token值，讲mutations中的SET_TOKEN事件
        //                 commit('SET_TOKEN', token)
        //                 resolve()
        //             } else {
        //                 // 自定义错误
        //                 reject(new Error('用户名或密码错误'))
        //             }
        //         }).catch(error => {
        //             console.log(error)
        //             reject(error)
        //         })
        //     })
        // }
    }
})
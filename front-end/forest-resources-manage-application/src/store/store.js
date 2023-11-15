import { defineStore } from "pinia";

export const useAppStore = defineStore('app', {
    state: () => ({
        flag: false,
        userInfor: null,
        currentIndex : 1,
        menu: [
            {
                index: '1',
                title: 'Hệ thống', 
                icon: 'fa-gauge-high',
                content: [{ item: 'Quản lí tài khoản', path: '/accountsMange' }, { item: 'Quản lí truy cập', path: '/accessManage' }],
            },
            {
                index: '2',
                title: 'Hành chính', // 
                icon: 'fa-city',
                content: [],
            },
            {
                index: '3',
                title: 'Giống cây trồng', // 
                icon: 'fa-seedling',
                content: [{ item: 'Loại', path: '/allStudentsGrade' },{ item: 'Cơ sở', path: '/selectExamToPart' }],
            },
            {
                index: '4',
                title: 'Gỗ', //
                icon: 'fa-tree',
                content: [{ item: 'Loại', path: '/allStudentsGrade' }, { item: 'Cơ sở', path: '/selectExamToPart' }],
            },
            {
                index: '5',
                title: 'Động vật', //
                icon: 'fa-hippo',
                content: [{ item: 'Loại', path: '/allStudentsGrade' }, { item: 'Cơ sở', path: '/selectExamToPart' }],
            },
        ]  
    }),
    getters: {

    },
    actions: {
        toggle() {
            this.flag = !this.flag
        },
        changeUserInfo(state, info) {
            state.userInfo = info
        },
    }
});
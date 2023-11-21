import { defineStore } from "pinia";

export const useAppStore = defineStore('app', {
    state: () => ({
        flag: false,
        currentIndex : 1,
        menu: [
            {
                index: '1',
                title: 'Hành chính', // 
                icon: 'fa-city',
                content: [{item : 'Danh sách' , path : '/administration'}],
            },
            {
                index: '2',
                title: 'Giống cây trồng', // 
                icon: 'fa-seedling',
                content: [{ item: 'Loại', path: '/seedtype' },{ item: 'Cơ sở', path: '/seedfacility' }],
            },
            {
                index: '3',
                title: 'Gỗ', //
                icon: 'fa-tree',
                content: [{ item: 'Loại', path: '/woodtype' }, { item: 'Cơ sở', path: '/woodfacility' }],
            },
            {
                index: '4',
                title: 'Động vật', //
                icon: 'fa-hippo',
                content: [{ item: 'Loại', path: '/animaltype' }, { item: 'Cơ sở', path: '/animalfacility' }],
            },
        ],
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
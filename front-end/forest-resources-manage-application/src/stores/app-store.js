import { defineStore } from "pinia";

export const useAppStore = defineStore('app', {
    state: () => ({
        flag: false,
        currentIndex: 1,
        menu: [
            {
                index: '1',
                title: 'Hành chính', // 
                icon: 'fa-city',
                content: [{ item: 'Danh sách', path: '/main/administration' }],
            },
            {
                index: '2',
                title: 'Giống cây trồng', // 
                icon: 'fa-seedling',
                content: [{ item: 'Loại', path: '/main/seedtype' }, { item: 'Cơ sở', path: '/main/seedfacility' }],
            },
            {
                index: '3',
                title: 'Gỗ', //
                icon: 'fa-tree',
                content: [{ item: 'Loại', path: '/main/woodtype' }, { item: 'Cơ sở', path: '/main/woodfacility' }],
            },
            {
                index: '4',
                title: 'Động vật', //
                icon: 'fa-hippo',
                content: [{ item: 'Loại', path: '/main/animaltype' }, { item: 'Cơ sở', path: '/main/animalfacility' }],
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
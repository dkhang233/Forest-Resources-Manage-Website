import { defineStore } from "pinia";

export const useAppStore = defineStore('app', {
    state: () => ({
        flag: false,
        currentIndex: 1,
        menu: [
            {
                index: '2',
                title: 'Giống cây trồng', // 
                icon: 'fa-seedling',
                content: [{ item: 'Loại', path: '/main/seedtype' },{ item: 'Cơ sở', path: '/main/seedfacility' }],
            },
            {
                index: '3',
                title: 'Gỗ', //
                icon: 'fa-tree',
                content: [{ item: 'Loại hình sản xuất', path: '/main/woodtype' },{item: 'Hình thức hoạt động',path: '/main/operation-form'}, { item: 'Cơ sở', path: '/main/woodfacility' }],
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
import { defineStore } from "pinia"

export const useUserStore = defineStore('user', {
    state: () => ({
        token: 'a',
        username: '',
        firstName: '',
        lastName: '',
        email: '',
        avatar: '',
        address: '',
        birthDate: '',
        role: '',
        administration: null,
        active: true
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
        getInfor() {
            this.username = $cookies.get('user').username
            this.firstName = $cookies.get('user').firstName
            this.lastName = $cookies.get('user').lastName
            this.email = $cookies.get('user').email
            this.avatar = $cookies.get('user').avatar
            this.address = $cookies.get('user').address
            this.birthDate = $cookies.get('user').birthDate
            this.role = $cookies.get('user').role
            this.administration = $cookies.get('user').administrationName
            this.active = $cookies.get('user').active
        },

    }
})
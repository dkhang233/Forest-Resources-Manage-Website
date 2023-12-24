import { defineStore } from "pinia"
import * as userApi from "@/api/user"

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
            userApi.retrieveUserByUserName($cookies.get('username'))
                .then((res) => {
                    this.username = res.data.username
                    this.firstName = res.data.firstName
                    this.lastName = res.data.lastName
                    this.email = res.data.email
                    this.avatar = res.data.avatar
                    this.address = res.data.address
                    this.birthDate = res.data.birthDate
                    this.role = res.data.role
                    this.administration = res.data.administrationCode
                    this.active = res.data.active

                    $cookies.set("role",this.role)
                })
                .catch((err) => console.log(err))
        },

    }
})
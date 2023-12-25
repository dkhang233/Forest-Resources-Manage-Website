<template>
  <div>
    <main class="profile-page">
      <section class="relative block" style="height: 500px;">
        <div class="absolute top-0 w-full h-full bg-center bg-cover"
          style='background-image: url("https://images.unsplash.com/photo-1499336315816-097655dcfbda?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=2710&amp;q=80");'>
          <span id="blackOverlay" class="w-full h-full absolute opacity-50 bg-black"></span>
        </div>
        <div class="top-auto bottom-0 left-0 right-0 w-full absolute pointer-events-none overflow-hidden"
          style="height: 70px;">
          <svg class="absolute bottom-0 overflow-hidden" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="none"
            version="1.1" viewBox="0 0 2560 100" x="0" y="0">
            <polygon class="text-gray-300 fill-current" points="2560 0 2560 100 0 100"></polygon>
          </svg>
        </div>
      </section>
      <section class="relative py-16 bg-gray-300">
        <div class="container mx-auto px-4">
          <div class="relative flex flex-col min-w-0 break-words bg-white w-full mb-6 shadow-xl rounded-lg -mt-64">
            <div class="px-6">
              <div class="flex flex-wrap justify-center">
                <div class="w-full lg:w-3/12 px-4 lg:order-2 flex justify-center">
                  <div class="relative">
                    <img alt="..." src="@/assets/image/default-avatar.png"
                      class="shadow-xl rounded-full h-auto align-middle border-none absolute -m-16 -ml-20 lg:-ml-16"
                      style="max-width: 150px;" v-if="userStore.avatar == ''" />
                    <img alt="..." :src="avatar"
                      class=" shadow-xl rounded-full h-auto align-middle border-none absolute -m-16 -ml-20 lg:-ml-16"
                      style="max-width: 150px;" v-if="userStore.avatar != ''" />
                  </div>
                </div>
                <div class="w-full lg:w-4/12 px-4 lg:order-3 lg:text-right lg:self-center">
                  <div class="py-6 px-3 mt-32 sm:mt-0">
                    <button
                      class="bg-pink-500 active:bg-pink-600 text-white font-bold hover:shadow-md shadow px-4 py-2 rounded outline-none focus:outline-none sm:mr-2 mb-1"
                      type="button" style="transition: all 0.15s ease 0s;" @click="handleClickEdit">
                      Chỉnh sửa
                    </button>
                  </div>
                </div>
                <div class="w-full lg:w-4/12 px-4 lg:order-1">
                  <!-- <div class="flex justify-center py-4 lg:pt-4 pt-8">
                    <div class="mr-4 p-3 text-center">
                      <span class="text-xl font-bold block uppercase tracking-wide text-gray-700">22</span><span
                        class="text-sm text-gray-500">Friends</span>
                    </div>
                    <div class="mr-4 p-3 text-center">
                      <span class="text-xl font-bold block uppercase tracking-wide text-gray-700">10</span><span
                        class="text-sm text-gray-500">Photos</span>
                    </div>
                    <div class="lg:mr-4 p-3 text-center">
                      <span class="text-xl font-bold block uppercase tracking-wide text-gray-700">89</span><span
                        class="text-sm text-gray-500">Comments</span>
                    </div>
                  </div> -->
                </div>
              </div>
              <div class="text-center mt-12">
                <h3 class="text-4xl font-semibold leading-normal mb-2 text-gray-800">
                  {{ userStore.firstName + " " + userStore.lastName + " (" + userStore.username + ")" }}
                </h3>
                <div class="text-sm leading-normal mt-0 mb-2 text-gray-500 font-bold uppercase">
                  <i class="fas fa-map-marker-alt mr-2 text-lg text-gray-500"></i>
                  {{ userStore.address }}
                </div>
                <div class="mb-2 text-gray-700 mt-10">
                  Vai trò:<span class="font-[600] ml-2 text-lg text-gray-500">{{ userStore.role }}</span>
                </div>
                <div class="mb-2 text-gray-700">
                  Trực thuộc (mã):<span class="font-[600] ml-2 text-lg text-gray-500">{{ userStore.administration
                  }}</span>
                </div>
                <div class="mb-2 text-gray-700 mt-2">
                  Email:<span class="font-[600] ml-2 text-lg text-gray-500">{{ userStore.email }}</span>
                </div>
                <div class="mb-2 text-gray-700 mt-2">
                  Ngày sinh:<span class="font-[600] ml-2 text-lg text-gray-500">{{ userStore.birthDate }}</span>
                </div>
              </div>
              <div class="mt-10 py-10 border-t border-gray-300 text-center">
                <div class="flex flex-wrap justify-center">
                  <div class="w-full lg:w-9/12 px-4">
                    <p class="mb-4 text-lg leading-relaxed text-gray-800">
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
      <el-dialog class=" block rounded-lg
                    bg-white p-6 
                    shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)]
                    dark:bg-neutral-700" top="2vh" v-model="dialogFormVisible" title="Chỉnh sửa thông tin tài khoản"
        :before-close="handleCancel">
        <el-form class="grid grid-cols-10" ref="ruleFormRef" :model="form" status-icon :rules="rules" size="default"
          label-position="top">
          <div class="col-span-3">
            <el-form-item class="" prop="avatar">
              <input ref="uploadInput" @change="handleFileChange" type="file" v-show="false" />
              <img @click="openFileInput" class="rounded-full shadow-lg hover:cursor-pointer hover:opacity-60"
                src="@/assets/image/default-avatar.png" v-if="form.avatar == ''" />
              <img @click="openFileInput" class="rounded-full shadow-lg " :src="avatarInForm" v-if="form.avatar != ''" />
              <font-awesome-icon
                class="shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)] p-2 hover:cursor-pointer hover:opacity-60 hover:text-red-600"
                v-if="form.avatar != ''" @click="resetAvatar" :icon="['fas', 'trash-can']" size="lg" />
            </el-form-item>
          </div>
          <div class="col-start-5 col-span-10">
            <el-form-item label="Username" prop="username">
              <el-input v-model="form.username" disabled />
            </el-form-item>
            <div class="grid grid-cols-2 gap-5">
              <el-form-item label="Họ" prop="firstName">
                <el-input v-model="form.firstName" />
              </el-form-item>
              <el-form-item label="Tên" prop="lastName">
                <el-input v-model="form.lastName" />
              </el-form-item>
            </div>
            <el-form-item label="Email" prop="email">
              <el-input v-model="form.email" />
            </el-form-item>
            <div class="grid grid-cols-2 gap-5">
              <el-form-item label="Ngày sinh" prop="birthDate">
                <el-date-picker v-model="form.birthDate" type="date" placeholder="Chọn ngày sinh" size="default" />
              </el-form-item>
              <el-form-item label="Địa chỉ" prop="address">
                <el-input v-model="form.address" />
              </el-form-item>
            </div>
          </div>
        </el-form>
        <template #footer>
          <span class="grid grid-cols-16 gap-4">
            <!-- <button class="p-2 mr-3  font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg 
                        px-5 bg-red-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="dialogFormVisible = false">
              Xóa
            </button> -->
            <button class=" p-2 col-start-12  font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg px-5 bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150"
              @click="handleUpdate(this.$refs.ruleFormRef)">
              Cập nhập
            </button>
          </span>
        </template>
      </el-dialog>
    </main>
  </div>
</template>
<script>
import { mapStores } from 'pinia'
import { useUserStore } from '@/stores/user-store'
import * as userApi from '@/api/user'
export default {
  data() {
    return {
      dialogFormVisible: false,
      form: {
        username: '',
        firstName: '',
        lastName: '',
        email: '',
        avatar: '',
        address: '',
        birthDate: '',
      },
      formBackUp: {
        username: '',
        firstName: '',
        lastName: '',
        email: '',
        avatar: '',
        address: '',
        birthDate: ''
      },
      avatarFile: '',
      rules: {
        username: [{ validator: this.checkUsername, trigger: 'blur' }],
        password: [{ validator: this.checkPassword, trigger: 'blur' }],
        email: [{ validator: this.checkEmail, trigger: 'blur' }]
      }
    }
  },
  computed: {
    ...mapStores(useUserStore),
    avatar() {
      let avatar = this.userStore.avatar
      if (avatar == '') {
        return ''
      } else {
        return userApi.IMAGE_URL + avatar
      }
    },
    avatarInForm() {
      if (this.form.avatar.includes("http://")) {
        return this.form.avatar
      }
      return userApi.IMAGE_URL + this.form.avatar
    }
  },
  methods: {
    handleClickEdit() {
      if (this.$refs.ruleFormRef != null) {
        this.$refs.ruleFormRef.clearValidate()
      }
      this.formBackUp.username = this.form.username = this.userStore.username
      this.formBackUp.firstName = this.form.firstName = this.userStore.firstName
      this.formBackUp.lastName = this.form.lastName = this.userStore.lastName
      this.formBackUp.email = this.form.email = this.userStore.email
      this.formBackUp.avatar = this.form.avatar = this.userStore.avatar
      this.formBackUp.address = this.form.address = this.userStore.address
      this.formBackUp.birthDate = this.form.birthDate = this.userStore.birthDate
      this.dialogFormVisible = true
    },
    handleCancel() {
      if (this.form.username == this.formBackUp.username
        && this.form.firstName == this.formBackUp.firstName
        && this.form.lastName == this.formBackUp.lastName
        && this.form.email == this.formBackUp.email
        && this.form.avatar == this.formBackUp.avatar
        && this.form.address == this.formBackUp.address
        && this.form.birthDate == this.formBackUp.birthDate
      ) {
        this.dialogFormVisible = false
      }
      else {
        this.$confirm(
          'Hủy bỏ thay đổi. Tiếp tục?',
          'Xác nhận',
          {
            confirmButtonText: 'OK',
            cancelButtonText: 'Hủy',
            type: 'warning',
          }
        )
          .then(() => {
            this.dialogFormVisible = false
          })
          .catch(() => {
          })
      }
    },
    handleUpdate(form) {
      if (!form) return
      form.validate((valid) => {
        if (valid) {
          this.$confirm(
            'Cập nhập thông tin này. Tiếp tục?',
            'Xác nhận',
            {
              confirmButtonText: 'OK',
              cancelButtonText: 'Hủy',
              type: 'warning',

            }
          )
            .then(() => {
              this.loadingStatus = true
              let user = new FormData()
              user.append('avatar-file', this.avatarFile)
              let formJson = JSON.stringify(this.form)
              const formData = new Blob([formJson], {
                type: 'application/json'
              });
              user.append('body', formData)
              userApi.updateUser(this.form.username, user)
                .then((res) => {
                  this.userStore.getInfor()
                  this.loadingStatus = false
                  this.$notify({
                    title: 'Thành công',
                    message: 'Cập nhập thành công',
                    type: 'success'
                  })
                }).catch((err) => {
                  let message = ''
                  try {
                    message = err.response.data.messages
                  } catch (error) {
                    console.log(err)
                  }
                  this.loadingStatus = false
                  this.$notify({
                    title: 'Đã xảy ra lỗi',
                    message: message,
                    type: 'error',
                  })
                })
              this.dialogFormVisible = false
            })
            .catch((err) => {
              console.log(err)
            })
        } else {
          return false
        }
      })
    },
    openFileInput() {
      this.$refs.uploadInput.value = null
      this.$refs.uploadInput.click()
    },
    // XỬ lí khi người dùng chọn file
    handleFileChange(event) {
      let file = event.target.files[0]
      if (!file.type.startsWith('image')) {
        this.$message.error('Vui lòng chọn file ảnh')
      } else if (file.size / 1024 / 1024 > 1) {
        this.$message.error('Vui lòng chọn file ảnh có kích thước nhỏ hơn 1MB')
      } else {
        this.avatarFile = file
        if (file != null) {
          let avatar = URL.createObjectURL(file);
          this.form.avatar = avatar
        }
      }
    },
    resetAvatar() {
      this.form.avatar = ''
      this.avatarFile = null
    },
    checkUsername(rule, value, callback) {
      if (value == '') {
        callback(new Error('Vui lòng nhập username'))
      }
      return callback()
    },
    checkPassword(rule, value, callback) {
      if (value == '') {
        callback(new Error('Vui lòng nhập mật khẩu'))
      }
      return callback()
    },
    //Kiểm tra dữ liệu người dùng nhập vào
    checkEmail(rule, value, callback) {
      if (value === '') {
        callback(new Error('Vui lòng nhập email'))
      } else {
        if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(value)) {
          callback()
        }
        callback(new Error('Vui lòng nhập đúng địa chỉ email'))
      }
    },
  }
}
</script>
  
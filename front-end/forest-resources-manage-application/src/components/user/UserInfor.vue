<template>
    <el-dialog class=" block rounded-lg
                    bg-white p-6 
                    shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)]
                    dark:bg-neutral-700" top="2vh" v-model="dialogFormVisible" :title="formTitle">
        <el-form class="grid grid-cols-10" ref="ruleFormRef" :model="form" status-icon :rules="rules" size="default"
            label-position="top">
            <div class="col-span-3">
                <el-form-item class="" prop="avatar">
                    <input ref="uploadInput" @change="handleFileChange" type="file" v-show="false" />
                    <img @click="openFileInput" class="rounded-full shadow-lg hover:cursor-pointer hover:opacity-60"
                        src="@/assets/image/default-avatar.png" v-if="form.avatar == ''" />
                    <img @click="openFileInput" class="rounded-full shadow-lg " :src="userAvatar"
                        v-if="form.avatar != ''" />
                    <font-awesome-icon
                        class="shadow-[0_2px_15px_-3px_rgba(0,0,0,0.07),0_10px_20px_-2px_rgba(0,0,0,0.04)] p-2 hover:cursor-pointer hover:opacity-60 hover:text-red-600"
                        v-if="form.avatar != ''" @click="resetAvatar" :icon="['fas', 'trash-can']" size="lg" />
                </el-form-item>
            </div>
            <div class="col-start-5 col-span-10">
                <el-form-item label="Username" prop="username">
                    <el-input v-model="form.username" :disabled="formType == 'update'" />
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
                <div class="grid grid-cols-2 gap-5">
                    <el-form-item label="Vai trò" prop="role">
                        <el-select v-model="form.role" placeholder="Chọn vai trò">
                            <el-option label="User" value="user" />
                            <el-option label="Admin" value="admin" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="Trực thuộc" prop="administrationName">
                        <el-input v-model="form.administrationName" />
                    </el-form-item>
                </div>
            </div>
        </el-form>
        <template #footer>
            <span class="grid grid-cols-16 gap-4">
                <button class="p-2 mr-3  font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg 
                        px-5 bg-red-500 shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="dialogFormVisible = false"
                    v-if="formType == 'update'">
                    Xóa
                </button>
                <button class="p-2 mr-3 col-start-11  font-sans font-bold text-sm 
                        text-white rounded-lg shadow-lg 
                        px-5 bg-[#839192] shadow-blue-100 
                        hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150" @click="handleCancel">
                    Quay lại
                </button>
                <button class=" p-2 col-start-12  font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg px-5 bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150"
                    @click="handleUpdate(this.$refs.ruleFormRef)" v-if="formType == 'update'">
                    Cập nhập
                </button>
                <button class=" p-2 col-start-12 font-sans font-bold text-sm
                        text-white rounded-lg shadow-lg px-5 bg-blue-500 
                        shadow-blue-100 hover:bg-opacity-90  hover:shadow-lg 
                        border transition hover:-translate-y-0.5 duration-150"
                    @click="handleCreate(this.$refs.ruleFormRef)" v-if="formType == 'create'">
                    Tạo mới
                </button>
            </span>
        </template>
    </el-dialog>
</template>

<script>

export default{
    computed(){
        
    }
}
</script>
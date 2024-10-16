<template>
    <div v-loading="loadingStatus"
        class="flex items-center justify-center min-h-screen bg-[#FFF] bg-[url('@/assets/image/login-image-3.jpg')]  bg-cover bg-center ">
        <!-- Card Container -->
        <div class="relative flex flex-col m-6 space-y-10 bg-white bg-opacity-60 shadow-2xl rounded-2xl md:flex-row md:space-y-0 md:m-0"
            style="backdrop-filter: blur(20px);">
            <!-- Left Side -->
            <div class="p-6 md:p-20">
                <!-- Top Content -->
                <router-view @on-loading-status="loadingStatus = true"
                    @off-loading-status="loadingStatus = false"></router-view>
                <!-- Border -->
                <div class=" mt-10 border-b border-b-gray-300"></div>
            </div>

            <!-- Right Side -->
            <!-- <img src="@/assets/image/login-image.jpg" alt="" class="w-[430px] hidden md:block md:rounded-r-2xl" /> -->

            <!-- Close button -->
            <div
                class="group absolute -top-5 right-4 flex items-center justify-center w-10 h-10 bg-gray-200 rounded-full md:bg-white md:top-4 hove:cursor-pointer hover:-translate-y-0.5 transition duration-150">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-black group-hover:text-gray-600"
                    viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" fill="none" stroke-linecap="round"
                    stroke-linejoin="round">
                    <path stroke="none" d="M0 0h24v24H0z" fill="none" />
                    <line x1="18" y1="6" x2="6" y2="18" />
                    <line x1="6" y1="6" x2="18" y2="18" />
                </svg>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    data() {
        return {
            loadingStatus: false
        }
    },
    mounted() {
        this.$notify({
            title: 'Note',
            message: "User: a, Password: 1 (Vui lòng đợi 60s để server khởi động)",
            type: 'info',
            duration: 0,
        })
    }
}

</script>

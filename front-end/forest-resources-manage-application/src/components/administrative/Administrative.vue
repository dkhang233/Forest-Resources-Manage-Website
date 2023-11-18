<template>
    <div class="tree"  v-loading="load">
        <font-awesome-icon :icon="['fas', 'magnifying-glass']" flip="horizontal" size="lg" />
        <el-input :offset="2" v-model="filterText" placeholder="Filter keyword" class="form" />
        <el-tree ref="treeRef"
            class="el-tree" 
            :data="treeData" 
            :props="defaultProps" 
            :expand-on-click-node="false" 
            :item-size="50"
            :height="1000" 
            :filter-node-method="filterNode"
            @node-click="showNode" 
        />
        <el-dialog v-model="dialogFormVisible" title="Administrative Details">
            <el-form :model="form">
                <el-form-item label="Mã">
                    <el-input v-model="form.code" autocomplete="off" />
                </el-form-item>
                <el-form-item label="Cấp Hành Chính" >
                    <el-select v-model="form.level" placeholder="Chọn cấp hành chính">
                        <el-option label="xã" value="xã" />
                         <el-option label="thị trấn" value="thị trấn" />
                        <el-option label="huyện" value="huyện" />
                        <el-option label="thành phố" value="thành phố" />
                        <el-option label="tỉnh" value="tỉnh" />
                    </el-select>
                </el-form-item>
                <el-form-item label="Tên" >
                    <el-input v-model="form.name" placeholder="" />
                </el-form-item>
                <el-form-item label="Trực thuộc">
                    <el-input v-model="form.sub" autocomplete="off" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button  @click="dialogFormVisible = false">Cancel</el-button>
                    <el-button type="primary"  @click="dialogFormVisible = false">
                        Confirm
                    </el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import { retrieveSubAdministrativesWithHierarchy } from "../../api/administrative"
import { mapStores } from 'pinia'
import {useUserStore} from "../../stores/user-store"
export default {
    data() {
        return {
            defaultProps: {
                children: `children`,
                label: `fullName`,
            },
            filterText: "",
            treeData: [],
            dialogFormVisible: false,
            form: {
                code: "",
                level: "",
                name: "",
                sub: ""
            },
            load : false
        }
    },
    watch: {
        filterText(val) {
            this.$refs.treeRef.filter(val)
        }
    },
    computed: {
        ...mapStores(useUserStore)
    },
    methods: {
        filterNode(value, dataa) {
            if (!value) return true
            return dataa.fullName.includes(value)
        },
        showNode(node, nodeProperty, treeNode) {
            this.form.code = node.code
            this.form.name = node.name
            this.form.level = node.levelName
            this.form.sub = node.subordinateName
            this.dialogFormVisible = true
        }
    }
    ,
    created() {
        this.load = true
        retrieveSubAdministrativesWithHierarchy(this.userStore.administrativeName)
            .then((res) => {
                this.treeData = res.data
                this.load = false
            }) 
            .catch(err => console.log(err));

    }
}
</script>

<style scoped>
.tree {
    margin: 15px 30px 10px 30px;
}

.form {
    margin: 0px 0px 0px 10px;
    width: 400px;
}

.el-tree {
    --el-tree-node-hover-bg-color: #D0D3D4;
    font-size: 20px !important;
    margin: 20px;
}
</style>

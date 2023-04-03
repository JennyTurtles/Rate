<template>
    <div class="eassign-pe">
        <div style="display: flex; justify-content: left">
            <div style="width: 100%;text-align: center">分配选手和专家</div>
            <div style="margin-left: auto">
                <el-button icon="el-icon-back" type="primary" @click="back">
                    返回
                </el-button>
            </div>
        </div>

    </div>
</template>

<script>
export default {
    name: "EassignPE",
    data(){
        return{

        }
    },
    mounted() {
    },
    methods:{
        back(){
            this.$router.go(-1);
        }
    }
}
</script>

<style scoped>

</style>
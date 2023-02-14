<template>
    <div>
        <el-tabs v-model="activeName" type="card">
            <el-tab-pane label="职位管理" name="posmana"><PosMana></PosMana></el-tab-pane>
            <el-tab-pane label="奖惩规则" name="ecmana"><EcMana></EcMana></el-tab-pane>
            <el-tab-pane label="权限组" name="permissmana"><PermissMana></PermissMana></el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    import PosMana from '../../components/sys/basic/PosMana'
    import EcMana from '../../components/sys/basic/EcMana'
    import PermissMana from '../../components/sys/basic/PermissMana'
    export default {
        name: "SysBasic",
        data(){
            return{
                activeName: 'depmana'
            }
        },
        components:{
            PosMana,
            EcMana,
            PermissMana
        }
    }
</script>

<style scoped>

</style>

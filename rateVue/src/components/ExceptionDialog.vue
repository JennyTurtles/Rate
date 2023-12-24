<template>
  <div class="ExceptionDialog">
    <el-dialog
        title="出错了！"
        :visible.sync="visible"
        center
        width="70%">
      <el-input
          type="textarea"
          v-model="content"
          autosize
          :autosize="{maxRows: 15}"
          class="borderNone"
          style="width: 100%"
          :style="{color: 'red'}"
      ></el-input>
      <span slot="footer" class="dialog-footer">
         <el-button type="primary" @click="visible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "ExceptionDialog",
  data(){
    return{
      content:'',
      visible: false,
    }
  }
}
</script>

<style scoped>

</style>

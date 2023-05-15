<template>
    <div>
        <elt-transfer
            style="text-align: center;margin-top: 10px;"
            ref="eltTransfer"
            :show-pagination="true"
            :show-query="true"
            :pagination-call-back="paginationCallBackPar"
            :left-columns="leftColumns"
            :right-columns="rightColumns"
            :title-texts="['所有专家','本组专家']"
            :button-texts="['添加','删除']"
            :table-row-key="row => row.name"
            v-model="tableData"
        >
        <template v-slot:leftCondition="{scope}">
            <el-form-item label="" style="margin-left: 3px">
                <el-input v-model="scope.jobNumber" placeholder="工号" style="width: 120px"></el-input>
            </el-form-item>
            <el-form-item label="" style="margin-right: 3px">
                <el-input v-model="scope.name" placeholder="姓名" style="width: 80px"></el-input>
            </el-form-item>
        </template>
        </elt-transfer>

        <el-dialog
            title="导入专家到本组"
            :visible.sync="importParVisible"
            width="35%">
            <span style="font-weight:600;">导入新数据</span> 第一步：
            <el-button
                       type="primary"
                       @click="exportMo"
                       icon="el-icon-upload"
                       style="margin-right: 8px"
            >
                下载模板
            </el-button>
            第二步：
            <el-upload
                :show-file-list="false"
                :before-upload="beforeUpload"
                :on-success="onSuccess"
                :on-error="onError"
                :disabled="importDataDisabled"
                style="display: inline-flex; margin-right: 8px"
                :action="UploadUrl()"
            >
                <el-button
                    :disabled="importDataDisabled"
                    type="primary"
                    :icon="importDataBtnIcon"
                >导入专家</el-button>
            </el-upload>
            <span slot="footer" class="dialog-footer">
  </span>
        </el-dialog>
    </div>
</template>

<script>
import eltTransfer from '../../utils/eltTransfer'
import {Message} from "element-ui";
import axios from "axios";
import de from "element-ui/src/locale/lang/de";
export default {
    name: 'app',
    computed: {
        user() {
            return JSON.parse(localStorage.getItem('user')); //object信息
        }
    },
    data() {
        return {
            labelPosition: "left",
            importDataDisabled: false,
            importDataBtnIcon: 'el-icon-plus',
            importDataBtnText: '导入专家',
            scoreitem:[],
            infoitem:[],
            scoreitem_from_back:[],
            infoitem_from_back:[],
            dymatic_list:["姓名","身份证号码"],
            importParVisible:false,
            activityIDParent:-1,
            activityID:-1,
            groupIDParent:-1,
            groupID:-1,
            mode:-1,
            tableData: [],
            leftColumns: [
                {label: '工号', id: 'jobNumber', width: 'auto'},
                {label: '姓名', id: 'name', width: '90px'},
            ],
            rightColumns:[
                {label: '工号', id: 'jobNumber', width: '150px'},
                {label: '姓名', id: 'name', width: '70px'},
            ],
            experts: []
        }
    },
    mounted() {
        this.activityIDParent=this.$route.query.activityIDParent;
        this.activityID=this.$route.query.activityID;
        this.groupIDParent=this.$route.query.groupIDParent;
        this.groupID=this.$route.query.groupID;
        this.mode=this.$route.query.mode;
        this.init();
    },
    components: {
        'elt-transfer': eltTransfer,
    },
    methods: {
        // fixCount(parID,subID){ // 确保子活动的选手数和和专家数和父活动一致
        //   this.postRequest("/activities/basic/fixCount?parID="+parID+"&subID="+subID).then(res=>{
        //       if(res.status!==200){
        //           this.$message.fail('计数更新失败')
        //       }
        //   })
        // },
        UploadUrl() {
            let url = "/systemM/Experts/import?groupid=" + this.groupID + "&activityid=" + this.activityID+"&insititutionID="+
                this.user.institutionID;
            return url;
        },
        delete(delList){
            this.postRequest("/systemM/Experts/deleteExperts",delList).then(res=>{
                if(res.status==200){
                    this.$message.success('删除成功')
                    this.init();
                }else
                {
                    this.$message.fail('删除失败')
                }
            })
        },
        simpleAdd(addList){ // 将大组选手添加到当前小组，在子组件内调用的
            for (let i = 0; i < addList.length; i++) {
                addList[i].activityID=this.activityID;
                addList[i].groupID=this.groupID;
                addList[i].institutionid=this.user.institutionID;
            }
            this.postRequest("/systemM/Experts/addExperts",addList).then(res=>{
                if(res.status==200){
                    this.$message.success('添加成功')
                    this.init();
                }else
                {
                    this.$message.fail('添加失败')
                }
            })
        },
        init(){
           this.getRequest("/groups/basic/expertsByActID?activityID="+this.activityID).then(res=>{ // 获取大组内的选手
               this.experts = res.obj;
               this.getRequest("/groups/basic/experts?groupID="+this.groupID).then(res=>{ // 获取当前组内的选手
                   this.tableData = res.obj;
                   this.clearTransfer();
                   for (let i = 0; i < this.tableData.length; i++) {
                       this.$refs.eltTransfer.rightTableData.push(this.tableData[i])
                   }
               })

           })
        },
        paginationCallBackPar(obj) {
            let d = this.experts.filter((item, index) => {
                if (index >= (obj.pageIndex - 1) * obj.pageSize && index < obj.pageIndex * obj.pageSize) {
                    return true;
                }
                return false;
            });
            return new Promise(((resolve, reject) => {
                try {
                    resolve({total: this.experts.length, data: d})
                } catch {
                    reject()
                }
            }))
        },
        clearTransfer() {
            this.$refs.eltTransfer.clear()
        },
        back(){
            this.$router.go(-1);
        },
        import(){
            this.importParVisible=true;
        },
        exportMo() {
            Message.success("正在下载模板");
            window.open("/participants/basic/exportMo", "_parent");
        },
        beforeUpload() {
            this.importDataBtnText = "正在导入";
            this.importDataBtnIcon = "el-icon-loading";
            this.importDataDisabled = true;
        },
        onError(err, file, fileList) {
            this.importDataBtnText = "导入专家";
            this.importDataBtnIcon = "el-icon-upload2";
            this.importDataDisabled = false;
        },
        onSuccess(res) {
            if (res.msg === 'file error') {
                Message.error("文件内容或者格式有误，请不要修改表头，信息按格式填写！")
            } else if (res.msg === 'success') {
                Message.success('数据导入成功！')
            } else {
                this.err_dialogVisible_edit = true;
                this.err_msg = res.obj;
                this.err_title = "以下专家用户名重复：";
                Message.error("上传失败！",res.msg)
            }
            this.importDataBtnText = "导入数据";
            this.importDataBtnIcon = "el-icon-upload2";
            this.importParVisible = false
            this.init();
        },
    }
}
</script>

<style>
</style>
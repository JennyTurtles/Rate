<template>
    <div>
        <div style="display: flex; justify-content: left">
            <div style="width: 100%;text-align: center;font-size: 20px;margin-left: 80px">分配选手和专家</div>
               <div style="margin-left: auto">
                    <el-button icon="el-icon-back" type="primary" @click="back">
                            返回
                    </el-button>
               </div>
        </div>
        <elt-transfer
            style="text-align: center;margin-top: 10px;"
            ref="eltTransfer"
            :show-pagination="true"
            :pagination-call-back="paginationCallBackPar"
            :left-columns="leftColumns"
            :right-columns="rightColumns"
            :title-texts="['未分组选手','已分组选手']"
            :button-texts="['添加','删除']"
            :table-row-key="row => row.name"
            v-model="tableData"
        >
            <!-- 可以使用插槽获取到列信息和行信息，从而进行数据的处理 -->
            <template v-slot:default="{scope}">
                <div>
<!--                    <span>{{scope.row}}</span>-->
                    <span v-if="scope.col.id === 'gender'">{{scope.row.gender === '男' ? '男生' : '女生'}}</span>
                    <span v-else>{{scope.row[scope.col.id]}}</span>
                </div>
            </template>
            <template v-slot:leftCondition="{scope}">
                <el-form-item label="姓名">
                    <el-input v-model="scope.name" placeholder="姓名"></el-input>
                </el-form-item>
                <el-form-item label="年龄">
                    <el-input v-model="scope.age" placeholder="年龄"></el-input>
                </el-form-item>
            </template>
            <template v-slot:rightCondition="{scope}">
                <el-form-item label="名称">
                    <el-input v-model="scope.name" placeholder="名称"></el-input>
                </el-form-item>
            </template>
        </elt-transfer>

        <el-dialog
            title="导入选手到本组"
            :visible.sync="importParVisible"
            width="35%">
            <span style="font-weight:600;">导入新数据</span> 第一步：
            <el-button type="primary" @click="exportCheckbox" icon="el-icon-upload" style="margin-right: 8px">
                下载模板
            </el-button>
            第二步：
            <el-upload
                :show-file-list="false"
                :on-success="onSuccess"
                :on-error="onError"
                :disabled="importDataDisabled"
                style="display: inline-flex;margin-right: 8px"
                action="#"
                :http-request="goImport">
                <el-button :disabled="importDataDisabled" type="primary" :icon="importDataBtnIcon">
                    导入选手
                </el-button>
            </el-upload>
            <span slot="footer" class="dialog-footer">
<!--    <el-button @click="importParVisible = false">取 消</el-button>-->
<!--    <el-button type="primary" @click="importParVisible = false">确 定</el-button>-->
  </span>
        </el-dialog>
        <el-dialog title="选择导入模板的数据列" :visible.sync="dialogVisible_checkbox" width="60%" center>
            <div style="font-size: 17px;">
                导入模板中必须包含身份证号和姓名，以下勾选的列将包含在导入模板中。模板中不包含的列，则导入时将保持数据库中已有信息不变。
                <br/>
                首次导入请注意！如果student表中已有该选手的记录，则手机号、邮箱、属于本单位三列可为空，“属于本单位”列填是或否。用户名密码可以不填写，若不填写第一次导入将默认为编号和手机号，其余必须填写。
            </div><br/>
            <div style="font-size: 16px;margin-left: 15%">基本信息：<br/>
                <el-checkbox label="姓名" key="姓名" v-model="dymatic_list" disabled style="width: 150px">姓名</el-checkbox>
                <el-checkbox label="身份证号码" v-model="dymatic_list" disabled style="width: 150px">身份证号码</el-checkbox>
                <el-checkbox label="编号" v-model="dymatic_list"  style="width: 150px">编号</el-checkbox>
                <el-checkbox label="序号" v-model="dymatic_list"  style="width: 150px">序号</el-checkbox>
                <el-checkbox label="手机号" v-model="dymatic_list"  style="width: 150px">手机号</el-checkbox>
                <el-checkbox label="邮箱" v-model="dymatic_list"  style="width: 150px">邮箱</el-checkbox>
                <el-checkbox label="属于本单位" v-model="dymatic_list"  style="width: 150px">属于本单位</el-checkbox>
                <el-checkbox label="用户名" v-model="dymatic_list"  style="width: 150px">用户名</el-checkbox>
                <el-checkbox label="密码" v-model="dymatic_list"  style="width: 150px">密码</el-checkbox>
            </div><br/>
            <div style="font-size: 16px;margin-left: 15%">信息项：<br/>
                <el-checkbox v-for="item in infoitem_from_back" :key="item.name" :label="item.name" v-model="infoitem" style="width: 150px">
                </el-checkbox>
            </div><br/>
            <div style="font-size: 16px;margin-left: 15%">评分项：<br/>
                <el-checkbox v-for="item in scoreitem_from_back" :key="item.name" :label="item.name" v-model="scoreitem" style="width: 150px">
                </el-checkbox>
            </div>
            <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportMo">下载</el-button>
          <el-button @click="dialogVisible_checkbox = false">关闭</el-button>
        </span>
        </el-dialog>

    </div>
</template>

<script>
import eltTransfer from '../../utils/eltTransfer'
import {Message} from "element-ui";
import axios from "axios";
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
            importDataBtnText: '导入选手',
            scoreitem:[],
            infoitem:[],
            scoreitem_from_back:[],
            infoitem_from_back:[],
            dymatic_list:["姓名","身份证号码"],
            dialogVisible_checkbox: false,
            importParVisible:false,
            activityIDParent:-1,
            activityID:-1,
            groupIDParent:-1,
            groupID:-1,
            mode:-1,
            tableData: [],
            leftColumns: [
                {label: '姓名', id: 'name', width: '120px'},
                {label: '编号', id: 'code', width: '120px'},
                {label: '身份证', id: 'idnumber',}
            ],
            rightColumns:[
                {label: '姓名', id: 'name', },
                {label: '编号', id: 'code', },
            ],
            pars: []
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
        'elt-transfer': eltTransfer
    },
    methods: {
        add(addList){
            for (let i = 0; i < addList.length; i++) {
                addList[i].activityID=this.activityID;
                addList[i].groupID=this.groupID;
            }
            this.postRequest("/participants/basic/addPars",addList).then(res=>{
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
           this.getRequest("/groups/basic/pars?groupID="+this.groupIDParent).then(res=>{ // 获取大组内的选手
               this.pars = res.obj;
               this.clearTransfer();
               this.getRequest("/groups/basic/pars?groupID="+this.groupID).then(res=>{ // 获取当前组内的选手
                   this.tableData = res.obj;
                   for (let i = 0; i < this.tableData.length; i++) {
                       this.$refs.eltTransfer.rightTableData.push(this.tableData[i])
                   }
               })
           })

        },
        paginationCallBackPar(obj) {
            let d = this.pars.filter((item, index) => {
                if (index >= (obj.pageIndex - 1) * obj.pageSize && index < obj.pageIndex * obj.pageSize) {
                    return true;
                }
                return false;
            });
            return new Promise(((resolve, reject) => {
                try {
                    resolve({total: this.pars.length, data: d})
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
        exportCheckbox() {
            let url = "/participants/basic/getItem?activityid="+this.activityID;
            this.getRequest(url).then(resp => {
                this.loading = false;
                if (resp) {
                    this.scoreitem_from_back=resp.extend.ScoreItem;
                    this.infoitem_from_back=resp.extend.InfoItem;
                }
            });
            this.title = "选择导入模板的数据列";
            this.dialogVisible_checkbox=true;
        },
        exportMo() {
            this.dialogVisible_checkbox=false;
            Message.success("正在下载模板");
            let url = "/participants/basic/exportMoPar_Group?activityid="+this.activityID+"&dymatic_list="+this.dymatic_list+"&scoreitem="+this.scoreitem+"&infoitem="+this.infoitem;
            window.open(url, '_parent');
            console.log(url)
        },
        onError(err, file, fileList) {
            this.importDataBtnText = '导入选手';
            this.importDataBtnIcon = 'el-icon-upload2';
            this.importDataDisabled = false;
        },
        onSuccess(res) {
            this.importDataBtnText = '导入选手';
            this.importDataBtnIcon = 'el-icon-upload2';
            this.importDataDisabled = false;
        },
        goImport(file) {
            this.importParVisible=false;
            this.show = true;
            let fd = new FormData();
            let fileName = file.file.name + new Date().getTime();
            fd.append("file", file.file);
            fd.append("key", fileName);
            let url = "/participants/basic/check?groupid="+this.groupID;
            axios.post(url, fd, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            })
                .then((res1) => {
                    if(res1.length===0)
                    {
                        url = "/participants/basic/import?groupid="+this.groupID+"&activityid="+this.activityID+"&insititutionID="+this.user.institutionID;
                        axios.post(url, fd, {
                            headers: {
                                "Content-Type": "multipart/form-data",
                            },
                        }).then((res) => {
                            this.init()
                            this.$message(res.msg);
                        })
                    }
                    else{
                        let newD=[],h=this.$createElement;
                        newD.push(h('span',null,'导入数据中'));
                        for(const i in res1)
                        {
                            newD.push(h('span',null,res1[i]))
                        }
                        newD.push(h('span',null,'，以上列数据会被置空，是否确认继续?'));
                        this.$confirm(h('div',null,newD), '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            url = "/participants/basic/import?groupid="+this.groupID+"&activityid="+this.activityID+"&insititutionID="+this.user.institutionID;
                            axios.post(url, fd, {
                                headers: {
                                    "Content-Type": "multipart/form-data",
                                },
                            }).then((res) => {
                                this.onSuccess(res);
                                this.init();
                                this.$message(res.msg);
                            })
                        })
                    }
                })
                .catch((err) => {
                    console.log(err);
                });
        },
    }
}
</script>

<style>
</style>
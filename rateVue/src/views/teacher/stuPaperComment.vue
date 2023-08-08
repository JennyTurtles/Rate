<template>
    <div>
        <!-- 这里显示学生的姓名和毕业设计的题目 -->
        <div
            style="display: flex; justify-content: flex-start; margin: 15px 0; font-weight: bold;">
            <div>{{ stuName }}({{ stuID }})</div>
            <!-- <div style="margin: 0 auto;">{{ thesis.name }}</div> -->
            <div style="margin-left: 20px;">{{ thesis.name }}</div>
        </div>

        <div style="margin-top: 10px">
            <el-table
                class="table-with-shadow"
                :data="emps"
                stripe="stripe"
                border="border"
                v-loading="loading"
                :header-cell-style="rowClass"
                element-loading-text="正在加载..."
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.12)"
                style="width: 100%">
                <el-table-column align="center" label="操作" width="220px">
                    <template slot-scope="scope">
                        <div
                            style="text-align: center; height: 300px; display: flex; flex-direction: column; justify-content: center;">
                            <div style="margin-bottom: 10px;left">指导期次:
                                <span
                                    style="color: black;font-size:bold;display: inline-block; width: 100px; text-align: left; margin-left: 10px;">
                                    第{{scope.row.num}}次
                                </span>
                            </div>
                            <div style="margin-bottom: 10px;">
                                提交日期:
                                <span
                                    style="display: inline-block; width: 100px; text-align: left; margin-left: 10px;">{{scope.row.dateStu}}</span>
                            </div>
                            <div style="margin-bottom: 10px;">
                                评价日期:
                                <span
                                    style="display: inline-block; width: 100px; text-align: left; margin-left: 10px;color: black;">{{scope.row.dateTea || '待填写'}}</span>
                            </div>
                            <div style="margin-bottom: 10px;left">审核结果:
                                <span
                                    style="display: inline-block; width: 100px; text-align: left; margin-left: 10px;"
                                    :style="scope.row.isPass=='tea_deny' ? {'color':'red'}:{'color':'black'}">
                                    {{scope.row.isPass=="tea_pass" ? "导师通过"
                                    :scope.row.isPass=="tea_deny" ? "导师驳回"
                                    :"待填写"}}</span>
                                <!-- <div :style="{ color: emp.isPass === '驳回' ? 'red' : '' }"> {{ emp.isPass }}
                                </div> -->

                            </div>
                            <div style="display: flex; justify-content: center;">
                                <el-button
                                    @click="showEditEmpView(scope.row)"
                                    style="padding: 4px; margin-right: 10px;"
                                    size="mini"
                                    icon="el-icon-edit"
                                    type="primary"
                                    plain="plain"
                                    v-show="scope.row.isPass === 'tea_pass'||scope.row.isPass === 'tea_deny'?false:true">填写评价</el-button>
                                <el-button
                                    @click="showRefuseEmpView(scope.row)"
                                    style="padding: 4px; margin-right: 10px; margin-left: auto; margin-right: auto;"
                                    size="mini"
                                    type="warning"
                                    icon="el-icon-delete"
                                    plain="plain"
                                    v-show="scope.row.isPass === 'tea_pass'?true:false">驳 回</el-button>
                            </div>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column label="详细信息">
                    <template slot-scope="scope">
                        <div class="table-cell">
                            <p>
                                <strong style="display: inline-block; min-width: 0px; text-align: left;">上期总结：</strong>
                                {{ scope.row.preSum }}</p>

                            <p>
                                <strong style="display: inline-block; min-width: 0px;text-align: left;">下期计划：</strong>
                                {{ scope.row.nextPlan }}</p>
                            <p>
                                <strong style="display: inline-block; min-width: 0px;text-align: left;">导师评价：</strong>
                                {{ scope.row.tutorComment || '待评价' }}
                            </p>
                        </div>
                    </template>
                </el-table-column>

            </el-table>

        </div>

        <!-- 添加评价对话框 -->
        <el-dialog
            :close-on-click-modal="false"
            :title="title"
            :visible.sync="dialogVisible"
            width="65%"
            center="center">
            <el-form
                :hide-required-asterisk="true"
                :label-position="labelPosition"
                label-width="300px"
                :model="emp"
                :rules="rules"
                ref="empForm">
                <el-form-item
                    label="提交次序:"
                    prop="num"
                    label-width="80px"
                    style="margin-left: 20px">
                    <!-- <span class="isMust">*</span> -->
                    <div>
                        {{curIndex}}
                    </div>
                </el-form-item>
                <el-form-item
                    label="提交时间:"
                    label-width="80px"
                    style="margin-left: 20px">
                    <!-- <span class="isMust">*</span> -->
                    <div>
                        {{this.showDateStu}}
                    </div>
                </el-form-item>

                <el-row>
                    <el-form-item
                        prop="dateTea"
                        label="评价时间:"
                        label-width="80px"
                        style="margin-left: 20px">
                        <span class="isMust">*</span>
                        <el-date-picker
                            style="width: 200px;"
                            value-format="yyyy-MM-dd"
                            v-model="emp.dateTea"
                            type="date"
                            placeholder="请选择评价时间"
                            :picker-options="pickerOptions"
                            :default-value="new Date()"></el-date-picker>

                        <el-tooltip
                            effect="light"
                            popper-class="btnitem"
                            :content="prePlan"
                            placement="top"
                            v-if="showTooltip">
                            <span
                                style="display: inline-block; margin-left: 20px; color: #409eff; position: relative;">上期总结
                                <span
                                    style="position: absolute; bottom: 0px; left: 0; right: 0; height: 2px; background-color: #303133; top: 28px; transform: translateY(-1px);"></span>
                            </span>

                        </el-tooltip>
                    </el-form-item>
                </el-row>

                <el-form-item
                    label="是否通过:"
                    prop="isPass"
                    label-width="80px"
                    style="margin-left: 20px">
                    <span class="isMust">*</span>
                    <!-- <div :style="{ color: emp.isPass === '驳回' ? 'red' : '' }"> {{ emp.isPass }}
                    </div> -->
                    <el-select v-model="emp.isPass" placeholder="请选择">
                        <el-option label="通过" value="tea_pass"></el-option>
                        <el-option label="驳回" value="tea_deny"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item
                    label="导师评价:"
                    prop="tutorComment"
                    label-width="80px"
                    style="margin-left: 20px;">
                    <span class="isMust">*</span>
                    <el-input
                        type="textarea"
                        size="medium"
                        style="width: 77%"
                        prefix-icon="el-icon-edit"
                        v-model="emp.tutorComment"
                        placeholder="请输入导师评价"
                        :show-word-limit="true"
                        :rows="3"
                        :maxlength="100"></el-input>

                </el-form-item>

            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button @click.stop.prevent="handleCancel">取 消</el-button>
                <el-button type="primary" @click="doAddEmp()" v-show="addButtonState">提 交</el-button >
            </span>
        </el-dialog>

    </div>
</template>

<script>
    // import { delete } from 'vue/types/umd';
    import axios from "axios";
    import {postRequest1} from "@/utils/api";
    export default {
        name: "stuPaperComment",
        data() {
            return {
                disabledInput: true,
                timer: null,
                select_pubName: [],
                ispubFlag: false,
                ispubShow: false,
                empPaperName: "", //添加论文中的论文名称
                view_point: 0,
                headers: {
                    "Content-Type": "multipart/form-data"
                },
                files: [], //选择上传的文件列表
                urlFile: "", //文件路径
                addButtonState: true, //是否允许添加论文
                operList: [], //每个论文的历史操作记录
                remarksort: [], //对显示的驳回理由做排序
                writer: "", //和输入的作者列表绑定
                options: [], //存储所有刊物对象
                data_picker: "", //选择时间
                ulList: false,
                labelPosition: "left",
                title: "",
                title_show: "",
                importDataBtnText: "导入数据",
                importDataBtnIcon: "el-icon-upload2",
                importDataDisabled: false,
                showAdvanceSearchView: false,
                allDeps: [],
                emps: [],

                loading: false,
                dialogVisible: false,
                dialogVisible_show: false,
                dialogVisible_showInfo: false,

                thesisName: "",
                stuName: "",
                stuID: 0, // 从老师毕设评论页面传来的学生ID
                total: 0, // 现在显示的数据个数
                prePlan: "", // 上期安排
                preDate: new Date(), // 上一条记录的时间
                nextDate: new Date(), // 下一条记录的时间
                thesis: {},
                timeChoose: 0,
                curIndex: 0,
                showTooltip: true,
                pageMonth: new Date().getMonth(),
                showDateStu: null,

                page: 1,
                keyword: "",
                size: 10,
                positions: [],
                publicationName: "", //所属期刊
                publicationID: -1,
                startPage: "",
                endPage: "",

                emp: {
                    id: null,

                    num: null,
                    thesisID: null,
                    preSum: "",
                    nextPlan: "",
                    dateStu: new Date(),
                    dateTea: new Date(),
                    tutorComment: ""
                },
                defaultProps: {
                    children: "children",
                    label: "name"
                },
                rules: {
                    dateTea: [
                        {
                            required: true,
                            message: "请输入评价时间",
                            trigger: "blur"
                        }
                    ],
                    isPass: [
                        {
                            required: true,
                            message: "请选择是否通过",
                            trigger: "blur"
                        }
                    ],
                    tutorComment: [
                        {
                            required: true,
                            message: "请输入导师评价",
                            trigger: "blur",
                            min: 1
                        }
                    ]
                },
                pickerOptions: {
                    disabledDate: (time) => {
                        const date = new Date(time);
                        return this.disabledTime(date);
                    }
                }
            };
        },
        watch: {
            publicationName: {
                handler(val) {
                    //函数抖动
                    this.delayInputTimer(val);
                }
            },
            data_picker: {
                //清除时间input设置为不可输入
                handler(val) {
                    if (!val) {
                        this.disabledInput = true;
                    }
                }
            }
        },
        computed: {
            user() {
                return this.$store.state.currentHr; //object信息
            },
            menuHeight() {
                return this.publicationName.length * 50 > 150
                    ? 150 + "px"
                    : "${this.publicationName.length * 50}px";
            }

        },
        created() {},
        mounted() {
            this.stuID = this.$route.query.keyword;
            this.stuName = this.$route.query.keyname;
            this.initEmps();

        },
        filters: {},
        methods: {
            disabledTime(date) {
                // const today = new Date();
                const sevenDaysAfter = new Date(
                    new Date(this.preDate).getTime() + 7 * 24 * 60 * 60 * 1000
                );
                // console.log(new Date(this.preDate).getTime());
                switch (this.timeChoose) {
                    case 10:
                        return (
                            date.getTime() < new Date(this.preDate).getTime() - 8.64e7 || date.getTime() > new Date(this.nextDate).getTime()
                        );
                    case 11:
                        return (
                            date.getTime() < new Date(this.preDate).getTime() - 8.64e7 || date.getTime() > sevenDaysAfter.getTime()
                        );
                    default:
                        console.log("时间限制出现了其他的问题！请检查");
                        return false;

                }
            },

            handleCancel(event) {
                // 阻止事件冒泡，避免点击其他元素也能关闭对话框
                event.stopPropagation();
                // 阻止默认行为，避免触发其他元素的默认行为
                event.preventDefault();
                // 关闭对话框
                this.dialogVisible = false;
                this.initEmps();
            },

            rowClass() {
                return "background:#b3d8ff;color:black;font-size:13px;text-align:center";
            },

            emptyEmp() {
                this.emp = {
                    dateTea: null,
                    id: null,
                    num: null,
                    preSum: "",
                    nextPlan: "",
                    thesisID: null,
                    tutorComment: null,
                    isPass: ""
                };
            },

            // 驳回按钮
            showRefuseEmpView(data) {

                this.emptyEmp();
                this.emp.num = data.num
                this.emp.dateTea = null
                this.emp.tutorComment = data.tutorComment
                this.emp.studentID = data.stuID
                this.emp.thesisID = data.thesisID
                this.emp.isPass = 'tea_deny'

                this
                    .postRequest1("/paperComment/basic/updateTea", this.emp)
                    .then((resp) => {
                        if (resp) {
                            this.initEmps();
                        }
                    });

            },

            //编辑按钮
            showEditEmpView(data) {
                // this.initPositions();
                this.title = "填写评价";
                this.emp = data;
                this.showDateStu = data.dateStu;
                let sortedEmps = this
                    .emps
                    .slice()
                    .sort((a, b) => a.num - b.num);
                this.sortedEmps = sortedEmps;
                // this.emps.num = data.num; console.log(this.emps);  也就是这个可以获取当前表格中的所有数据
                this.curIndex = data.num

                if (data.num > 1) {
                    this.showTooltip = true;
                    this.prePlan = this
                        .sortedEmps[data.num - 2]
                        .nextPlan;
                    this.preDate = this
                        .sortedEmps[data.num - 1]
                        .dateStu;
                    // 不是第一条记录
                    if (data.num < this.total) {
                        this.timeChoose = 10;
                        this.nextDate = this
                            .sortedEmps[data.num]
                            .dateStu;
                    } else if (data.num = this.total) {
                        this.timeChoose = 11;

                    }
                } else {
                    this.showTooltip = false;
                    this.prePlan = "";
                    // 是第一条记录
                    this.preDate = this
                        .sortedEmps[0]
                        .dateStu;
                    if (this.total > 1) {
                        this.timeChoose = 10;
                        this.nextDate = this
                            .sortedEmps[1]
                            .dateStu;
                    } else if (this.total == 1) {
                        this.timeChoose = 11;

                    }

                }

                this.dialogVisible = true;
            },
            deleteEmp(data) {
                //点击删除按钮
                if (confirm("此操作将永久删除【第" + data.num + "条记录】, 是否继续?")) {
                    axios
                        .delete("/paperComment/basic/remove/" + data.num + "/" + data.thesisID)
                        .then((resp) => {
                            if (resp) {
                                console.log(resp);
                                this.dialogVisible = false;
                                this.initEmps();
                            }
                        });
                }
            },
            doAddEmp() {
                //确定是添加记录还是新增记录 emptyEmp中没有将id设置为空 所以可以判断
                var empdata = this.emp;
                this.emptyEmp();
                this
                    .$refs["empForm"]
                    .validate((valid) => {
                        if (valid) {
                            this.emp.num = empdata.num
                            this.emp.dateTea = empdata.dateTea
                            this.emp.tutorComment = empdata.tutorComment
                            this.emp.studentID = this.stuID
                            this.emp.thesisID = this.thesis.id
                            this.emp.isPass = empdata.isPass


                            const _this = this;
                            this
                                .postRequest1("/paperComment/basic/updateTea", _this.emp)
                                .then((resp) => {
                                    if (resp) {
                                        this.dialogVisible = false;
                                        this.initEmps();
                                    }
                                });

                        }
                    });

            },

            initEmps() {
                this.loading = true;
                console.log(this.stuID);
                let getTIDPromise = new Promise((resolve, reject) => {
                    this
                        .getRequest("/paperComment/basic/getThesis?stuID=" + this.stuID)
                        .then((resp) => {
                            if (resp) {
                                this.thesis = resp.data;
                                resolve(this.thesis);
                            } else {
                                reject(Error("Error getting thesis"));
                            }
                        });
                });

                getTIDPromise.then((tID) => {
                    let url = "/paperComment/basic/getAllComment?thesisID=" + this.thesis.id;
                    // 通过学生ID获取其毕业设计的ID
                    this
                        .getRequest(url)
                        .then((resp) => {
                            this.loading = false;
                            if (resp) {
                                this.emps = resp.data;
                                this.total = resp.data.length;
                            }
                        });

                });
            }
        }
    };
</script>

<style>
    .table-with-shadow {
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
    }

    .select_div_input {
        /*margin-left:3px;*/
        width: 80%;
        height: 32px;
        position: relative;
        display: inline-block;
    }
    .select_div {
        border: 0.5px solid lightgray;
        border-radius: 3px;
        margin-top: 5px;
        font-size: 14px;
        position: absolute;
        background-color: #fff;
        z-index: 999;
        overflow: hidden;
        width: 90%;
        cursor: pointer;
    }
    .select_div_div:hover {
        background-color: lightgray;
    }
    .select_div_div {
        padding-bottom: 2px;
        /*padding-top: 7px;*/
        padding-left: 12px;
        width: 100%;
    }
    /*input[type=text]::placeholder {*/
    /*  color:lightgrey;*/
    /*}*/
    /*input:focus{*/
    /*  border:1px solid lightblue;*/
    /*}*/
    .showInfo_dialog .el-form-item {
        margin-bottom: 5px;
    }
    .isMust {
        position: absolute;
        color: #f56c6c;
        top: 2px;
        left: -100px;
    }

    .el-form-item label {
        text-align: justify;
    }

    /* 可以设置不同的进入和离开动画 */
    /* 设置持续时间和动画函数 */
    #selectItem ul {
        list-style: none;
        /* height: 100px; */
        width: 200px;
        /* background: red; */
        /* border: 1px solid #eee; */
        /* visibility: hidden; */
    }
    #selectItem ul li {
        height: 28px;
        line-height: 28px;
        padding-left: 10px;
        cursor: pointer;
    }
    .slide-fade-enter-active {
        transition: all 0.8s ease;
    }

    .slide-fade-leave-active {
        transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
    }
    /* .slide-fade-leave-active for below version 2.1.8 */
    .slide-fade-enter,
    .slide-fade-leave-to {
        transform: translateX(10px);
        opacity: 0;
    }
    div::-webkit-scrollbar {
        /* 隐藏默认的滚动条 */
        -webkit-appearance: none;
    }
    div::-webkit-scrollbar:vertical {
        /* 设置垂直滚动条宽度 */
        width: 6px;
    }

    /* 这里不需要用到这个 */
    /* div::-webkit-scrollbar:horizontal{ */
    /* 设置水平滚动条厚度 */
    /* height: 2px; */
    /* } */

    div::-webkit-scrollbar-thumb {
        /* 滚动条的其他样式定制，注意，这个一定也要定制，否则就是一个透明的滚动条 */
        border-radius: 8px;
        border: 3px solid rgba(255, 255, 255, 0.4);
        background-color: rgba(0, 0, 0, 0.5);
    }
</style>
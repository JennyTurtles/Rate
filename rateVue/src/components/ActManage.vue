<template>
  <div>
    <div>
      <div
          style="display: flex; justify-content: space-between; margin: 15px 0"
      >
        <div>
          <el-input
              placeholder="请输入活动名称进行搜索，可以直接回车搜索..."
              prefix-icon="el-icon-search"
              clearable
              @clear="searchEmps"
              style="width: 350px; margin-right: 10px"
              v-model="keyword"
              @keydown.enter.native="searchEmps"
              :disabled="showAdvanceSearchView"
          ></el-input>
          <el-button
              icon="el-icon-search"
              type="primary"
              @click="searchEmps"
              :disabled="showAdvanceSearchView"
          >
            搜索
          </el-button>
          <el-button
              icon="el-icon-refresh"
              type="primary"
              @click="initEmps"
              :disabled="showAdvanceSearchView"
          >
            重置
          </el-button>
          <el-button type="primary" icon="el-icon-plus" @click='showAddEmpView' v-show="mode === 'admin' || mode === 'adminSub'">
            添加活动
          </el-button>

          <span style="margin-left: 20px" v-show="mode === 'secretarySub'">当前管理的是： {{actName}} {{groupName}} </span>
        </div>
        <div style="margin-left: auto">
          <el-button
              v-show="mode === 'adminSub' || mode === 'secretarySub'"
              type="primary"
              icon="el-icon-arrow-left"
              @click="back">
            返回
          </el-button>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="emps"
          stripe
          border
          v-loading="loading"
          :header-cell-style="rowClass"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%"
      >
        <el-table-column type="selection" width="35px"></el-table-column>

        <el-table-column
            prop="id"
            fixed
            align="center"
            label="编号"
            width="50"
        >
        </el-table-column>
        <el-table-column
            prop="name"
            fixed
            align="left"
            label="活动名称"
        >
        </el-table-column>
        <el-table-column
            prop="startDate"
            label="开始时间"
            align="center"
            width="130"
        >
        </el-table-column>
        <el-table-column
            prop="scoreItemCount"
            label="评分项数"
            align="center"
            width="80"
            v-if="mode==='admin'"
        >
        </el-table-column>
        <!-- width="70" -->
        <el-table-column
            prop="score"
            label="总分"
            align="center"
            width="75"
        >
        </el-table-column>
        <el-table-column
            prop="groupName"
            label="组名"
            align="center"
            width="75"
            v-if="mode==='secretary'"
        >
        </el-table-column>
        <el-table-column align="center" width="650" label="操 作">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView_show(scope.row)"
                style="padding: 4px"
                size="mini"
            >查看详情
            </el-button
            >
            <el-button
                @click="showEditEmpView(scope.row)"
                v-show="mode==='admin' || mode==='adminSub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-edit"
                type="primary"
                plain
            >编辑
            </el-button
            >
            <el-button
                v-show="scope.row.isShowPermissionBtn"
                @click="initAdminListofPermission(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >活动授权
            </el-button
            >
            <el-button
                @click="showScoreItem(scope.row)"
                v-show="(mode==='admin' || mode==='adminSub') && scope.row.haveSub!==1"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >评分项设置
            </el-button
            >
            <el-button
                @click="showScoreItem(scope.row)"
                v-show="(mode==='secretary' || mode==='secretarySub') && scope.row.haveSub!==1"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >评分项查看
            </el-button
            >
            <el-button
                @click="showInfoItem(scope.row)"
                v-show="mode==='admin' || mode==='adminSub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >信息项设置
            </el-button
            >
            <el-button
                @click="showInfoItem(scope.row)"
                v-show="mode==='secretary' || mode==='secretarySub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-search"
                type="primary"
                plain
            >信息项查看
            </el-button
            >
            <el-button
                @click="showTotalItem(scope.row)"
                v-show="mode==='admin' || mode==='adminSub'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-tickets"
                type="primary"
                plain
            >成绩查看设置
            </el-button
            >
            <el-button
                @click="showGroupmanagement(scope.row)"
                v-show="mode !== 'secretary' && mode !== 'secretarySub' && mode !== 'adminSub'|| scope.row.requireGroup === true"
                style="padding: 4px"
                size="mini"
                icon="el-icon-s-operation"
                type="primary"
                plain
            >分组管理
            </el-button
            >
            <el-button
                @click="showInsertmanagement(scope.row)"
                v-show="mode==='admin'"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >选手管理
            </el-button
            >
              <el-button
                      v-show="mode === 'secretary'"
                      @click="showParticipants(scope.row)"
                      style="padding: 4px"
                      size="mini"
                      icon="el-icon-tickets"
                      type="primary"
                      plain
              >选手管理
              </el-button
              >
             <el-button
                      @click="showGroups(scope.row)"
                      style="padding: 4px"
                      size="mini"
                      icon="el-icon-tickets"
                      type="primary"
                      v-show="mode==='admin' || mode==='secretary'"
                      plain
              >专家管理
              </el-button
              >
            <el-button
                @click="showScore(scope.row)"
                v-show="scope.row.haveSub !== 1"
                style="padding: 4px"
                size="mini"
                icon="el-icon-plus"
                type="primary"
                plain
            >分数统计
            </el-button
            >
              <el-button
                      @click="assignPE(scope.row)"
                      v-show="mode === 'secretarySub' && scope.row.requireGroup === false"
                      style="padding: 4px"
                      size="mini"
                      icon="el-icon-tickets"
                      type="primary"
                      plain
              >分配选手和专家
              </el-button
              >
            <el-button
                @click="exportEx(scope.row)"
                :loading="loading"
                v-show="scope.row.haveSub !== 1"
                style="padding: 4px"
                size="mini"
                icon="el-icon-download"
                type="primary"
                plain
            >{{text}}导出专家打分
            </el-button
            >
            <el-button
                @click="showFinalScore(scope.row)"
                :loading="loading"
                style="padding: 4px"
                size="mini"
                icon="el-icon-search"
                type="primary"
                plain
            >查看选手成绩
            </el-button
            >
              <el-button
                      @click="showSubActivity(scope.row)"
                      style="padding: 4px"
                      size="mini"
                      icon="el-icon-plus"
                      type="primary"
                      plain
                      v-show="mode !== 'secretarySub' && mode !== 'adminSub' && scope.row.haveSub === 1"
              >子活动管理
              </el-button
              >
              <el-button
                      @click="exportGradeForm(scope.row)"
                      style="padding: 4px"
                      size="mini"
                      icon="el-icon-download"
                      type="primary"
                      plain
                      v-show="scope.row.haveSub === 1"
              >导出成绩评定表
              </el-button
              >
            <el-button
                @click="endEmp(scope.row)"
                v-show="mode==='admin'|| mode==='adminSub'"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-circle-close"
                plain
                :disabled="scope.row.status=='close'"
            >{{ scope.row.status == 'open' ? '结束活动' : '已结束' }}
            </el-button
            >
            <el-button
                @click="deleteEmp(scope.row)"
                v-show="mode==='admin' || mode==='adminSub'"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
            >删除
            </el-button
            >
          </template>
        </el-table-column>
        <el-table-column
            v-if="mode === 'adminSub'"
            prop="group"
            label="是否分组"
            align="center"
            width="75"
        >
            <template slot-scope="scope">
                <el-checkbox v-model="scope.row.requireGroup"
                             @change="changeCheckGroup(scope.row)"></el-checkbox>
            </template>
        </el-table-column>
      </el-table>

      <div style="display: flex; justify-content: flex-end; margin: 10px 0">
        <el-pagination
            background
            @current-change="currentChange"
            @size-change="sizeChange"
            layout="sizes, prev, pager, next, jumper, ->, total, slot"
            :total="total"
        >
        </el-pagination>
      </div>
    </div>

    <el-dialog :title="title" :visible.sync="dialogVisible" width="30%" center>
      <el-form
          :label-position="labelPosition"
          label-width="100px"
          :model="emp_edit"
          :rules="rules"
          ref="empForm"
      >
        <el-form-item label="活动名称:" prop="name">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp_edit.name"
              placeholder="请输入活动名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="开始时间:" prop="startDate">
          <div class="block">
            <el-date-picker
                v-model="emp_edit.startDate"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期和时间">
            </el-date-picker>
          </div>
        </el-form-item>
        <el-form-item label="可见时间:" prop="visibleDate">
          <div class="block">
            <el-checkbox v-model="visibleDateSelected">不限</el-checkbox>
            <el-date-picker
                :disabled="visibleDateSelected"
                v-model="emp_edit.visibleDate"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期和时间">
            </el-date-picker>
          </div>
        </el-form-item>
        <el-form-item label="可进入时间:" prop="enterDate">
          <div class="block">
            <el-checkbox v-model="enterDateSelected">不限</el-checkbox>
            <el-date-picker
                :disabled="enterDateSelected"
                v-model="emp_edit.enterDate"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期和时间">
            </el-date-picker>
          </div>
        </el-form-item>
        <el-form-item label="备 注: " prop="comment">
          <el-input
              type="textarea"
              :rows="2"
              v-model="emp_edit.comment"
              placeholder="活动备注example：关于xxx的活动。备注信息将显示在专家评分表的活动标题下方。"
          >
          </el-input>
        </el-form-item>
        <el-form-item label="存在子活动: " v-show="mode === 'admin'">
            <el-checkbox v-model="haveSub"></el-checkbox>
        </el-form-item>
        <el-form-item label="是否写评语: ">
            <el-checkbox v-model="haveComment"></el-checkbox>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddEmp">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
        :title="title_show"
        :visible.sync="dialogVisible_show"
        width="25%"
        center
    >
      <el-form
          :label-position="labelPosition"
          label-width="80px"
          :model="emp"
          :rules="rules"
          ref="empForm"
          style="margin-left: 60px"
      >
        <el-form-item label="分组数:" v-show="mode === 'admin'" prop="groupCount">
          <span>{{ emp.groupCount }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="专家数:" prop="expertCount">
          <span>{{ emp.expertCount }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="选手数:" prop="participantCount">
          <span>{{ emp.participantCount }}</span
          ><br/>
        </el-form-item>
        <el-form-item label="备 注:">
          <span>{{ emp.comment }}</span>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible_show = false"
        >关 闭</el-button
        >
      </span>
    </el-dialog>
    <el-dialog :visible.sync="dialogActivityPermission" width="70%" center @close="closeDialogOfAddPermission">
      <div style="font-size: 17px;margin-bottom: 8px">将此活动授权给以下管理员：</div>
      <el-table @selection-change="handleSelectionChange" :data="currentInstitutionAdminList" ref="addPermissionTable" row-key="id"
          :header-cell-style="{background:'#E6E6FA'}">
        <el-table-column type="selection" width="35px" :reserve-selection="true" :selectable="permissionSelectable"></el-table-column>
        <el-table-column label="姓名" prop="name"></el-table-column>
        <el-table-column label="电话" prop="phone"></el-table-column>
      </el-table>
      <div style="margin-top: 10px;text-align:right">
        <el-pagination @size-change="dialogAddTeaPermissionSizeChange"
                       @current-change="dialogAddTeaPermissionPageChange"
                       :current-page="dialogAddTeaPermissionPage"
                       :page-size="dialogAddTeaPermissionSize" layout="total, sizes, prev, pager, next, jumper"
                       :total="dialogAddTeaPermissionTotal"
                       :page-sizes="[20,20,20,20,20]"
                       background
        >
        </el-pagination>
      </div>
      <span slot="footer">
        <el-button @click="doAddAdminPermission" type="primary">确定</el-button>
        <el-button @click="closeDialogOfAddPermission">取消</el-button>
      </span>
    </el-dialog>
    <el-dialog title="成绩评定表导出设置" :visible.sync="exportGradeFormVisible" width="35%">
          <el-form
                  label-position="left"
                  label-width="120px"
                  :model="gradeForm"
          >
              <el-divider>评语设置</el-divider>
              <el-form-item label="指导教师评语:">
                  <el-select
                     style="width: 100%"
                     v-model="gradeForm.instructorCommentActID"
                     placeholder="请选择对应的子活动"
                  >
                    <el-option
                      v-for="item in subActs"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="评阅教师评语:" prop="startDate">
                  <el-select
                     style="width: 100%"
                     v-model="gradeForm.reviewCommentActID"
                     placeholder="请选择对应的子活动">
                     <el-option
                       v-for="item in subActs"
                       :key="item.id"
                       :label="item.name"
                       :value="item.id">
                     </el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="答辩评语:" prop="startDate">
                 <el-select
                    style="width: 100%"
                    v-model="gradeForm.defenseCommentActID"
                    placeholder="请选择对应的子活动">
                    <el-option
                      v-for="item in subActs"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                 </el-select>
              </el-form-item>
              <el-divider>评分项设置</el-divider>
              <el-form-item label="指导教师评分项:" prop="startDate">
                  <el-select
                     style="width: 80%"
                     v-model="gradeForm.instructorScoreItemsActID"
                     placeholder="请选择对应的子活动"
                     @change="gradeForm.instructorScoreItems=[]">
                      <el-option
                              v-for="item in subActs"
                              :key="item.id"
                              :label="item.name"
                              :value="item.id">
                      </el-option>
                  </el-select>
                  <el-button type="success" style="margin-left: 10px" @click="innerDialogType='指导教师评分项';getScoreItems(gradeForm.instructorScoreItemsActID)">设置</el-button>
              </el-form-item>
              <el-form-item label="评阅教师评分项:" prop="startDate">
                  <el-select
                    style="width: 80%"
                    v-model="gradeForm.reviewScoreItemsActID"
                    placeholder="请选择对应的子活动"
                    @change="gradeForm.reviewScoreItems=[]">
                      <el-option
                         v-for="item in subActs"
                         :key="item.id"
                         :label="item.name"
                         :value="item.id">
                      </el-option>
                  </el-select>
                  <el-button type="success" style="margin-left: 10px" @click="innerDialogType='评阅教师评分项';getScoreItems(gradeForm.reviewScoreItemsActID)">设置</el-button>
              </el-form-item>
              <el-form-item label="答辩小组评分项:" prop="startDate">
                  <el-select
                    style="width: 80%"
                    v-model="gradeForm.defenseScoreItemsActID"
                    placeholder="请选择对应的子活动"
                    @change="gradeForm.defenseScoreItems=[]">
                      <el-option
                         v-for="item in subActs"
                         :key="item.id"
                         :label="item.name"
                         :value="item.id">
                      </el-option>
                  </el-select>
                  <el-button type="success" style="margin-left: 10px" @click="innerDialogType='答辩小组评分项';getScoreItems(gradeForm.defenseScoreItemsActID)">设置</el-button>
              </el-form-item>
          </el-form>

          <span slot="footer" class="dialog-footer">
        <el-button @click="exportGradeFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="goExportGradeForm();exportGradeFormVisible = false">下 载</el-button>
      </span>
      </el-dialog>
    <el-dialog
        width="80%"
        :title="innerDialogType"
        :visible.sync="innerVisible"
        append-to-body>
        <el-table :data="scoreItemSelected" stripe style="width: 100%">
            <el-table-column label="评分项名">
                <template slot-scope="scope">
                <el-select
                   style="width: 100%"
                   v-model="scope.row.id"
                   placeholder="请选择评分项">
                    <el-option
                       v-for="item in scoreItems"
                       :key="item.id"
                       :label="item.name"
                       :value="item.id">
                    </el-option>
                </el-select>
                </template>
            </el-table-column>
            <el-table-column prop="coef" label="折合系数" width="100px">
                <template slot-scope="scope">
                    <el-input v-model="scope.row.coef"></el-input>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="80px">
                <template slot-scope="scope">
                    <el-button type="danger" @click="deleteRow(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-button type="success" style="margin-top: 10px" @click="scoreItemSelected.push({})">新增</el-button>
        <span slot="footer" class="dialog-footer">
<!--            <el-button @click="innerVisible = false;scoreItemSelected=[]">取 消</el-button>-->
            <el-button type="primary" @click="innerVisible = false;confirmScoreItem()">确 定</el-button>
        </span>
      </el-dialog>
  </div>
</template>

<script>
import {Message} from "element-ui";
import fa from "element-ui/src/locale/lang/fa";
import axios from "axios";

export default {
  name: "ActManage",
  props:["mode","activityID","actName","groupName","groupID"], // 四个地方复用组件
  data() {
    return {
      visibleDateSelected:true,
      enterDateSelected:true,
      enterDateOptions: {},
      visibleDateOptions: {},
      currentActivity:{},
      dialogAddTeaPermissionPage:1,//给老师授权对话框中的分页控制
      dialogAddTeaPermissionSize:20,
      dialogAddTeaPermissionTotal:0,
      selectedAddAdminListAllPage:[],//所有管理员名单不考虑分页
      selectedAddAdminList:[],//选择的需要授权管理员的列表---当前页
      currentInstitutionAdminList:[],//当前单位下的所有管理员
      scoreItems:[],
      dialogActivityPermission:false,//控制对话框
      activityPermissionOfAdmin:[],//选择授权的管理员名单
      scoreItemSelected:[[]],
      innerDialogType: '',
      innerVisible:false,
      subActs:[],
      haveSub:false,
      haveComment:false,
      startDate: '',
      experts:'',
      participates:'',
      labelPosition: "left",
      title: "",
      text: "",
      title_show: "",
      importDataBtnText: "导入数据",
      importDataBtnIcon: "el-icon-upload2",
      importDataDisabled: false,
      showAdvanceSearchView: false,
      allDeps: [],
      emps: [],
      emp_edit:{},
      gradeForm:{
          instructorCommentActID: '',
          reviewCommentActID: '',
          defenseCommentActID: '',
          instructorScoreItemsActID: '',
          reviewScoreItemsActID: '',
          defenseScoreItemsActID: '',
          instructorScoreItems: [],
          reviewScoreItems: [],
          defenseScoreItems: [],
          groupID: -1,
      },
      loading: false,
      dialogVisible: false,
      exportGradeFormVisible: false,
      dialogVisible_show: false,
      total: 0,
      page: 1,
      keyword: "",
      size: 10,
      positions: [],
      emp: {
        id: null,
        institutionID: null,
        name: null,
        startDate: "",
        enterDate: "",
        visibleDate: "",
        scoreItemCount: "0",
        score: "100",
        groupCount: "0",
        expertCount: "0",
        participantCount: "0",
        comment: "javaboy",
        parentId: null,
        requireGroup: true,
        isShowPermissionBtn:false
      },
      defaultProps: {
        children: "children",
        label: "name",
      },
      rules: {
        name: [{required: true, message: "请输入活动名", trigger: "blur"}],
        startDate: [
          {required: true, message: "请输入活动时间", trigger: "blur"},
        ],
        scoreItemCount: [
          {
            required: true,
            type: "number",
            message: "请输入正确数据",
            trigger: "blur",
            transform: (value) => Number(value),
          },
        ],
        comment: [{required: true, message: "请输入备注", trigger: "blur"}],
      },
    };
  },
  computed: {
    fa() {
        return fa
    },
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
  },
  created() {
  },
  mounted() {
    this.isGroup = this.$route.query.isGroup;
    this.initEmps();
  },
  methods: {
    permissionSelectable(raw,index){//活动授权对话框中用于控制创建者不可把自己的权限取消
      if(this.currentActivity.creatorID == raw.id) return false
      else return true
    },
    closeDialogOfAddPermission(){
      this.dialogActivityPermission = false
      this.dialogAddTeaPermissionPage = 1
      this.currentInstitutionAdminList = []
    },
    doAddAdminPermission(){//点击对话框中的确定按钮
      let url = '/activitygrant/basic/changePermissionList'
      let data = [];
      this.selectedAddAdminList.map((item) => {
        let temp = {
          adminID:item.id,
          activityID:this.currentActivity.id,
          institutionID:this.user.institutionID
        }
        data.push(temp)
      })
      this.postRequest(url,data).then((response) => {
        if(response){
          if(response.status === 200){
            this.$message.success(response.msg)
            this.closeDialogOfAddPermission()
          }
        }
      })
    },
    dialogAddTeaPermissionPageChange(val){
      this.dialogAddTeaPermissionPage = val
      this.activityPermission(this.currentActivity)
    },
    dialogAddTeaPermissionSizeChange(val){
      this.dialogAddTeaPermissionSize = val
      this.activityPermission(this.currentActivity)
    },
    handleSelectionChange(selection){//选择某行数据调用的函数 selection - 存储当前被选中的所有行数据
      this.selectedAddAdminList = selection
    },
    confirmScoreItem(){
        if (this.innerDialogType === '指导教师评分项'){
            this.gradeForm.instructorScoreItems = this.scoreItemSelected
        }else if (this.innerDialogType === '评阅教师评分项'){
            this.gradeForm.reviewScoreItems = this.scoreItemSelected
        }else
            this.gradeForm.defenseScoreItems = this.scoreItemSelected
    },
    deleteRow(row){
        const index = this.scoreItemSelected.indexOf(row);
        if (index !== -1) {
            this.scoreItemSelected.splice(index, 1);
        }
    },
    getScoreItems(id){
        if (id === ""){
            // 弹出提示框
            Message({
                message: '请先选择子活动',
                type: 'warning'
            })
            return
        }
        this.innerVisible=true
        if (this.innerDialogType === '指导教师评分项'){
            this.scoreItemSelected = this.gradeForm.instructorScoreItems
        }else if (this.innerDialogType === '评阅教师评分项'){
            this.scoreItemSelected = this.gradeForm.reviewScoreItems
        }else
            this.scoreItemSelected = this.gradeForm.defenseScoreItems

        if (this.scoreItemSelected.length === 0){
            this.scoreItemSelected.push({})
        }
        this.getRequest("/scoreItem/basic/getall?activityID="+id).then(res=>{
            if(res.status === 200){
                this.scoreItems = res.obj;
            }
        })
    },
    //活动进行授权给其他的管理员，在后端处理需要添加类字段，所以纯前端处理
    initAdminListofPermission(data){//点击按钮进行初始化本单位下所有管理员的数据列表，并做属性赋值，用于在界面做回显
      this.currentActivity = data
      this.dialogActivityPermission = true
      let url = '/system/admin/selectAdminOfCurrentInstitution?dialogAddTeaPermissionPage=' + this.dialogAddTeaPermissionPage +
          '&dialogAddTeaPermissionSize=' + this.dialogAddTeaPermissionSize + '&activityID=' + data.id + '&institutionID=' + this.user.institutionID
      this.getRequest(url).then((resp)=>{
        if(resp){
          if(resp.code == 200){
            let aaAllList = resp.extend.allAdmList
            let aa = resp.extend.aa
            for(let i = 0;i < aaAllList.length;i ++){
              for(let j = 0;j < aa.length; j++){
                if(aaAllList[i].id == aa[j].adminID){//说明本单位下的该管理员已经有了这个活动的权限,使用响应式数据isPermission做判断
                  this.$set(
                      aaAllList[i],
                      "isPermission",
                      true
                  )
                  break;
                }else {
                  this.$set(
                      aaAllList[i],
                      "isPermission",
                      false
                  )
                }
              }
            }
            this.selectedAddAdminListAllPage = aaAllList
            this.activityPermission(data)//初始化本页数据
          }
        }
      })
    },
    activityPermission(data){//分页+selection勾选 有点复杂
      this.currentActivity = data
      let url = '/system/admin/selectAdminOfCurrentInstitution?dialogAddTeaPermissionPage=' + this.dialogAddTeaPermissionPage +
          '&dialogAddTeaPermissionSize=' + this.dialogAddTeaPermissionSize + '&activityID=' + data.id + '&institutionID=' + this.user.institutionID
      this.getRequest(url).then((resp)=>{
        if(resp){
          if(resp.code == 200){
            let adm = resp.extend.adm
            let aa = resp.extend.aa
            for(let i = 0;i < adm[0].length;i ++){//adm是本页需要展示的数据，同样做响应式属性赋值
              for(let j = 0;j < aa.length; j++){
                if(adm[0][i].id == aa[j].adminID){
                  this.$set(
                      adm[0][i],
                      "isPermission",
                      true
                  )
                  break;
                }else {
                  this.$set(
                      adm[0][i],
                      "isPermission",
                      false
                  )
                }
              }
            }
            this.currentInstitutionAdminList = adm[0]
            this.dialogAddTeaPermissionTotal = adm[1]//找到本单位下的管理员的总数量
            this.$nextTick(()=>{//用于处理勾选状态
              for(let i = 0;i < this.currentInstitutionAdminList.length;i ++){//已经有权限的,加载数据后默认勾选
                for(let j = 0;j < this.selectedAddAdminListAllPage.length;j ++){
                  if(this.selectedAddAdminListAllPage[j].id == this.currentInstitutionAdminList[i].id){//需要在所有数据在找到本页的数据，id标识
                    if(this.selectedAddAdminListAllPage[j].isPermission){//初始化数据中需要勾选上的某行
                      this.$set(
                          this.currentInstitutionAdminList[i],
                          "isPermission",
                          true
                      )
                      this.$refs.addPermissionTable.toggleRowSelection(this.currentInstitutionAdminList[i],true)
                    }
                    break;
                  }
                }
              }
            })
          }
        }
      })
    },
    assignPE(data) {
        const _this = this;
        _this.$router.push({
            path: "/Expert/EassignPE",
            query: {
                activityIDParent: this.$route.query.id,
                activityID: data.id,
                groupIDParent: this.$route.query.groupID, // 这里有问题
                groupID: data.groupID,
                mode:this.mode
            }
        })
    },
      // 转换为map
      scoreItemsConvert(scoreItems){
          var res = {}
          for (var i in scoreItems) {
              const scoreItem = scoreItems[i]
              if (typeof scoreItem.id === 'undefined' || scoreItem.id === '' || typeof scoreItem.coef === 'undefined' || scoreItem.coef === '')
                  continue
              res[scoreItem.id] = scoreItem.coef
          }
          return res
      },
      goExportGradeForm(row){
          this.gradeForm.teacherID = this.user.id
          this.gradeForm.instructorScoreItems = this.scoreItemsConvert(this.gradeForm.instructorScoreItems)
          this.gradeForm.reviewScoreItems = this.scoreItemsConvert(this.gradeForm.reviewScoreItems)
          this.gradeForm.defenseScoreItems = this.scoreItemsConvert(this.gradeForm.defenseScoreItems)
          axios({url:"/system/Experts/exportGradeForm",method:'post',data:this.gradeForm,
              headers: {'Content-Type': 'application/json'},
              responseType: 'blob'}).then(res=>{
              if (new Blob([res]) !== null)
                  this.$message({type: 'success', message: '导出成绩评定表成功!'});
              else
                  this.$message({type: 'error', message: '导出成绩评定表失败!'});
              const url = window.URL.createObjectURL(new Blob([res]));
              const link = document.createElement('a');
              link.href = url;
              link.setAttribute('download', this.gradeForm.groupName + '成绩评定表.zip');
              document.body.appendChild(link);
              link.click();
          })
      },
    changeCheckGroup(row){
      this.postRequest("/activities/basic/changeRequireGroup?activityID="+row.id+"&requireGroup="+(row.requireGroup?1:0)).then(res=>{
        if(res.status === 200){
            this.$message({
                type: 'success',
                message: '修改成功!'
            });
        }else{
            this.$message({
                type: 'error',
                message: '修改失败!'
            });
        }
      })
    },
    rowClass() {
      return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
    },
    /** 查询角色列表 */
    onError(err, file, fileList) {
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
    },
    onSuccess(response, file, fileList) {
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-upload2";
      this.importDataDisabled = false;
      this.initEmps();
    },
    beforeUpload() {
      this.importDataBtnText = "正在导入";
      this.importDataBtnIcon = "el-icon-loading";
      this.importDataDisabled = true;
    },
    emptyEmp() {
      this.emp = {
        id: null,
        startDate: null,
        name: "",
        scoreItemCount: "0",
        comment: "活动备注example：关于xxx的活动。备注信息将显示在专家评分表的活动标题下方。",
      };
    },
    exportEx(data){
      this.loading=true;
      this.text='正在';
      Message.success("正在导出");
      let url = '/participants/basic/export?activityID=' + data.id;
      window.open(url, "_parent");
      this.loading=false;
      this.text='';
    },
    showEditEmpView(data) {
      this.title = "编辑活动信息";
      this.emp = data;
      this.haveSub = data.haveSub === 1;
      this.haveComment = data.haveComment === 1;
      this.dialogVisible = true;
      if(data.visibleDate) this.visibleDateSelected = false
      else this.visibleDateSelected = true
      if(data.enterDate) this.enterDateSelected = false
      else this.enterDateSelected = true
      this.emp_edit = JSON.parse(JSON.stringify(data));
    },
    showEditEmpView_show(data) {
      this.title_show = "显示详情";
      this.emp = data;
      this.dialogVisible_show = true;
    },
    showScore(data){
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/score",
        query: {
          keywords: data.id,
          keyword_name: data.name,
          mode:this.mode,
          backID:this.activityID,
        },
      });
    },
    deleteEmp(data) {
      data.institutionID = this.user.institutionID;
      this.getRequest("/activities/basic/check?id=" + data.id).then(res => {
        if (res == true) {
          this.$confirm(
              "此操作将永久删除【" + data.name + "】, 是否继续?",
              "提示",
              {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
              }
          ).then(() => {
            this.postRequest("/activities/basic/predelete", data).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          });
        } else {
          this.$confirm(
              "尚存在与活动" + data.name + "相关的信息，所以放入回收站，如要永久删除，请先删除分组和评分项等相关信息，是否放入回收站?",
              "提示",
              {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
              }
          ).then(() => {
            this.postRequest("/activities/basic/predelete", data).then((resp) => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          });
        }
      })
    },
    endEmp(data) {
      data.institutionID = this.user.institutionID;
      this.$confirm(
          "此操作将永久停止活动【" + data.name + "】, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
      ).then(() => {
        this.postRequest("/activities/basic/end", data).then((resp) => {
          if (resp) {
            this.dialogVisible = false;
            this.initEmps();
          }
        });
      });
    },
    doAddEmp() {
      this.emp = this.emp_edit
      if(this.mode === 'adminSub')
         this.emp.parentID = this.activityID;
      if(this.visibleDateSelected) {//不限
        this.emp.visibleDate = null
      }else {
        if(this.emp.visibleDate !== '' && this.emp.visibleDate != null){
          if(this.emp.enterDate !== '' && this.emp.enterDate != null){
            if(this.emp.visibleDate > this.emp.enterDate){
              this.$message.warning('可见时间应不大于进入时间!')
              return
            }
          }
          if(this.emp.visibleDate > this.emp.startDate){
            this.$message.warning('可见时间应不大于开始时间!')
            return
          }
        }
        this.emp.visibleDate = this.dateFormatFunc(this.emp.visibleDate)
      }
      if(this.enterDateSelected) {
        this.emp.enterDate = null
      }else {
        if(this.emp.enterDate !== '' && this.emp.enterDate != null){
          if(this.emp.enterDate > this.emp.startDate){
            this.$message.warning('进入时间应不大于开始时间!')
            return
          }
          this.emp.enterDate = this.dateFormatFunc(this.emp.enterDate)
        }
      }
      this.emp.haveSub = this.haveSub ? 1 : 0
      this.emp.haveComment = this.haveComment ? 1 : 0
      this.emp.requireGroup = this.requireGroup ? 1 : 0
      this.$set(this.emp,"adminID",this.user.id)
      this.emp.startDate = this.dateFormatFunc(this.emp.startDate)
      if (this.emp.id) {
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.institutionID = this.user.institutionID;
            const _this = this;
            this.postRequest("/activities/basic/update", _this.emp).then(
                (resp) => {
                  if (resp) {
                    this.dialogVisible = false;
                      this.$message({
                          type: 'success',
                          message: '修改成功!'
                      });
                    this.initEmps();
                  }
                }
            );
          }
        });
      } else { //添加活动 能看见的小于能进入的小于开始时间
        this.$refs["empForm"].validate((valid) => {
          if (valid) {
            this.emp.institutionID = this.user.institutionID;
            this.$set(this.emp,"adminID",this.user.id)
            this.emp.startDate = this.dateFormatFunc(this.emp.startDate)
            // if(this.emp.visibleDate != '' && this.emp.visibleDate != null)
            //   this.emp.visibleDate = this.dateFormatFunc(this.emp.visibleDate)
            // if(this.emp.enterDate != '' && this.emp.enterDate != null)
            //   this.emp.enterDate = this.dateFormatFunc(this.emp.enterDate)
            const _this = this;
            this.postRequest("/activities/basic/insert", _this.emp).then(
                (resp) => {
                  if (resp) {
                      this.$message({
                          type: 'success',
                          message: '添加成功!'
                      });
                    this.dialogVisible = false;
                    this.initEmps();
                  }
                }
            );
          }
        });
      }
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps("advanced");
    },
    showAddEmpView() {
      this.emptyEmp();
      this.title = "添加活动";
      this.dialogVisible = true;
    },
    initEmps() { // 在此适配不同的组件
      this.loading = true;
      this.emp_edit = {};
      if (this.mode === "admin"){ // 管理员活动管理
        let url = "/activities/basic/?page=" + this.page + "&size=" + this.size + "&institutionID=" + this.user.institutionID + "&adminID=" + this.user.id;
        this.getRequest(url).then((resp) => {
          this.loading = false;
          if (resp) {
            this.emps = resp.data;
            this.total = resp.total;
            this.emps.map(item => {
              if(item.creatorID != this.user.id){
                this.$set(item,'isShowPermissionBtn',false)
              }else this.$set(item,'isShowPermissionBtn',true)
            })
          }
        });
      }else if (this.mode === "secretary"){ // 秘书活动管理
        const id = JSON.parse(localStorage.getItem("user")).id
        this.getRequest("/secretary/getAct?teacherID="+id).then((resp)=>{
          this.loading = false;
          if (resp) { // 后续再包装成函数
            this.emps = resp.obj;
            this.total = this.emps.length;
          }
        })
      }else if (this.mode === "adminSub"){ // 管理员子活动管理
        this.getRequest("/activities/basic/sub?activityID="+this.activityID).then((resp)=>{
          this.loading = false;
          this.emps = resp.obj;
          for (let i = 0; i < this.emps.length; i++) {
              this.emps[i].requireGroup = this.emps[i].requireGroup === 1
          }
          this.total = this.emps.length
        })
      }else if (this.mode === "secretarySub"){ // 秘书子活动管理
        this.loading = false;
        // 获取当前组内的专家和学生，待修改
        this.getRequest("/secretary/getMember?activityID="+this.activityID+"&groupID="+this.groupID).then((resp)=>{
          this.experts = resp.obj[1]
          this.participates = resp.obj[0]
          // 获取所有的子活动
          this.getRequest("/activities/basic/sub?activityID="+this.activityID).then((resp)=>{
            this.loading = false;
            this.emps = resp.obj;
              for (let i = 0; i < this.emps.length; i++) {
                  this.emps[i].requireGroup = this.emps[i].requireGroup === 1
              }
            for (let i = 0; i < this.emps.length; i++) {
              this.emps[i].participantCount =  this.participates.length
              this.emps[i].expertCount = this.experts.length
            }
            this.total = this.emps.length
          })
        })
      }

    },
    showGroupmanagement(data) {
      const _this = this;
      if (this.mode === "admin"){
        _this.$router.push({
          path: "/ActivitM/table",
          query: {
            keywords: data.id,
            keyword_name: data.name,
            mode:this.mode,
            haveSub:data.haveSub,
          },
        });
      }else if (this.mode === "secretary"){
        _this.$router.push({
          path: "/ActivitM/table",
          query: {
            keywords: data.id,
            keyword_name: data.name,
            groupID:data.groupID,
            mode:this.mode,
          },
        });
      }else if (this.mode === "adminSub") {
          _this.$router.push({
              path: "/ActivitM/table",
              query: {
                  keywords: data.id,
                  keyword_name: data.name,
                  groupID: this.groupID,
                  mode: this.mode,
                  backID: this.activityID,
              },
          });
      }else if (this.mode === "secretarySub") {
          _this.$router.push({
              path: "/ActivitM/table",
              query: {
                  keywords: data.id,
                  keyword_name: data.name,
                  groupID: this.groupID, // 用于读取主活动的组内的选手
                  groupName: this.groupName,
                  mode: this.mode,
                  backID: this.activityID,
                  backActName: this.actName,
              },
          });
      }
    },
    showInsertmanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/group",
        query: {
          keywords: data.id,
          keyword_name: data.name,
          groupID:this.groupID,
          mode:this.mode,
          backID:this.activityID,
        },
      });
    },
    showSubActivity(data) {
      const _this = this;
      if (this.mode === "admin")
        _this.$router.push({query :{id:data.id}, path: "/ActivitM/SubActManage",});
      else if (this.mode === "secretary" || this.mode === "secretarySub"){
        _this.$router.push({
          query :{id:data.id,actName:data.name,groupName:data.groupName,groupID:data.groupID},
          path: "/secretary/SubActManage",});
      }

    },
    showteachermanagement(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/sobcfg",
        query: {
          keywords: data.id,
          keyword_name: data.name,
          mode:this.mode,
          backID:this.activityID,
        },
      });
    },
    showFinalScore(data){
      const _this = this;
      if (this.mode === "admin" || this.mode === "adminSub"){
          _this.$router.push({
              path: "/ActivitM/final",
              query: {
                  keywords: data.id,
                  keyword_name: data.name,
                  mode:this.mode,
                  backID:this.activityID,
              },
          });
      }else if (this.mode === "secretary"){
          _this.$router.push({
              path: "/ActivitM/final",
              query: {
                  keywords: data.id,
                  keyword_name: data.name,
                  mode:this.mode,
                  groupName:data.groupName,
                  groupID:data.groupID,
              },
          });
      }else if (this.mode === "secretarySub") {
          _this.$router.push({
              path: "/ActivitM/final",
              query: {
                  keywords: data.id,
                  keyword_name: data.name,
                  mode:this.mode,
                  backGroupID:this.groupID,
                  backActID:this.activityID,
                  backActName:this.actName,
              },
          });
      }

    },
    showScoreItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/month",
        query: {
          keywords: data.id,
          keyword_name: data.name,
          mode:this.mode,
          backID:this.activityID
        },
      });
    },
    showInfoItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/infos",
        query: {
          keywords: data.id,
          keyword_name: data.name,
          mode:this.mode,
          backID:this.activityID,
        },
      });
    },
    showTotalItem(data) {
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/total",
        query: {
          keywords: data.id,
          keyword_name: data.name,
          backID:this.activityID,
          mode:this.mode,
        },
      });
    },
    searchEmps() {
      this.loading = true;
      const _this = this;
      //let url =
      this.getRequest(
          "/activities/basic/search?company=" +
          this.keyword +
          "&page=" +
          this.page +
          "&size=" +
          this.size +
          "&institutionID=" + this.user.institutionID
      ).then((resp) => {
        this.loading = false;
        if (resp) {
          this.emps = resp.data;
          this.total = resp.total;
        }
      });
    },
      back(){
          const _this = this;
          var url = ""
          var query = ""
          if (this.isGroup){
            console.log(this.keywords);
            _this.$router.push({
              path: "/ActivitM/table",
              query:{
                 keywords:this.$route.query.keywords,
                 keywords_name:this.actName,
                 mode:"admin",
                 haveSub:this.$route.query.haveSub,
              }
            });
          }
          else{
            if (this.mode === 'adminSub'){
              url = "/ActivitM/search"
            }else if (this.mode === "secretarySub"){
              url = "/secretary/ActManage"
            }
            _this.$router.push({
              path: url,
            });
          }
      },
      showGroups(data) {
          const _this = this;
          _this.$router.push({
              path: "/ActivitM/sobcfg",
              query: {
                  activityID: data.id,
                  keywords: data.id,
                  keyword_name: data.name,
                  keywords_name:this.keywords_name,
                  groupID: data.groupID,
                  backID: this.activityID,
                  mode:this.mode,
                  haveSub:data.haveSub
              }
          })
      },
     showParticipants(data){
         const _this = this;
         _this.$router.push({
             path: "/participantsM",
             query: {
                 activityID: data.id,
                 keyword_name: data.name,
                 keywords_name:this.keywords_name,
                 groupID: data.groupID,
                 mode:this.mode
         }
       })
     },
      exportGradeForm(data){
          this.gradeForm.groupID = data.groupID
          this.gradeForm.groupName = data.groupName
          this.getRequest("/activities/basic/sub?activityID="+data.id).then((resp)=>{
              this.subActs = resp.obj
          })
          this.gradeForm.activityID = data.id
        this.exportGradeFormVisible = true;
      },
      showScoreItemByActID(actID){

      },
  },
};
</script>

<style>
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all 0.8s ease;
}

.slide-fade-leave-active {
  transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}
</style>
